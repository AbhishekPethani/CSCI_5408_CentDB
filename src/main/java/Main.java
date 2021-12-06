import analytics.DatabaseAnalyticsImpl;
import analytics.TableAnalyticsImpl;
//import log_management.GeneralLogsImpl;
//import log_management.QueryLogsImpl;
//import log_management.constant.QueryStatus;
//import log_management.model.GeneralLogsModel;
//import log_management.model.QueryLogsModel;
//import log_management.utils.UserSessionUtils;
//
//import java.time.Instant;

public class Main {
    public static void main(String[] args) {
//        long instant1 = Instant.now().toEpochMilli();
//        System.out.println(instant1);
//        QueryLogsModel queryLogsModel = QueryLogsModel.getQueryLogModelInstance();
//        queryLogsModel.setUsername("vivekpatel810");
//        queryLogsModel.setDatabaseName("student");
//        queryLogsModel.setQuery("CREATE DATABASE dalhousie");
//        queryLogsModel.setQueryStatus(QueryStatus.SUCCESS);
//        QueryLogsImpl queryLogsImpl = QueryLogsImpl.getQueryLogsInstance();
//        queryLogsImpl.queryLogsEntry(queryLogsModel);
//        long instant2 = Instant.now().toEpochMilli();
//        System.out.println(instant2);
//        long queryExecutionTime = instant2 - instant1;
//        System.out.println(queryExecutionTime + " milliseconds");
//        GeneralLogsModel generalLogsModel = GeneralLogsModel.getInstance();
//        generalLogsModel.setUsername("abhishek1997");
//        generalLogsModel.setDatabaseName("dalhousie");
//        generalLogsModel.setMessage("Table Has Been Dropped Successfully!");
//        generalLogsModel.setQueryExecutionTime(queryExecutionTime);
//        generalLogsModel.setTableCount(1000);
//        generalLogsModel.setRecordCount(300000);
//        GeneralLogsImpl generalLogsImpl = GeneralLogsImpl.getGeneralLogsImplInstance();
//        generalLogsImpl.generalLogsEntry(generalLogsModel);
//        UserSessionUtils.setUserSession("vivekpatel810", Instant.now());
//        System.out.println("Username: " + UserSessionUtils.getUsername() + " Login Timestamp: " + UserSessionUtils.getLoginTimestamp());
//        UserSessionUtils.setDatabaseName("centDB");
//        System.out.println("Database In Use: " + UserSessionUtils.getDatabaseName());
        //DatabaseAnalyticsImpl databaseAnalytics = DatabaseAnalyticsImpl.getInstance();
        //databaseAnalytics.getAnalytics("count queries student");
        TableAnalyticsImpl tableAnalytics = TableAnalyticsImpl.getInstance();
        tableAnalytics.getAnalytics("count update student");
    }
}
