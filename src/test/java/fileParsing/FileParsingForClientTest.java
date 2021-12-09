package FileParsing;

import org.junit.Test;

/**
 * @author zhaoling
 */
public class FileParsingForClientTest {

//	@Test
//	public void checkIsPasswordCorrectTest() {
//		FileParsingForClient fileParsing = new FileParsingForClient();
//		Assert.assertTrue(fileParsing.checkIsPasswordCorrect("zhaoling", "beibeiQQ159"));
//	}
//	@Test
//	public void checkIsUserExistTest() {
//		FileParsingForClient fileParsing = new FileParsingForClient();
//		Assert.assertTrue(fileParsing.checkIsUserExist("zhaoling"));
//	}
	@Test
	public void addUserInfoTest () {
		FileParsingForClient fileParsing = new FileParsingForClient();
		fileParsing.addUserInfo("Liya", "123456789");
	}

}
