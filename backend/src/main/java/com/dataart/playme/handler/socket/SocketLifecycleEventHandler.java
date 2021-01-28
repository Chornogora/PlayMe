package com.dataart.playme.handler.socket;

import com.dataart.playme.controller.socket.CabinetController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

@Component
public class SocketLifecycleEventHandler implements WebSocketHandlerDecoratorFactory {

    private CabinetController controller;

    @Autowired
    public void setController(CabinetController controller) {
        this.controller = controller;
    }

    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {
        return new WebSocketHandlerDecorator(handler) {

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                String sessionId = session.getId();
                controller.setOffline(sessionId);
                super.afterConnectionClosed(session, closeStatus);
            }
        };
    }
}
