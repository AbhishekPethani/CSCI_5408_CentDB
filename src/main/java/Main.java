import ExportData.GenerateSQLDump;
import ExportData.IGenerateSQLDump;

public class Main {

	public static void main(String[] args) {
		IGenerateSQLDump sqlDump = new GenerateSQLDump("Demo");
		try {
			sqlDump.generateSQLDump();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
