package com.dataart.playme.service;

import com.dataart.playme.dto.cabinet.UpdateMetronomeMessage;
import com.dataart.playme.model.cabinet.Cabinet;
import com.dataart.playme.model.cabinet.RehearsalState;

public interface CabinetService {

    Cabinet findById(String rehearsalId);

    Cabinet findBySessionId(String sessionId);

    Cabinet setOnline(String rehearsalId, String musicianId, String sessionId);

    Cabinet setOffline(String sessionId);

    Cabinet switchMicrophone(String sessionId);

    RehearsalState startCountdown(String rehearsalId);

    RehearsalState start(String rehearsalId);

    RehearsalState stop(String rehearsalId);

    Cabinet updateMetronome(UpdateMetronomeMessage message);

    void updateRehearsal(String rehearsalId);

    Cabinet updatePinnedStatus(String musicianId, boolean pinnedStatus,
                               String sessionId);
}
