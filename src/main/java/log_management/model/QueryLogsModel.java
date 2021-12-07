package log_management.model;

import java.time.Instant;

public class QueryLogsModel {
    private String queryStatus;
    private String query;
    private static QueryLogsModel queryLogModelInstance = null;

    public static QueryLogsModel getQueryLogModelInstance() {
        if (queryLogModelInstance == null) {
            queryLogModelInstance = new QueryLogsModel();
        }
        return queryLogModelInstance;
    }

    public long getQueryLogsEntryTimestamp() {
        return Instant.now().toEpochMilli();
    }

    public String getQueryStatus() {
        return queryStatus;
    }

    public void setQueryStatus(String queryStatus) {
        this.queryStatus = queryStatus;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Instant getQuerySubmissionTimestamp() {
        return Instant.now();
    }
}
