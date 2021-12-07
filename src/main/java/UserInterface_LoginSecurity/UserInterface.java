package UserInterface_LoginSecurity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class UserInterface {

    String userName="";

    public void welcomeScreen() throws NoSuchAlgorithmException, IOException {
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
            userName = USERNAME_INPUT;
            System.out.println("Security Question:   " + SECURITY_QUESTION);
            System.out.println("Answer:  ");
            SECURITY_ANSWER_INPUT = scanner.nextLine();
            if(SECURITY_ANSWER_INPUT.equals(SECURITY_ANSWER_CHECK)){
                System.out.println("1.Write Queries\n2.Export\n3.Data Model\n4.Analytics");
                int option = scanner.nextInt();
                chooseOption(option);
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

    public void chooseOption(int option){

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
        myWriter.write(USERNAME + " :: " + PASSWORD + " :: " + SECURITY_QUESTION + " :: " + SECURITY_ANSWER );
        myWriter.close();
    }
}
