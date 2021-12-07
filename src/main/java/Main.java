import UserInterface_LoginSecurity.UserInterface;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Main {

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		UserInterface userInterface = new UserInterface();
		userInterface.welcomeScreen();
	}

}
