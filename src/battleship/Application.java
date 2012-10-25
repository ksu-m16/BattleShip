package battleship;

import model.FieldModel;
import model.IFieldModel;

public class Application {
	public void init() {
		SetupController sc = new SetupController();
		GameController gc = new GameController();

		SetupStrategy ss1 = new SetupStrategy();
		SetupStrategy ss2 = new SetupStrategy();
		Strategy str1 = new Strategy();
		Strategy str2 = new Strategy();
		sc.setSetupStrategy(ss1);
		IFieldModel f1 = sc.setup();
		sc.setSetupStrategy(ss2);
		IFieldModel f2 = sc.setup();

	}
}
