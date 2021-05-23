package com.dataart.playme.controller.socket;

import com.dataart.playme.model.cabinet.Cabinet;

public interface CabinetPublisher {

    void sendMetronome(String rehearsalId, String metronomeSignal);

    void sendCabinetUpdate(Cabinet cabinet);
}
