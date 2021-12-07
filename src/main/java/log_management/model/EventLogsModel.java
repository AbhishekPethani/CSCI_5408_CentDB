package log_management.model;

import java.time.Instant;

public class EventLogsModel {
    private String message;
    private String databaseName;
    private String tableName;
    private String columnName;
    private int recordID;
    private String oldValue;
    private String newValue;
    private String eventType;
    private static EventLogsModel eventLogsModel = null;

    public static EventLogsModel getInstance() {
        if (eventLogsModel == null) {
            eventLogsModel = new EventLogsModel();
        }
        return eventLogsModel;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public long getEventLogsEntryTimestamp() {
        return Instant.now().toEpochMilli();
    }

    public Instant getEventLogsTimestamp() {
        return Instant.now();
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
