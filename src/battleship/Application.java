package battleship;

import model.FieldModel;
import model.IFieldModel;

public class Application {
	public void init() {
		SetupController sc = new SetupController();
		GameController gc = new GameController();

		SetupStrategy ss = new SetupStrategy();
		Strategy str1 = new Strategy();
		Strategy str2 = new Strategy();
		sc.setSetupStrategy1(ss);
		sc.setSetupStrategy2(ss);
		IFieldModel f1 = FieldModel.getFieldModel(sc.getSetupStrategy1().getShips());
		IFieldModel f2 = FieldModel.getFieldModel(sc.getSetupStrategy2().getShips());

	}
}
