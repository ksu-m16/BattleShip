package battleship;

import model.FieldModel;
import model.IFieldModel;

public class Application {
	public void init() {
		SetupController sc = new SetupController();
		GameController gc = new GameController();

		SetupStrategy ss1 = new SetupStrategy();
		SetupStrategy ss2 = new SetupStrategy();
		XuStrategy str1 = new XuStrategy();
		XuStrategy str2 = new XuStrategy();
		sc.setSetupStrategy(ss1);
		IFieldModel f1 = sc.setup();
		sc.setSetupStrategy(ss2);
		IFieldModel f2 = sc.setup();

	}
}
