package file_parsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileParsingForClient implements FileParsingForClientInterface {
	
	public void addUserInfo (String userName, String password) {
		File userFile = new File("File/User/user.txt");
		try {
			userFile.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(userFile, true));
			out.write(userName + ":" + password + "\r\n");
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Boolean checkIsUserExist (String username) {
		Map<String, String> userMap = new HashMap<String, String>();
		userMap = transToKeyValueUser();
		return userMap.containsKey(username);
	}
	
	public Boolean checkIsPasswordCorrect (String username, String passWord) {
		Map<String, String> userMap = new HashMap<String, String>();
		userMap = transToKeyValueUser();
		String rightPassword = userMap.get(username);
		return rightPassword.equals(passWord);
	}

	public Map<String, String> transToKeyValueUser () {
		Map<String, String> userMap = new HashMap<String, String>();
		File userFile = new File("File/User/user.txt");
		try {
			userFile.createNewFile();
			BufferedReader in = new BufferedReader(new FileReader(userFile));
			String string;
			while ((string = in.readLine()) != null) {
				String key = string.split(":")[0];
				String value = string.split(":")[1];
				userMap.put(key, value);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userMap;
	}
	
}
