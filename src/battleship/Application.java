package battleship;

import kship.KGameStategy;
import kship.KHelper;
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
	
	
	private int ngames = 1000;

	private int firstPlayerWins = 0;
	private int winner;
	
	private int player1Millis = 0;
	public int getPlayer1Millis() {
		return player1Millis;
	}
	private int player2Millis = 0;
	public int getPlayer2Millis() {
		return player2Millis;
	}

	public void startChampionship() {
		
		long tm = System.currentTimeMillis();
		
		player1Millis = 0;
		player2Millis = 0;
		firstPlayerWins = 0;
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
		System.out.println("player1 took: " + player1Millis + " ms, player2 took: " + player2Millis);	
	}
	
	public void init() {
		sc = new SetupController();
		gc = new GameController();

		// setup fields		
		ss1 = new KSetupStrategy();
		ss2 = new XuSetupStrategy2();
		
		sc.setSetupStrategy(ss1);
		
		long time = System.currentTimeMillis();
		f1 = sc.setup();
		player1Millis += System.currentTimeMillis() - time;		
		
		sc.setSetupStrategy(ss2);
		
		time = System.currentTimeMillis();		
		f2 = sc.setup();
		player2Millis += System.currentTimeMillis() - time;

		// setup game controller
		str1 = new KGameStategy(12., 8.);
		str2 = new XuStrategy5();


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
			
		boolean res = true;
		while (res) {			
			int player = gc.getCurrentPlayer();
			res = gc.next(); 
			long millis = gc.getLastMillis();
			
			if (player == 0) {
				player1Millis += millis;
			} else {
				player2Millis += millis;
			}
		}

		if (gc.getWinner() == 0) {
			firstPlayerWins++;
		}

//		 System.out.println();
//		 System.out.println("player 1 field");
//		 f1.printField();
//		 System.out.println();
//		 System.out.println("player 2 field");
//		 f2.printField();
	}
}
