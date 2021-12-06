package log_management;

import log_management.constant.Files;
import log_management.model.QueryLogsModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class QueryLogsImpl implements QueryLogs {
    private static QueryLogsImpl queryLogsImplInstance = null;

    public static QueryLogsImpl getQueryLogsInstance() {
        if (queryLogsImplInstance == null) {
            queryLogsImplInstance = new QueryLogsImpl();
        }
        return queryLogsImplInstance;
    }

    /**
     * Captures SQL queries fired by the user and query submission timestamp.
     *
     * @param queryLogsModel Contains instance of QueryLogsModel and has fields such as Log Entry Timestamp, Username,
     *                       Database Name, Query, Query Submission Timestamp,and Query Status.
     *
     * @implNote User of this method needs to create instance of QueryLogsImpl class. Set Database Name, Query, and
     *           Query Status (Use Query Status Constant) fields using respective setters and then pass the instance
     *           as a parameter to this method.
     */
    @Override
    public void queryLogsEntry(QueryLogsModel queryLogsModel) {
        LogDirectorySetup logDirSetup = LogDirectorySetup.getInstance();
        File logFilesDirectory = logDirSetup.setupDirectories();
        try (BufferedWriter queryLogsWriter = new BufferedWriter(new FileWriter(logFilesDirectory.getCanonicalPath() + "\\" + Files.QUERY_LOGS_FILE, true))) {
            queryLogsWriter.append("Log Timestamp: ").append(String.valueOf(queryLogsModel.getQueryLogsEntryTimestamp()));
            queryLogsWriter.append(" |:| ");
            queryLogsWriter.append("Username: ").append(queryLogsModel.getUsername());
            queryLogsWriter.append(" |:| ");
            queryLogsWriter.append("Database: ").append(queryLogsModel.getDatabaseName());
            queryLogsWriter.append(" |:| ");
            queryLogsWriter.append("Query: ").append(queryLogsModel.getQuery());
            queryLogsWriter.append(" |:| ");
            queryLogsWriter.append("Query Submission Timestamp: ").append(String.valueOf(queryLogsModel.getQuerySubmissionTimestamp()));
            queryLogsWriter.append(" |:| ");
            queryLogsWriter.append("Status: ").append(queryLogsModel.getQueryStatus());
            queryLogsWriter.append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
