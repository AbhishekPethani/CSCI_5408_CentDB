package analytics;

import DataModelling.GenerateERD;
import log_management.EventLogsImpl;
import log_management.LogDirectorySetup;
import log_management.constant.Event;
import log_management.constant.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import FileParsing.FileOperation;
import log_management.model.EventLogsModel;

public class TableAnalyticsImpl implements Analytics {
    private static TableAnalyticsImpl tableAnalytics = null;

    public static TableAnalyticsImpl getInstance() {
        if (tableAnalytics == null) {
            tableAnalytics = new TableAnalyticsImpl();
        }
        return tableAnalytics;
    }

    @Override
    public void getAnalytics(String query) {
        List<String> queryTokens = new ArrayList<>(Arrays.asList(query.split(" ")));
        String databaseName = queryTokens.get(2);
<<<<<<< HEAD
        String operationType = queryTokens.get(1).toUpperCase();
        System.out.println(databaseName);
        System.out.println(operationType);
        List<String> tableList = new ArrayList<String>() {{
            add("class12");
            add("class11");
            add("staff");
            add("tutors");
            add("monitors");
=======
        ArrayList keywordList = new ArrayList() {{
            add("CREATE");
            add("UPDATE");
            add("DELETE");
            add("INSERT");
            add("DROP");
            add("USE");
            add("ALTER");
>>>>>>> dd61f904a9d9dc4c4e377bad2687514ee9f8266a
        }};
        String operationType = queryTokens.get(1).toUpperCase();
        if (GenerateERD.databaseExist(databaseName) && keywordList.contains(operationType)) {
            System.out.println(databaseName);
            System.out.println(operationType);
            FileOperation fileOperation = new FileOperation();
            List<String> tableList = fileOperation.getTableNames(databaseName);
            Map<String, Integer> tableQueryCount = new HashMap<>();
            LogDirectorySetup logDirSetup = LogDirectorySetup.getInstance();
            File logFilesDirectory = logDirSetup.setupDirectories();
            try {
                File queryLogFile = new File(logFilesDirectory.getCanonicalPath() + "\\" + Files.QUERY_LOGS_FILE);
                for (String table : tableList) {
                    Scanner scan = new Scanner(new FileInputStream(queryLogFile));
                    int queryCount = 0;
                    while (scan.hasNextLine()) {
                        String logEntry = scan.nextLine();
                        if (logEntry.contains(operationType) && logEntry.contains(table)) {
                            queryCount += 1;
                            tableQueryCount.put(table, queryCount);
                        }
                    }
                }
                for (String table : tableList) {
                    System.out.println("Total " + tableQueryCount.get(table) + " " + operationType + " Operations Were Performed On " + table + " Table");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(!GenerateERD.databaseExist(databaseName)){
            String errorMessage = "Database Does Not Exist";
            EventLogsModel eventLogsModel = EventLogsModel.getInstance();
            eventLogsModel.setEventType(Event.CRASH_REPORT);
            eventLogsModel.setMessage(errorMessage);
            eventLogsModel.setDatabaseName(databaseName);
            EventLogsImpl eventLogs = EventLogsImpl.getInstance();
            eventLogs.eventLogsEntry(eventLogsModel);
        }
        if(!keywordList.contains(operationType)){
            String errorMessage = "No Such Operation Exist";
            EventLogsModel eventLogsModel = EventLogsModel.getInstance();
            eventLogsModel.setEventType(Event.CRASH_REPORT);
            eventLogsModel.setMessage(errorMessage);
            eventLogsModel.setDatabaseName(databaseName);
            EventLogsImpl eventLogs = EventLogsImpl.getInstance();
            eventLogs.eventLogsEntry(eventLogsModel);
        }

    }
}
