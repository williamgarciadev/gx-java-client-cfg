package com.william.garcia;

import com.genexus.ModelContext;
import com.genexus.internet.GXWebNotificationInfo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class WebSocketNotifierGX {
    private static final Logger logger = Logger.getLogger(WebSocketNotifierGX.class.getName());
    private static final String SERVER_URL = "ws://localhost:8080/GxTestJava/gxwebsocket?f36da2ed-e2c6-473e-b073-3c41759e7982"; // Ajusta ClientId
    // OBTENER CLIENTID GENEXUS
    // 1. Iniciar la aplicación web en GeneXus
    // 2. Abrir la consola del navegador (F12)
    // 3. Ejecutar el siguiente código en la consola:
    //    window.gx.ajax.getPostData().clientId




    public static void main(String[] args) {
        logger.info("Iniciando proceso en paralelo...");

        // Conectar al WebSocket de GeneXus
        GeneXusWebSocketClient client = new GeneXusWebSocketClient(SERVER_URL);

        // Crear un ExecutorService para la ejecución en paralelo
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            try {
                logger.info("⏳ Ejecutando proceso...");
                Thread.sleep(5000); // Simula un proceso en paralelo
                logger.info("✅ Proceso finalizado.");

                // Crear mensaje estructurado para GeneXus
                ModelContext context = new ModelContext(WebSocketNotifierGX.class);

                // Crear una instancia de la notificación con datos válidos
                NotificationMessage message = new NotificationMessage(context);
                message.setStatus("✅ Finalizado");
                message.setDetail("Proceso ejecutado correctamente desde Java.");

                // Crear el objeto GXWebNotificationInfo para enviar la notificación
                GXWebNotificationInfo notificationInfo = new GXWebNotificationInfo(0, context, "NotificationInfo");
                notificationInfo.setId("GENEXUS_PROCESS");
                notificationInfo.setObject("ProcesoJava");
                notificationInfo.setGroupName("GENEXUS_NOTIFICATIONS");
                notificationInfo.setMessage(message);  // Aquí asignamos el mensaje correctamente

                // Enviar el mensaje a GeneXus vía WebSocket
                String jsonMessage = notificationInfo.toJSonString();
                logger.info("📤 JSON generado antes de enviar: " + jsonMessage); // <-- Agregar esta línea
                client.sendMessage(jsonMessage);



                logger.info("📤 Notificación enviada a GeneXus correctamente.");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.severe("❌ Error en el proceso: " + e.getMessage());
            }
        });

        executor.shutdown();
    }
}
