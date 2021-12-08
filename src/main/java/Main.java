import UserInterface_LoginSecurity.UserInterface;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Main {

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		UserInterface userInterface = new UserInterface();
		userInterface.welcomeScreen();
	}

}
