package log_management;

import log_management.constant.Event;
import log_management.constant.Files;
import log_management.model.EventLogsModel;
import log_management.utils.UserSessionUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EventLogsImpl implements EventLogs {
    private static EventLogsImpl eventLogsImplInstance = null;

    public static EventLogsImpl getInstance() {
        if (eventLogsImplInstance == null) {
            eventLogsImplInstance = new EventLogsImpl();
        }
        return eventLogsImplInstance;
    }

    @Override
    public void eventLogsEntry(EventLogsModel eventLogsModel) {
        LogDirectorySetup logDirSetup = LogDirectorySetup.getInstance();
        File logFilesDirectory = logDirSetup.setupDirectories();
        try (BufferedWriter eventLogsWriter = new BufferedWriter(new FileWriter(logFilesDirectory.getCanonicalPath() + "\\" + Files.EVENT_LOGS_FILE))) {
            if (eventLogsModel.getEventType().equals(Event.DB_CHANGE)) {
                eventLogsWriter.append("Log Timestamp: ").append(String.valueOf(eventLogsModel.getEventLogsEntryTimestamp()));
                eventLogsWriter.append(" |:| ");
                eventLogsWriter.append("Username: ").append(UserSessionUtils.getUsername());
                eventLogsWriter.append(" |:| ");
                eventLogsWriter.append("Database: ").append(eventLogsModel.getDatabaseName());
                eventLogsWriter.append(" |:| ");
                eventLogsWriter.append("Message: ").append(eventLogsModel.getMessage());
                eventLogsWriter.append(" |:| ");
                eventLogsWriter.append("Timestamp: ").append(String.valueOf(eventLogsModel.getEventLogsTimestamp()));
                eventLogsWriter.append("\n");
            } else if (eventLogsModel.getEventType().equals(Event.CRASH_REPORT)) {
                eventLogsWriter.append("Log Timestamp: ").append(String.valueOf(eventLogsModel.getEventLogsEntryTimestamp()));
                eventLogsWriter.append(" |:| ");
                eventLogsWriter.append("Username: ").append(UserSessionUtils.getUsername());
                eventLogsWriter.append(" |:| ");
                eventLogsWriter.append("Database: ").append(eventLogsModel.getDatabaseName());
                eventLogsWriter.append(" |:| ");
                eventLogsWriter.append("Error: ").append(eventLogsModel.getMessage());
                eventLogsWriter.append(" |:| ");
                eventLogsWriter.append("Timestamp: ").append(String.valueOf(eventLogsModel.getEventLogsTimestamp()));
                eventLogsWriter.append("\n");
            } else if (eventLogsModel.getEventType().equals(Event.UPDATE_OPERATION)) {
                eventLogsWriter.append("Log Timestamp: ").append(String.valueOf(eventLogsModel.getEventLogsEntryTimestamp()));
                eventLogsWriter.append(" |:| ");
                eventLogsWriter.append("Username: ").append(UserSessionUtils.getUsername());
                eventLogsWriter.append(" |:| ");
                eventLogsWriter.append("Database: ").append(eventLogsModel.getDatabaseName());
                eventLogsWriter.append(" |:| ");
                eventLogsWriter.append("Message: ").append(eventLogsModel.getMessage());
                eventLogsWriter.append(" |:| ");
                eventLogsWriter.append("Table: ").append(eventLogsModel.getTableName());
                eventLogsWriter.append(" |:| ");
                eventLogsWriter.append("Column: ").append(eventLogsModel.getColumnName());
                eventLogsWriter.append(" |:| ");
                eventLogsWriter.append("Record ID: ").append(String.valueOf(eventLogsModel.getRecordID()));
                eventLogsWriter.append(" |:| ");
                eventLogsWriter.append("Old Value: ").append(eventLogsModel.getOldValue());
                eventLogsWriter.append(" |:| ");
                eventLogsWriter.append("New Value: ").append(eventLogsModel.getNewValue());
                eventLogsWriter.append(" |:| ");
                eventLogsWriter.append("Timestamp: ").append(String.valueOf(eventLogsModel.getEventLogsTimestamp()));
                eventLogsWriter.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
