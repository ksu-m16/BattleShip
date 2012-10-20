package battleship;

import model.Field;
import model.IField;

public class Application {
	public void init() {
		SetupController sc = new SetupController();
		GameController gc = new GameController();
		IField f = new Field();
		SetupStrategy ss = new SetupStrategy(f);
		Strategy str1 = new Strategy();
		Strategy str2 = new Strategy();
		sc.setField(f);
		sc.setSetupStrategy(ss);
		sc.getListOfShips();
		
	}
}
