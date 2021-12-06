package log_management;

import log_management.constant.Files;
import log_management.model.GeneralLogsModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GeneralLogsImpl implements GeneralLogs {
    private static GeneralLogsImpl generalLogsImplInstance = null;

    public static GeneralLogsImpl getGeneralLogsImplInstance() {
        if (generalLogsImplInstance == null) {
            generalLogsImplInstance = new GeneralLogsImpl();
        }
        return generalLogsImplInstance;
    }
    /**
     * Captures execution time of SQL queries and records the changes in the database such as number of tables and
     * records at a given point of time.
     *
     * @param generalLogsModel Contains instance of GeneralLogsModel and has fields such as Log Entry Timestamp,
     *                         Username, Database Name, Message, Query Execution Time, Number of Tables, Number of
     *                         Records and Timestamp.
     *
     * @implNote User of this method needs to create instance of GeneralLogsImpl class. Set Database Name, Message,
     *           Query Execution Time (Type: long), Number of Tables (Type: long) and Number of Records (Type: long)
     *           fields using respective setters and then pass the instance as a parameter to this method.
     */
    @Override
    public void generalLogsEntry(GeneralLogsModel generalLogsModel) {
        LogDirectorySetup logDirSetup = LogDirectorySetup.getInstance();
        File logFilesDirectory = logDirSetup.setupDirectories();
        try (BufferedWriter generalLogsWriter = new BufferedWriter(new FileWriter(logFilesDirectory.getCanonicalPath() + "\\" + Files.GENERAL_LOGS_FILE, true))) {
            generalLogsWriter.append("Log Timestamp: ").append(String.valueOf(generalLogsModel.getGeneralLogsEntryTimestamp()));
            generalLogsWriter.append(" |:| ");
            generalLogsWriter.append("Username: ").append(generalLogsModel.getUsername());
            generalLogsWriter.append(" |:| ");
            generalLogsWriter.append("Database: ").append(generalLogsModel.getDatabaseName());
            generalLogsWriter.append(" |:| ");
            generalLogsWriter.append("Message: ").append(generalLogsModel.getMessage());
            generalLogsWriter.append(" |:| ");
            generalLogsWriter.append("Query Execution Time: ").append(String.valueOf(generalLogsModel.getQueryExecutionTime())).append("ms");
            generalLogsWriter.append(" |:| ");
            generalLogsWriter.append("Number of Tables: ").append(String.valueOf(generalLogsModel.getTableCount()));
            generalLogsWriter.append(" |:| ");
            generalLogsWriter.append("Number of Records: ").append(String.valueOf(generalLogsModel.getRecordCount()));
            generalLogsWriter.append(" |:| ");
            generalLogsWriter.append("Timestamp: ").append(String.valueOf(generalLogsModel.getGeneralLogsTimestamp()));
            generalLogsWriter.append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
