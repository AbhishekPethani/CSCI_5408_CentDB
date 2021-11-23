package fileParsing;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import file_parsing.FileParsing;

public class FileParsingTest {

	@Test
	public void checkIsPasswordCorrectTest() {
		FileParsing fileParsing = new FileParsing();
		Assert.assertTrue(fileParsing.checkIsPasswordCorrect("zhaoling", "beibeiQQ159"));
	}
	@Test
	public void checkIsUserExistTest() {
		FileParsing fileParsing = new FileParsing();
		Assert.assertTrue(fileParsing.checkIsUserExist("zhaoling"));
	}

}
