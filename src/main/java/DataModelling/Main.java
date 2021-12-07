package DataModelling;

public class Main {

	public static void main(String[] args) {
		IGenerateERD erd = new GenerateERD("Demo");
		try {
			erd.generateERD();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
