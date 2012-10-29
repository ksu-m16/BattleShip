package battleship;

import model.FieldModel;
import model.IFieldModel;

public class Application {
	public void init() {
		SetupController sc = new SetupController();
		GameController gc = new GameController();

		//setup fields
		SetupStrategy ss1 = new SetupStrategy();
		SetupStrategy ss2 = new SetupStrategy();
		sc.setSetupStrategy(ss1);
		IFieldModel f1 = sc.setup();
		sc.setSetupStrategy(ss2);
		IFieldModel f2 = sc.setup();
		
		//setup gamecontroller
		XuStrategy str1 = new XuStrategy();
		XuStrategy str2 = new XuStrategy();
		gc.setPlayer1Strategy(str1);
		gc.setPlayer2Strategy(str2);
		gc.setPlayer1Model(f1);
		gc.setPlayer2Model(f2);
		while (gc.next()){}
		System.out.println("player1 field");
		f1.printField();
		System.out.println("player2 field");
		f2.printField();
	}
}
