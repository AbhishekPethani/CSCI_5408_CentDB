package file_parsing;

public interface FileParsingInterface {
	
	//File parsing for Client Section
	public void addUserInfo (String userName, String password);
	
	public Boolean checkIsUserExist (String username);
	
	public Boolean checkIsPasswordCorrect (String username, String passWord);
	
	//File parsing for query Section
	
	
}
