package com.dataart.playme.service.impl;

import com.dataart.playme.controller.socket.CabinetPublisher;
import com.dataart.playme.dto.cabinet.UpdateMetronomeMessage;
import com.dataart.playme.exception.NoSuchRecordException;
import com.dataart.playme.exception.socket.CabinetIllegalStateException;
import com.dataart.playme.exception.socket.SocketException;
import com.dataart.playme.model.Rehearsal;
import com.dataart.playme.model.cabinet.Cabinet;
import com.dataart.playme.model.cabinet.RehearsalMember;
import com.dataart.playme.model.cabinet.RehearsalState;
import com.dataart.playme.repository.CabinetRepository;
import com.dataart.playme.repository.RehearsalRepository;
import com.dataart.playme.service.CabinetService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CabinetServiceImpl implements CabinetService {

    private final Logger LOGGER = LoggerFactory.getLogger(CabinetServiceImpl.class);

    private static final String ALREADY_CONNECTED_EXCEPTION_NAME = "already-connected";

    private static final String TICK_FILEPATH = "./src/main/resources/media/tick.mp3";

    private final CabinetPublisher cabinetPublisher;

    private final RehearsalRepository rehearsalRepository;

    private final CabinetRepository cabinetRepository;

    @Autowired
    public CabinetServiceImpl(@Lazy CabinetPublisher cabinetPublisher,
                              RehearsalRepository rehearsalRepository, CabinetRepository cabinetRepository) {
        this.cabinetPublisher = cabinetPublisher;
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

    @Override
    public RehearsalState start(String rehearsalId) {
        Cabinet cabinet = cabinetRepository.getCabinet(rehearsalId)
                .orElseThrow(() -> new NoSuchRecordException("Can't find cabinet by rehearsal id"));
        cabinet.setRehearsalState(RehearsalState.STARTED);
        new MetronomeTimerTask(cabinet).run();
        return RehearsalState.STARTED;
    }

    @Override
    public RehearsalState stop(String rehearsalId) {
        Cabinet cabinet = cabinetRepository.getCabinet(rehearsalId)
                .orElseThrow(() -> new NoSuchRecordException("Can't find cabinet by rehearsal id"));
        cabinet.setRehearsalState(RehearsalState.STOPPED);
        return RehearsalState.STOPPED;
    }

    @Override
    public Cabinet updateMetronome(UpdateMetronomeMessage message) {
        Cabinet cabinet = cabinetRepository.getCabinet(message.getRehearsalId())
                .orElseThrow(() -> new NoSuchRecordException("Can't find cabinet by rehearsal id"));
        boolean wasDisabled = !cabinet.getMetronomeConfiguration().isEnabled();
        cabinet.setMetronomeConfiguration(message.getMetronomeConfiguration());
        if (wasDisabled && message.getMetronomeConfiguration().isEnabled()) {
            new MetronomeTimerTask(cabinet).run();
        }
        return cabinet;
    }

    @Override
    public void updateRehearsal(String rehearsalId) {
        Rehearsal rehearsal = rehearsalRepository.findById(rehearsalId)
                .orElseThrow(() -> new NoSuchRecordException("Can't find rehearsal by id"));
        Cabinet cabinet = cabinetRepository.getCabinet(rehearsalId)
                .orElseThrow(() -> new NoSuchRecordException("Can't find cabinet by rehearsal id"));
        cabinet.setRehearsal(rehearsal);
        cabinetPublisher.sendCabinetUpdate(cabinet);
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

    private class MetronomeTimerTask extends TimerTask {

        String tick;

        private final Cabinet cabinet;

        private MetronomeTimerTask(Cabinet cabinet) {
            this.cabinet = cabinet;
        }

        @Override
        public void run() {
            if (shouldTick()) {
                try {
                    long delay = 60000 / getTempo();
                    String tick = getTick();
                    new Timer().schedule(new MetronomeTimerTask(cabinet), delay);
                    cabinetPublisher.sendMetronome(cabinet.getRehearsal().getId(), tick);
                } catch (NoSuchRecordException e) {
                    LOGGER.info(e.getMessage());
                    cabinet.getMetronomeConfiguration().setEnabled(false);
                    cabinetPublisher.sendCabinetUpdate(cabinet);
                }
            } else if (!isAnyoneOnline()) {
                this.cabinet.getMetronomeConfiguration().setEnabled(false);
            }
        }

        private boolean shouldTick() {
            return cabinet.getRehearsalState() == RehearsalState.STARTED &&
                    cabinet.getMetronomeConfiguration().isEnabled() &&
                    isAnyoneOnline();
        }

        private boolean isAnyoneOnline() {
            return cabinet.getMembers().stream()
                    .anyMatch(member -> member.getSessionId() != null);
        }

        private String getTick() {
            try {
                if (tick == null) {
                    File file = new File(TICK_FILEPATH);
                    byte[] tickArray = Files.readAllBytes(file.toPath());
                    tick = Base64.getEncoder().encodeToString(tickArray);
                }
                return tick;
            } catch (IOException e) {
                e.printStackTrace();
                return StringUtils.EMPTY;
            }
        }

        private int getTempo() {
            String metronomeId = cabinet.getMetronomeConfiguration().getMetronomeId();
            return cabinet.getRehearsal().getMetronomes()
                    .stream()
                    .filter(currentMetronome -> currentMetronome.getId().equals(metronomeId))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchRecordException("Can't find metronome by id"))
                    .getTempo();
        }
    }
}
