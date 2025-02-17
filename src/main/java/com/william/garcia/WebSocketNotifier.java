package com.william.garcia;

import javax.websocket.*;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ClientEndpoint
public class WebSocketNotifier {
    private Session userSession;

    public WebSocketNotifier(URI endpointURI, String authToken, String sessionId, String clientId) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();

            ClientEndpointConfig.Configurator configurator = new ClientEndpointConfig.Configurator() {
                @Override
                public void beforeRequest(Map<String, List<String>> headers) {
                    headers.put("x-gxauth-token", Collections.singletonList(authToken));
                    headers.put("Cookie", Collections.singletonList(
                            "GX_SESSION_ID=" + sessionId + "; GX_CLIENT_ID=" + clientId
                    ));
                }
            };

            ClientEndpointConfig config = ClientEndpointConfig.Builder.create()
                    .configurator(configurator)
                    .build();

            container.connectToServer(this, endpointURI);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.userSession = session;
        System.out.println("✅ Conectado al WebSocket en: " + session.getRequestURI());
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("📩 Mensaje recibido: " + message);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("🔴 Sesión cerrada: " + reason.getReasonPhrase());
        this.userSession = null;
    }

    public void sendNotification(String message) {
        if (userSession != null && userSession.isOpen()) {
            userSession.getAsyncRemote().sendText(message);
            System.out.println("📤 Notificación enviada: " + message);
        } else {
            System.out.println("⚠️ La conexión WebSocket no está abierta.");
        }
    }

    public static void main(String[] args) {
        try {
            URI serverUri = new URI("ws://192.168.1.4:8080/GxTestJava/gxwebsocket?214f0f28-d86f-4372-bac7-a94ab1483ccb");

            // ⚠️ Usa los valores reales de los tokens de GeneXus desde los logs
            String authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJneC1leHAiOiIxNzQwMzc0MDg1MzA0IiwiZ3gtcGdtIjoiV1BKQVZBIn0.HYAi_0u2_fskyuzcQlAgY2qeItshu6P2qkTDsoDNX4M";
            String sessionId = "MrjThRRDEjlaGOZyH2dSjoqKx5p/XAgWhrFRuP4Quho=";
            String clientId = "214f0f28-d86f-4372-bac7-a94ab1483ccb";

            WebSocketNotifier notifier = new WebSocketNotifier(serverUri, authToken, sessionId, clientId);

            // Esperar para conectar
            Thread.sleep(2000);

            // Enviar notificación
            notifier.sendNotification("🔔 Proceso finalizado desde Java ✅");

            // Mantener abierta la conexión por unos segundos
            Thread.sleep(5000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
