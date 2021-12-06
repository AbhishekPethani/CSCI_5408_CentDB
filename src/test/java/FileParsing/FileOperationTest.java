package FileParsing;

import static org.junit.Assert.*;

import org.junit.Test;

public class FileOperationTest {

	@Test
	public void getDatabaseInfoTest() {
		FileOperation fileOperation = new FileOperation();
		fileOperation.getDatabaseInfo("student");
	}

}
