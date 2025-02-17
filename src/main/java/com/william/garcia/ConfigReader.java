package com.william.garcia;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static final Properties properties = new Properties();

    static {
        // Detectar el perfil activo: Si no se pasa, usa "dev" por defecto.
        String profile = System.getProperty("env", "dev");
        String propertiesFile = "config-" + profile + ".properties";

        System.out.println("Cargando configuración para el perfil: " + profile);
        logger.info("Cargando configuración para el perfil: {}", profile);

        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(propertiesFile)) {
            if (input == null) {
                throw new IOException("Archivo de configuración no encontrado: " + propertiesFile);
            }
            properties.load(input);
            System.setProperty("log.level", properties.getProperty("log.level"));
            System.setProperty("log.file", properties.getProperty("log.file"));

            logger.info("Configuración cargada correctamente desde {}", propertiesFile);
        } catch (IOException e) {
            logger.error("Error al cargar configuración", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
