package com.dataart.playme.service;

import com.dataart.playme.model.cabinet.Cabinet;
import com.dataart.playme.model.cabinet.RehearsalState;

public interface CabinetService {

    Cabinet setOnline(String rehearsalId, String musicianId, String sessionId);

    Cabinet setOffline(String sessionId);

    Cabinet switchMicrophone(String sessionId);

    RehearsalState startCountdown(String rehearsalId);
}
