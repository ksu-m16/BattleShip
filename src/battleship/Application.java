package battleship;

import kship.KGameStategy;
import kship.KSetupStrategy;
import model.IFieldModel;

public class Application {

	private GameController gc;
	private SetupController sc;
	private ISetupStrategy ss1;
	private ISetupStrategy ss2;
	private IFieldModel f1;
	private IFieldModel f2;
	private IStrategy str1;
	private IStrategy str2;
	
	
	private int ngames = 100;

	private int firstPlayerWins = 0;
	private int winner;

	public void startChampionship() {
		
		long tm = System.currentTimeMillis();
		
		for (int i = 0; i < ngames; i++) {
			init();
			play();
		}
		System.out.println(ngames + " games played.");
		System.out.println("Player 1 wins " + firstPlayerWins + " times.");
		System.out.println("Player 2 wins " + (ngames - firstPlayerWins)
				+ " times.");

		if (firstPlayerWins == (ngames - firstPlayerWins)) {
			System.out.println("Friendship wins!");
		}

		else {
			winner = (firstPlayerWins > ngames / 2) ? 0 : 1;
			System.out.println("Player " + (winner + 1) + " wins championship");
		}
		
		System.out.println(System.currentTimeMillis() - tm + " ms");
		
	}

	public void init() {
		sc = new SetupController();
		gc = new GameController();

		// setup fields
//		ss1 = new SetupStrategy();
//		ss2 = new KSetupStrategy();
		
		ss2 = new SetupStrategy();
		ss1 = new KSetupStrategy();
		
		sc.setSetupStrategy(ss1);
		f1 = sc.setup();
		sc.setSetupStrategy(ss2);
		f2 = sc.setup();

		// setup gamecontroller

		str1 = new KGameStategy(1., 2., 3.);
		str2 = new XuStrategy4();

		gc.setPlayer1Strategy(str1);
		gc.setPlayer2Strategy(str2);
		gc.setPlayer1Model(f1);
		gc.setPlayer2Model(f2);
	}

	public void play() {
		
		if (f1 == null) {
			System.out.println("Bad setupStrategy, player 1. Player 2 wins.");
		}
		
		if (f2 == null) {
			System.out.println("Bad setupStrategy, player 2. Player 1 wins.");
		}
		
		if (f1 == null || f2 == null) {
			return;
		}
		
		
		while (gc.next()) {
		}

		if (gc.getWinner() == 0) {
			firstPlayerWins++;
		}

		// System.out.println();
		// System.out.println("player 1 field");
		// f1.printField();
		// System.out.println();
		// System.out.println("player 2 field");
		// f2.printField();
	}
}
