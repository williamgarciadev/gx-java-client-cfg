package com.william.garcia;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class ApiClient {
    private static final Logger logger = LogManager.getLogger(ApiClient.class);

    // Constructor sin argumentos
    public ApiClient() {
    }

    public void ejecutarTarea() {
        ConfigReader configReader = new ConfigReader();
        // leer la propiedad "api.url" del archivo de configuración
        String apiUrl = configReader.getProperty("api.url");
        logger.info("Conectando a la API en la URL: {}", apiUrl);


    }
//    // Usar Singleton para evitar que el pool se cierre antes de tiempo
//    private static final ExecutorService executor = Executors.newCachedThreadPool();
//
//    public void ejecutarTarea() {
//        for (int i = 0; i < 5; i++) {
//            final int requestId = i;
//            try {
//                executor.execute(() -> {
//                    logger.info("Conectando a la API en la solicitud {}", requestId);
//                    try {
//                        Thread.sleep(2000);
//                        logger.info("Conexión exitosa en la solicitud {}", requestId);
//                    } catch (InterruptedException e) {
//                        logger.error("Error en la solicitud {}", requestId, e);
//                    }
//                });
//            } catch (RejectedExecutionException e) {
//                logger.error("No se pudo ejecutar la solicitud {} porque el ThreadPoolExecutor está cerrado.", requestId);
//            }
//        }
//    }
//
//
//    public void detenerExecutor() {
//        logger.info("Cerrando ThreadPoolExecutor...");
//        executor.shutdown();
//        try {
//            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
//                executor.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            executor.shutdownNow();
//        }
//    }
}
