package battleship;

import model.FieldModel;
import model.IFieldModel;

public class Application {
	public void init() {
		SetupController sc = new SetupController();
		GameController gc = new GameController();
		IFieldModel f = new FieldModel();
		SetupStrategy ss = new SetupStrategy(f);
		Strategy str1 = new Strategy();
		Strategy str2 = new Strategy();
		sc.setField(f);
		sc.setSetupStrategy(ss);
		sc.getShips();
		
	}
}
