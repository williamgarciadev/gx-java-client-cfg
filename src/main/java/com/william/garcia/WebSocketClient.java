package com.william.garcia;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import javax.websocket.*;

@ClientEndpoint
public class WebSocketClient {

    private Session session;
    private static final Logger logger = LogManager.getLogger(WebSocketClient.class);
    public void connect(String webSocketURL) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, URI.create(webSocketURL));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("Conectado al WebSocket: " + session.getRequestURI());
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Mensaje recibido: " + message);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("Conexión cerrada: " + closeReason);
    }

    public void sendMessage(String message) {
        try {
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(message);
            } else {
                System.out.println("No hay una conexión activa.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (session != null) {
                session.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
