package com.william.garcia;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.*;
import java.net.URI;

@ClientEndpoint
public class GeneXusWebSocketClient {
    private static Session userSession = null;
    private static final Logger logger = LogManager.getLogger(GeneXusWebSocketClient.class);

    public GeneXusWebSocketClient(String serverURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI(serverURI));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        logger.info("✅ Conectado al WebSocket de GeneXus.");
        userSession = session;
    }

    @OnMessage
    public void onMessage(String message) {
        logger.info("📩 Mensaje recibido desde GeneXus: " + message);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        logger.info("🔌 Conexión cerrada con GeneXus: " + reason.getReasonPhrase());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
       logger.error("❌ Error en la conexión con GeneXus: " + throwable.getMessage());
    }

    public void sendMessage(String message) {
        try {
            if (userSession != null && userSession.isOpen()) {
                userSession.getBasicRemote().sendText(message);
                logger.info("📤 Mensaje enviado a GeneXus: " + message);
            } else {
                logger.error("❌ Error al enviar mensaje: la conexión no está abierta.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
