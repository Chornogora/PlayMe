package com.dataart.playme.repository.impl;

import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.exception.socket.CabinetIllegalStateException;
import com.dataart.playme.model.cabinet.Cabinet;
import com.dataart.playme.model.cabinet.RehearsalMember;
import com.dataart.playme.repository.CabinetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CabinetInMemoryRepository implements CabinetRepository {

    private final Logger LOGGER = LoggerFactory.getLogger(CabinetInMemoryRepository.class);

    private final List<Cabinet> cabinets = new ArrayList<>();

    @Override
    public Cabinet addCabinet(Cabinet cabinet) {
        cabinets.add(cabinet);
        return cabinet;
    }

    @Override
    public Optional<Cabinet> getCabinet(String rehearsalId) {
        return cabinets.stream()
                .filter(cabinet -> cabinet.getRehearsal().getId().equals(rehearsalId))
                .findFirst();
    }

    @Override
    public Cabinet setOnline(String rehearsalId, String musicianId, String sessionId) {
        Cabinet cabinet = getCabinet(rehearsalId)
                .orElseThrow(() -> new NoSuchRecordException("Can't find cabinet by rehearsal id"));
        RehearsalMember member = cabinet.getMembers()
                .stream()
                .filter(currentMember -> currentMember.getMusician().getId().equals(musicianId))
                .findFirst()
                .orElseThrow(() -> new NoSuchRecordException("Can't find member by musician id"));
        if (member.getSessionId() == null) {
            member.setSessionId(sessionId);
        } else {
            throw new CabinetIllegalStateException("Member is already online");
        }
        return cabinet;
    }

    @Override
    public Cabinet setOffline(String sessionId) {
        try {
            Cabinet cabinet = findBySessionId(sessionId);
            cabinet.getMembers()
                    .stream()
                    .filter(member -> sessionId.equals(member.getSessionId()))
                    .forEach(member -> member.setSessionId(null));
            return cabinet;
        } catch (NoSuchRecordException e) {
            LOGGER.info("Invalid session id for cabinet: " + sessionId);
            return null;
        }
    }

    @Override
    public Cabinet findBySessionId(String sessionId) {
        return cabinets.stream()
                .filter(currentCabinet -> currentCabinet.getMembers()
                        .stream()
                        .anyMatch(member -> sessionId.equals(member.getSessionId())))
                .findFirst()
                .orElseThrow(() -> new NoSuchRecordException("Can't find cabinet by session id"));
    }
}
