package com.william.garcia;

import com.genexus.Application;
import com.genexus.ApplicationContext;
import com.genexus.IHttpContext;
import com.genexus.ModelContext;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;

public class FTPListExample {

    private static final Logger logger = LogManager.getLogger(FTPListExample.class);

    // metodo para recibir dos argumentos pathConfig y packageName
    public void ejecutar(String pathConfig, String packageName) {
        MyGXConnectionAct myGXConnectionAct = new MyGXConnectionAct(pathConfig, packageName);
        try (Connection connection = myGXConnectionAct.getConnection()) {
            queryTable(connection);
            //insertIntoTable(connection);
            //updateTable(connection);
            //deleteFromTable(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void prueba() {
        // Initialize the GeneXus environment
        Application.init(ApplicationContext.getInstance().getClass());

        // Initialize the model context
        ModelContext context = new ModelContext(MyGXConnectionAct.class);
        IHttpContext httpContext = context.getHttpContext();


        logger.info("getClientId " + httpContext.getClientId());

        // WebSocketNotifierGX.main(new String[]{}); // Llamar al método main de WebSocketNotifierGX
// fUNCIONANDO
//        String serverURL = "ws://localhost:8080/GxTestJava/gxwebsocket?f36da2ed-e2c6-473e-b073-3c41759e7982"; // Ajusta según tu servidor 3c41759e4223
//        GeneXusWebSocketClient client = new GeneXusWebSocketClient(serverURL);
//
//        ExecutorService executor = ExecutorServiceSingleton.getExecutorService(); // Obtiene el mismo ExecutorService
//
//        executor.submit(() -> {
//            try {
//                System.out.println("⏳ Ejecutando proceso...");
//                Thread.sleep(5000); // Simula una tarea en paralelo
//                System.out.println("✅ Proceso finalizado.");
//
//                // Aquí puedes enviar la notificación a GeneXus
//                // Enviar mensaje a GeneXus
//                client.sendMessage("Prueba desde Java");
//
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        });

        WebSocketNotifierGX.main(new String[]{});



    }

    private static void enviarSms() {

        logger.info("Enviando SMS...");
        String wsURL = "ws://192.168.1.4:8080/GxTestJava/gxwebsocket?214f0f28-d86f-4372-bac7-a94ab1483ccb";

        WebSocketClient client = new WebSocketClient();
        client.connect(wsURL);

        // Espera un poco para establecer la conexión
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Enviar mensaje simulando que un proceso terminó
        client.sendMessage("Proceso completado en GeneXus.");

        // Cerrar la conexión después de un tiempo
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        client.close();
    }


    public static void main(String[] args) {


        if (args.length < 2) {
            System.err.println("Error: Se requieren al menos dos argumentos.");
            return;
        }

        String pathConfig = args[0];
        String packageName = args[1];

        logger.info("args = " + args);


        logger.debug("Mensaje DEBUG: Esto solo aparece si el nivel es DEBUG");
        logger.info("Mensaje INFO: Aplicación iniciada correctamente");
        logger.warn("Mensaje WARN: Advertencia de prueba");
        logger.error("Mensaje ERROR: Algo salió mal");

        logger.info("Iniciando la aplicación...");

        MyGXConnectionAct myGXConnectionAct = new MyGXConnectionAct(pathConfig, packageName);
        try (Connection connection = myGXConnectionAct.getConnection()) {
            queryTable(connection);
            //insertIntoTable(connection);
            //updateTable(connection);
            //deleteFromTable(connection);
        } catch (Exception e) {
            e.printStackTrace();
            FTPClient ftpClient = new FTPClient();
            String server = "80.241.211.8";  // Dirección del servidor FTP
            int port = 21;                        // Puerto FTP (por defecto 21)
            String user = "ftpuser";              // Nombre de usuario
            String pass = "ftppassword";           // Contraseña

            try {
                // Conexión al servidor FTP
                System.out.println("Respuesta del servidor: " + ftpClient.getReplyString());
                logger.info("Conectando al servidor FTP...");

                ftpClient.connect(server, port);
                int replyCode = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(replyCode)) {
                    System.out.println("Error en la conexión. Código de respuesta: " + replyCode);
                    return;
                }

                // Iniciar sesión
                boolean loggedIn = ftpClient.login(user, pass);
                if (!loggedIn) {
                    System.out.println("No se pudo iniciar sesión en el servidor FTP.");
                    return;
                }

                // Se recomienda usar el modo pasivo para evitar problemas con firewalls
                ftpClient.enterLocalPassiveMode();

                // Especificar el directorio remoto a listar
                String remoteDir = "/"; // Reemplaza "/ruta" por el directorio que necesites

                // Obtener la lista de archivos y directorios
                FTPFile[] files = ftpClient.listFiles(remoteDir);
                logger.info("Listado de archivos y directorios en: {}", remoteDir);
                System.out.println("Listado de archivos y directorios en: " + remoteDir);
                for (FTPFile file : files) {
                    String details = file.getName();
                    if (file.isDirectory()) {
                        details = "[DIR] " + details;
                    } else {
                        details = "[FILE] " + details;
                    }
                    logger.info(details);
                    System.out.println(details);
                }

                // Cerrar sesión y desconectar
                ftpClient.logout();
                ftpClient.disconnect();

            } catch (IOException ex) {
                System.err.println("Se produjo un error durante la operación FTP: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private static void queryTable(Connection connection) {
        String query = "SELECT top 100 * FROM WWP_UserExtended ";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String id = resultSet.getString("WWPUserExtendedId").trim();
                String nombre = resultSet.getString("WWPUserExtendedName");
                System.out.println("ID: " + id + ", WWPUserExtendedName: " + nombre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}