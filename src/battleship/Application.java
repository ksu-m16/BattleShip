package battleship;

import model.IFieldModel;

public class Application {
	
	private GameController gc;
	private SetupController sc;
	private SetupStrategy ss1;
	private SetupStrategy ss2;
	private IFieldModel f1;
	private IFieldModel f2;
	private IStrategy str1;
	private IStrategy str2;
	
	
	private int ngames = 1000;
	private int firstPlayerWins = 0;
	private int winner;
	
	public void startChampionship() {
		for (int i = 0; i < ngames; i++) {
			init();
			play();
		}
		System.out.println(ngames + " games played.");
		System.out.println("Player 1 wins " + firstPlayerWins + " times.");
		System.out.println("Player 2 wins " + (ngames - firstPlayerWins) + " times.");
		
		if (firstPlayerWins == (ngames - firstPlayerWins)) {
			System.out.println("Friendship wins!");
		}
		winner = (firstPlayerWins > ngames / 2) ? 0 : 1;
		
		System.out.println("Player " + (winner + 1) + " wins championship");
	}
	
	public void init() {
		sc = new SetupController();
		gc = new GameController();

		// setup fields
		ss1 = new SetupStrategy();
		ss2 = new SetupStrategy();
		sc.setSetupStrategy(ss1);
		f1 = sc.setup();
		sc.setSetupStrategy(ss2);
		f2 = sc.setup();

		// setup gamecontroller
		str1 = new XuStrategy();
		str2 = new XuStrategy();
		gc.setPlayer1Strategy(str1);
		gc.setPlayer2Strategy(str2);
		gc.setPlayer1Model(f1);
		gc.setPlayer2Model(f2);
	}
	
	public void play() {
		while (gc.next()) {
		}
		
		if (gc.getWinner() == 0) {
			firstPlayerWins++;
		}
		
//		System.out.println();
//		System.out.println("player 1 field");
//		f1.printField();
//		System.out.println();
//		System.out.println("player 2 field");
//		f2.printField();
	}
}
