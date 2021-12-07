package log_management.model;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class GeneralLogsModel {
    String message;
    long queryExecutionTime;
    List<Map<String, Long>> tableList;
    int tableCount;
    long recordCount;

    private static GeneralLogsModel generalLogsModelInstance;

    public static GeneralLogsModel getInstance() {
        if (generalLogsModelInstance == null) {
            generalLogsModelInstance = new GeneralLogsModel();
        }
        return generalLogsModelInstance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Map<String, Long>> getTableList() {
        return tableList;
    }

    public void setTableList(List<Map<String, Long>> tableList) {
        this.tableList = tableList;
    }

    public long getQueryExecutionTime() {
        return queryExecutionTime;
    }

    public void setQueryExecutionTime(long queryExecutionTime) {
        this.queryExecutionTime = queryExecutionTime;
    }

    public int getTableCount() {
        return tableCount;
    }

    public void setTableCount(int tableCount) {
        this.tableCount = tableCount;
    }

    public long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    public long getGeneralLogsEntryTimestamp() {
        return Instant.now().toEpochMilli();
    }

    public Instant getGeneralLogsTimestamp() {
        return Instant.now();
    }
}
