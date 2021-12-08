package UserInterface_LoginSecurity;

import DataModelling.GenerateERD;
import DataModelling.IGenerateERD;
import Exceptions.InvalidSQLQueryException;
import Exceptions.InvalidTransactionRequestException;
import ExportData.GenerateSQLDump;
import ExportData.IGenerateSQLDump;
import QueryProcessor.Processor;
import analytics.DatabaseAnalyticsImpl;
import analytics.TableAnalyticsImpl;
import log_management.utils.UserSessionUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class UserInterface {

    String userName="";
    Processor processor = null;

    public void welcomeScreen() throws NoSuchAlgorithmException, IOException {
        this.processor = new Processor();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome!");
        System.out.println("1. Login\n2.Register");
        int input = scanner.nextInt();
        switch (input){
            case 1:
                login();
                break;
            case 2:
                registration();
                break;
            default:
                System.out.println("Enter valid input");
                welcomeScreen();
        }
    }

    public void login() throws NoSuchAlgorithmException, IOException {
        UserSessionUtils userSessionUtils = new UserSessionUtils();
        Scanner scanner = new Scanner(System.in);
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        String USERNAME_INPUT;
        String PASSWORD_INPUT;
        String USERNAME_CHECK = "";
        String PASSWORD_CHECK = "";
        String SECURITY_QUESTION = "";
        String SECURITY_ANSWER_INPUT = "";
        String SECURITY_ANSWER_CHECK = "";
        System.out.println("Welcome!");
        System.out.println("Enter username:   ");
        USERNAME_INPUT = scanner.nextLine();
        userName = USERNAME_INPUT;
        System.out.println("Enter Password:   ");
        PASSWORD_INPUT = scanner.nextLine();
        messageDigest.update(USERNAME_INPUT.getBytes());
        byte[] resultByteArray = messageDigest.digest();
        StringBuilder stringBuilder = new StringBuilder();
        for(byte b : resultByteArray){
            stringBuilder.append(String.format("%02x",b));
        }
        USERNAME_INPUT = stringBuilder.toString();
        messageDigest.update(PASSWORD_INPUT.getBytes());
        byte[] resultByteArray2 = messageDigest.digest();
        StringBuilder stringBuilder2 = new StringBuilder();
        for(byte b : resultByteArray2){
            stringBuilder2.append(String.format("%02x",b));
        }
        PASSWORD_INPUT = stringBuilder2.toString();
        FileReader fileReader = new FileReader("src/main/resources/UserProfile.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String string;
        while ((string = bufferedReader.readLine()) != null) {
            USERNAME_CHECK = string.split(" :: ")[0];
            PASSWORD_CHECK = string.split(" :: ")[1];
            SECURITY_QUESTION = string.split(" :: ")[2];
            SECURITY_ANSWER_CHECK =  string.split(" :: ")[3];
        }
        if(USERNAME_INPUT.equals(USERNAME_CHECK ) && PASSWORD_INPUT.equals(PASSWORD_CHECK)){
            System.out.println("Security Question:   " + SECURITY_QUESTION);
            System.out.println("Answer:  ");
            SECURITY_ANSWER_INPUT = scanner.nextLine();
            if(SECURITY_ANSWER_INPUT.equals(SECURITY_ANSWER_CHECK)){
                userSessionUtils.setUserSession(userName, Instant.now());
                chooseOption();
            }
            else{
                System.out.println("Wrong Answer");
                login();
            }
        }
        else{
            System.out.println("Wrong Username or Password");
            login();
        }
    }

    public void chooseOption(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n1.Write Queries\n2.Export\n3.Data Model\n4.Analytics");
        int option = scanner.nextInt();
        scanner.nextLine();
        switch (option){
            case 1:
                System.out.println("Enter your query");
                String query = scanner.nextLine();
                try {
                    Map<String, Object> result = this.processor.submitQuery(query);
                    if ((Boolean) result.get("executed")) {
                        if ((String) result.get("queryType") == "SELECT_FROM") {
                            List<TreeMap<String, String>> rows = (List<TreeMap<String, String>>) result.get(
                                    "rows");
                            System.out.println("--------------------------- ROWS ---------------------------");
                            for (TreeMap<String, String> row : rows) {
                                System.out.println(row);
                            }
                        } else if ((String) result.get("queryType") == "INSERT_INTO") { // || (String) result.get(
                            // "queryType") == "UPDATE"
                            System.out.println("Query executed successfully");
                            System.out.println("Number of rows inserted: " + (String) result.get("successCount"));
                            System.out.println("Number of rows not inserted: " + (String) result.get("failureCount"));
                        } else {
                            System.out.println("Query executed successfully");
                        }
                    }
                } catch (InvalidSQLQueryException error) {
                    System.out.println(error.getMessage());
                } catch (Exception error) {
                    System.out.println(error.getMessage());
                }
                chooseOption();
                break;
            case 2:
                String databaseName;
                System.out.println("Enter database name");
                databaseName = scanner.nextLine();
                IGenerateSQLDump sqlDump = new GenerateSQLDump(databaseName);
                try {
                    sqlDump.generateSQLDump();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                String databaseName2;
                System.out.println("Enter database name");
                databaseName2 = scanner.nextLine();
                IGenerateERD erd = new GenerateERD(databaseName2);
                try {
                    erd.generateERD();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                System.out.println("1.Show analytics by Database");
                System.out.println("2.Show analytics by Table and operation");
                int subOption = scanner.nextInt();
                scanner.nextLine();
                switch (subOption){
                    case 1:
                        System.out.println("Enter query");
                        query = scanner.nextLine();
                        DatabaseAnalyticsImpl databaseAnalytics = new DatabaseAnalyticsImpl();
                        databaseAnalytics.getInstance().getAnalytics(query);
                        break;
                    case 2:
                        System.out.println("Enter query");
                        String query2 = scanner.nextLine();
                        TableAnalyticsImpl tableAnalytics = new TableAnalyticsImpl();
                        tableAnalytics.getInstance().getAnalytics(query2);
                }
                chooseOption();
                break;
        }
    }


    public void registration() throws IOException, NoSuchAlgorithmException {
        FileWriter myWriter = new FileWriter("src/main/resources/UserProfile.txt",true);
        Scanner scanner = new Scanner(System.in);
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        String USERNAME;
        String PASSWORD;
        String SECURITY_QUESTION;
        String SECURITY_ANSWER;
        System.out.println("Welcome!");
        System.out.println("Enter Username:   ");
        USERNAME = scanner.nextLine();
        System.out.println("Enter Password:   ");
        PASSWORD = scanner.nextLine();
        System.out.println("Enter Security Question:   ");
        SECURITY_QUESTION = scanner.nextLine();
        System.out.println("Enter Security Answer:   ");
        SECURITY_ANSWER = scanner.nextLine();
        messageDigest.update(USERNAME.getBytes());
        byte[] resultByteArray = messageDigest.digest();
        StringBuilder stringBuilder = new StringBuilder();
        for(byte b : resultByteArray){
            stringBuilder.append(String.format("%02x",b));
        }
        USERNAME = stringBuilder.toString();
        messageDigest.update(PASSWORD.getBytes());
        byte[] resultByteArray2 = messageDigest.digest();
        StringBuilder stringBuilder2 = new StringBuilder();
        for(byte b : resultByteArray2){
            stringBuilder2.append(String.format("%02x",b));
        }
        PASSWORD = stringBuilder2.toString();
        myWriter.append(USERNAME + " :: " + PASSWORD + " :: " + SECURITY_QUESTION + " :: " + SECURITY_ANSWER + "\n");
        myWriter.close();
        System.out.println("\nRegistration Successful! \n");
        welcomeScreen();
    }
}
