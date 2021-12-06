package log_management;

import log_management.constant.Directory;

import java.io.File;
import java.io.IOException;

public class LogDirectorySetup {
    private static LogDirectorySetup instance = null;
    File logFilesDirectory = null;

    public static LogDirectorySetup getInstance() {
        if (instance == null) {
            instance = new LogDirectorySetup();
        }
        return instance;
    }

    /**
     * This method checks whether the log_files directory exist or not and creates log_files directory if it does not
     * exist. It uses the path of log_management package. The log_files directory stores different types of log files.
     *
     * @return The method return File class instance logFilesDirectory which contains relative path of log_files
     * directory.
     * @implNote User of this method needs to create instance of LogDirectorySetup class and call this method using
     * the instance while making log entry inside any kind of log.
     */
    public File setupDirectories() {
        File logManagementDirectory = new File(Directory.LOG_MANAGEMENT_DIRECTORY);
        try {
            logFilesDirectory = new File(logManagementDirectory.getCanonicalPath() + "\\" + Directory.LOG_FILES_DIRECTORY);
            if (!logFilesDirectory.exists()) {
                if (logFilesDirectory.mkdirs()) {
                    System.out.println(logFilesDirectory.getName() + " Directory Created!");
                }
            }
            return logFilesDirectory;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
