package com.william.garcia;

import com.genexus.Application;
import com.genexus.ApplicationContext;
import com.genexus.IHttpContext;
import com.genexus.ModelContext;
import com.genexus.db.LocalUserInformation;
import com.genexus.util.IniFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MyGXConnectionAct {
    //private static final String CONFIG_FILE = "client.cfg"; // Ruta al archivo de configuración
    //private static final String NAMESPACE = "com.dlya.bantotal";
    private static String url;
    private static final String PROPERTIES_FILE = "/log4j2.component.properties";
    private String packageName;

    private String pathConfig;
    private static final Logger logger = LogManager.getLogger(MyGXConnectionAct.class);

    static {

        try (InputStream input = MyGXConnectionAct.class.getResourceAsStream(PROPERTIES_FILE)) {
            Properties properties = new Properties();
            if (input == null) {
                throw new IOException("Unable to find " + PROPERTIES_FILE);
            }
            properties.load(input);
            url = properties.getProperty("spring.datasource.url");
            //url = "jdbc:sqlserver://DEVGEOAPP:1433;databaseName=DB_GX16_U11;encrypt=true;trustServerCertificate=true";

            System.out.println("url = " + url);
            logger.info("url = " + url);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    MyGXConnectionAct() {

    }

    MyGXConnectionAct(String pathConfig, String packageName) {
        System.out.println("pathConfig = " + pathConfig);
        this.pathConfig = pathConfig;
        this.packageName = packageName;
    }

    public Connection getConnection() throws SQLException {
        try {
            // Load the SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Initialize the GeneXus environment
            Application.init(ApplicationContext.getInstance().getClass());

            // Initialize the model context
            ModelContext context = new ModelContext(MyGXConnectionAct.class);
            IHttpContext sss = context.getHttpContext();

            logger.info(" " + sss.getClass().getName());
            logger.info(" " + sss.getLanguage());
            logger.info("getDefaultPath " + sss.getDefaultPath());
            logger.info("getClientId " + sss.getClientId());
            logger.info("getRemoteAddr " + sss.getRemoteAddr());
            logger.info("getHeader " + sss.getHeader("GX_SESSION_ID"));
            logger.info("getCookie " + sss.getTheme());


//            ClientPreferences prefs = Application.getClientPreferences();
//            Field[] fields = ClientPreferences.class.getDeclaredFields();
//            Arrays.stream(fields).forEach(field -> {
//                field.setAccessible(true);
//                try {
//                    Object value = field.get(prefs);
//                    System.out.println(field.getName() + " = " + value);
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            });


//            logger.info("Métodos disponibles en IHttpContext:");
//            for (Method method : context.getClass().getMethods()) {
//                logger.info(method.getName());
//            }

//            logger.info("Métodos disponibles en IHttpContext:");
//            for (Method method : sss.getClass().getMethods()) {
//                logger.info(method.getName());
//            }


            // Leer configuración desde el archivo ini
            IniFile iniFile = new IniFile(pathConfig);
            LocalUserInformation localUserInformation = new LocalUserInformation(new com.genexus.db.Namespace(packageName, iniFile));

            String user = localUserInformation.getUser("DEFAULT");
            System.out.println("user = " + user);

            String password = localUserInformation.getPassword("DEFAULT");
            System.out.println("password = " + password);

            // Retorna la conexión
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error loading SQL Server JDBC driver", e);
        } catch (Exception e) {
            throw new SQLException("Error al inicializar la conexión de GeneXus", e);
        }
    }
}