package fileParsing;

import static org.junit.Assert.*;
import org.junit.Assert;
import org.junit.Test;
import file_parsing.FileParsingForClient;

public class FileParsingForClientTest {

	@Test
	public void checkIsPasswordCorrectTest() {
		FileParsingForClient fileParsing = new FileParsingForClient();
		Assert.assertTrue(fileParsing.checkIsPasswordCorrect("zhaoling", "beibeiQQ159"));
	}
	@Test
	public void checkIsUserExistTest() {
		FileParsingForClient fileParsing = new FileParsingForClient();
		Assert.assertTrue(fileParsing.checkIsUserExist("zhaoling"));
	}

}
