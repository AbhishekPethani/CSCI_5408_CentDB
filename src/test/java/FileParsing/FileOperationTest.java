package FileParsing;

import static org.junit.Assert.*;

import org.junit.Test;

public class FileOperationTest {

//	@Test
//	public void getDatabaseInfoTest() {
//		FileOperation fileOperation = new FileOperation();
//		fileOperation.getDatabaseInfo("student");
//	}
	
//	@Test
//	public void getDatabaseNamesTest() {
//		FileOperation fileOperation = new FileOperation();
//		fileOperation.getDatabaseNames();
//	}
	
//	@Test
//	public void createMetaDataTest() {
//		FileOperation fileOperation = new FileOperation();
//		fileOperation.createMetaData();
//	}
	
	@Test
	public void getColumnInfoTest() {
		FileOperation fileOperation = new FileOperation();
		fileOperation.getColumnInfo("professorInfo");
	}

}
