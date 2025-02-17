package com.william.garcia;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        System.out.println("args = " + args);
        System.out.println("System.getProperty(\"java.class.path\") = " + System.getProperty("java.class.path"));


        logger.info("Iniciando la aplicación...");
        System.out.println(" Iniciando la aplicación...");

        ConfigReader configReader = new ConfigReader();
        // leer la propiedad "api.url" del archivo de configuración
        String apiUrl = configReader.getProperty("api.url");
        logger.info("Conectando a la API en la URL: {}", apiUrl);
    }

    public void ejecutarTarea() {
        String path = System.getProperty("user.dir"); // Obtiene el path de ejecución
        System.out.println("El path actual es: " + path);
    }
}