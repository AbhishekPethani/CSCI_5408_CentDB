package log_management.utils;

import log_management.constant.Directory;
import log_management.constant.Files;

import java.io.*;
import java.time.Instant;
import java.util.Properties;

public class UserSessionUtils {
    private static String username;
    private static String loginTimestamp;
    private static String databaseName;

    public static String getUsername() {
        Properties properties = UserSessionProperties.getUserSessionProperties();
        username = properties.getProperty("centdb.username");
        return username;
    }


    public static String getLoginTimestamp() {
        Properties properties = UserSessionProperties.getUserSessionProperties();
        loginTimestamp = properties.getProperty("centdb.loginTimestamp");
        return loginTimestamp;
    }

    public static String getDatabaseName() {
        Properties properties = UserSessionProperties.getUserSessionProperties();
        databaseName = properties.getProperty("centdb.databaseName");
        return databaseName;
    }

    public void setUserSession(String username, Instant loginTimestamp) {
        Properties properties = UserSessionProperties.getUserSessionProperties();
        try {
            File resourcesDirectory = new File(Directory.RESOURCES_DIRECTORY);
            File userSessionPropertiesFile = new File(resourcesDirectory.getCanonicalPath() + "\\" + Files.USER_SESSION_PROPERTIES_FILE);
            FileOutputStream outputStream = new FileOutputStream(userSessionPropertiesFile.getCanonicalPath());
            properties.setProperty("centdb.username", username);
            properties.setProperty("centdb.loginTimestamp", String.valueOf(loginTimestamp));
            properties.store(outputStream, null);
            System.out.println("User Session Stored Successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setDatabaseName(String databaseName) {
        Properties properties = UserSessionProperties.getUserSessionProperties();
        try {
            File resourcesDirectory = new File(Directory.RESOURCES_DIRECTORY);
            File userSessionPropertiesFile = new File(resourcesDirectory.getCanonicalPath() + "\\" + Files.USER_SESSION_PROPERTIES_FILE);
            FileOutputStream outputStream = new FileOutputStream(userSessionPropertiesFile.getCanonicalPath());
            properties.setProperty("centdb.databaseName", databaseName);
            properties.store(outputStream, null);
            System.out.println("Database Information Stored Successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
