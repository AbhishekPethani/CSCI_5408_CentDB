package log_management.utils;

import log_management.constant.Directory;
import log_management.constant.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class UserSessionProperties {
    private static final Properties properties = new Properties();

    public static Properties getUserSessionProperties() {
        try {
            File resourcesDirectory = new File(Directory.RESOURCES_DIRECTORY);
            File userSessionPropertiesFile = new File(resourcesDirectory.getCanonicalPath() + "\\" + Files.USER_SESSION_PROPERTIES_FILE);
            FileInputStream fileInputStream = new FileInputStream(userSessionPropertiesFile.getCanonicalPath());
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}

