package com.dataart.playme.repository;

import com.dataart.playme.model.cabinet.Cabinet;

import java.util.Optional;

public interface CabinetRepository {

    Cabinet addCabinet(Cabinet cabinet);

    Optional<Cabinet> getCabinet(String rehearsalId);

    Cabinet setOnline(String rehearsalId, String musicianId, String sessionId);

    Cabinet setOffline(String sessionId);

    Cabinet findBySessionId(String sessionId);
}
