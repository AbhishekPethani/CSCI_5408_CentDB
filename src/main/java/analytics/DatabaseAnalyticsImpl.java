package analytics;

import log_management.LogDirectorySetup;
import log_management.constant.Files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class DatabaseAnalyticsImpl implements Analytics {
    private static DatabaseAnalyticsImpl databaseAnalytics = null;

    public static DatabaseAnalyticsImpl getInstance() {
        if (databaseAnalytics == null) {
            databaseAnalytics = new DatabaseAnalyticsImpl();
        }
        return databaseAnalytics;
    }

    @Override
    public void getAnalytics(String query) {
        List<String> queryTokens = new ArrayList<>(Arrays.asList(query.split(" ")));
        String databaseName = queryTokens.get(2);
        LogDirectorySetup logDirSetup = LogDirectorySetup.getInstance();
        File logFilesDirectory = logDirSetup.setupDirectories();
        try {
            File queryLogFile = new File(logFilesDirectory.getCanonicalPath() + "\\" + Files.QUERY_LOGS_FILE);
            Scanner scan = new Scanner(new FileInputStream(queryLogFile));
            List<String> userList = new ArrayList<>();
            while (scan.hasNextLine()) {
                String logEntry = scan.nextLine();
                List<String> logTokens = new ArrayList<>(Arrays.asList(logEntry.split(" \\|:\\| ")));
                String username = logTokens.get(1).replaceAll("Username: ", "");
                userList.add(username);
            }
            Set<String> uniqueUsers = new HashSet<>(userList);
            Map<String, Integer> userQueryCount = new HashMap<>();
            for (String user : uniqueUsers) {
                int queryCount = 0;
                scan = new Scanner(new FileInputStream(queryLogFile));
                while (scan.hasNextLine()) {
                    String logEntry = scan.nextLine();
                    if (logEntry.contains(user) && logEntry.contains(databaseName)) {
                        queryCount += 1;
                        userQueryCount.put(user, queryCount);
                    }
                }
            }
            for (String user : uniqueUsers) {
                System.out.println("User " + user + " Submitted " + userQueryCount.get(user) + " Queries On " + databaseName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
