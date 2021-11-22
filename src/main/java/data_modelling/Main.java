package data_modelling;

public class Main {

	public static void main(String[] args) {
		GenerateERD erd = new GenerateERD("Demo");
		try {
			erd.generateERD();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
