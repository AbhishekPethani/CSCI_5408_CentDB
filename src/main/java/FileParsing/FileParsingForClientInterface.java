package FileParsing;

public interface FileParsingForClientInterface {
	
	//File parsing for Client Section
	public void addUserInfo (String userName, String password);
	
	public Boolean checkIsUserExist (String username);
	
	public Boolean checkIsPasswordCorrect (String username, String passWord);
	
}
