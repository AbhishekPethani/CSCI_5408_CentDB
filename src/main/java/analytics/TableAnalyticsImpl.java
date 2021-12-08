package analytics;

import log_management.LogDirectorySetup;
import log_management.constant.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

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
        String operationType = queryTokens.get(1).toUpperCase();
        System.out.println(databaseName);
        System.out.println(operationType);
        List<String> tableList = new ArrayList<String>() {{
            add("class12");
            add("class11");
            add("staff");
            add("tutors");
            add("monitors");
        }};
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
}
