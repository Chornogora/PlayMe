package com.dataart.playme.service.impl;

import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.exception.socket.CabinetIllegalStateException;
import com.dataart.playme.exception.socket.SocketException;
import com.dataart.playme.model.cabinet.Cabinet;
import com.dataart.playme.model.Rehearsal;
import com.dataart.playme.model.cabinet.RehearsalMember;
import com.dataart.playme.model.cabinet.RehearsalState;
import com.dataart.playme.repository.CabinetRepository;
import com.dataart.playme.repository.RehearsalRepository;
import com.dataart.playme.service.CabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CabinetServiceImpl implements CabinetService {

    private static final String ALREADY_CONNECTED_EXCEPTION_NAME = "already-connected";

    private final RehearsalRepository rehearsalRepository;

    private final CabinetRepository cabinetRepository;

    @Autowired
    public CabinetServiceImpl(RehearsalRepository rehearsalRepository, CabinetRepository cabinetRepository) {
        this.rehearsalRepository = rehearsalRepository;
        this.cabinetRepository = cabinetRepository;
    }

    @Override
    public Cabinet setOnline(String rehearsalId, String musicianId, String sessionId) {
        cabinetRepository.getCabinet(rehearsalId)
                .orElse(createCabinet(rehearsalId));
        try {
            return cabinetRepository.setOnline(rehearsalId, musicianId, sessionId);
        } catch (CabinetIllegalStateException e) {
            throw new SocketException(rehearsalId, musicianId, ALREADY_CONNECTED_EXCEPTION_NAME,
                    "Member is already connected");
        }
    }

    @Override
    public Cabinet setOffline(String sessionId) {
        return cabinetRepository.setOffline(sessionId);
    }

    @Override
    public Cabinet switchMicrophone(String sessionId) {
        Cabinet cabinet = cabinetRepository.findBySessionId(sessionId);
        cabinet.getMembers()
                .stream()
                .filter(member -> sessionId.equals(member.getSessionId()))
                .findFirst()
                .ifPresent(member -> member.setMicrophoneEnabled(!member.isMicrophoneEnabled()));
        return cabinet;
    }

    @Override
    public RehearsalState startCountdown(String rehearsalId) {
        Cabinet cabinet = cabinetRepository.getCabinet(rehearsalId)
                .orElseThrow(() -> new NoSuchRecordException("Can't find cabinet by rehearsal id"));
        cabinet.setRehearsalState(RehearsalState.COUNTDOWN);
        return RehearsalState.COUNTDOWN;
    }

    private Cabinet createCabinet(String rehearsalId) {
        Rehearsal rehearsal = rehearsalRepository.findById(rehearsalId)
                .orElseThrow(() -> new NoSuchRecordException("Can't find rehearsal by id"));
        Cabinet cabinet = new Cabinet();
        cabinet.setRehearsal(rehearsal);
        cabinet.setMembers(
                Stream.concat(rehearsal.getMembers().stream(), Stream.of(rehearsal.getCreator()))
                        .map(musician -> {
                            RehearsalMember member = new RehearsalMember();
                            member.setMusician(musician);
                            return member;
                        })
                        .collect(Collectors.toList()));
        return cabinetRepository.addCabinet(cabinet);
    }
}
