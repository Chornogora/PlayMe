package com.dataart.playme.controller.socket;

import com.dataart.playme.dto.cabinet.EnterMessage;
import com.dataart.playme.exception.socket.SocketException;
import com.dataart.playme.model.cabinet.Cabinet;
import com.dataart.playme.model.cabinet.RehearsalState;
import com.dataart.playme.service.CabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class CabinetController {

    private final CabinetService cabinetService;

    private final SimpMessagingTemplate template;

    @Autowired
    public CabinetController(CabinetService cabinetService, SimpMessagingTemplate template) {
        this.cabinetService = cabinetService;
        this.template = template;
    }

    @Transactional
    @MessageMapping("/connect")
    public void enterCabinet(@Payload EnterMessage message, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();

        Cabinet cabinet = cabinetService.setOnline(message.getRehearsalId(),
                message.getMusicianId(), sessionId);
        sendCabinetStatus(cabinet);
    }

    @MessageMapping("/switch-microphone")
    public void switchMicrophone(String sessionId) {
        Cabinet cabinet = cabinetService.switchMicrophone(sessionId);
        sendCabinetStatus(cabinet);
    }

    @MessageMapping("/start-countdown")
    public void startCountdown(String rehearsalId) {
        RehearsalState state = cabinetService.startCountdown(rehearsalId);
        String stateAsText = state.name();
        sendRehearsalState(stateAsText, rehearsalId);
    }

    @MessageExceptionHandler(value = {SocketException.class})
    public void handleException(SocketException exception) {
        template.convertAndSend(String.format("/cabinet/%s/error/%s", exception.getRehearsalId(),
                exception.getMusicianId()), exception);
    }

    public void setOffline(String sessionId) {
        Cabinet cabinet = cabinetService.setOffline(sessionId);
        if (cabinet != null) {
            sendCabinetStatus(cabinet);
        }
    }

    private void sendCabinetStatus(Cabinet cabinet) {
        String path = "/cabinet/" + cabinet.getRehearsal().getId() + "/state";
        template.convertAndSend(path, cabinet);
    }

    private void sendRehearsalState(String state, String rehearsalId) {
        String path = "/cabinet/rehearsal/" + rehearsalId + "/state";
        template.convertAndSend(path, state);
    }
}
