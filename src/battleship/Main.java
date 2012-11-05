package battleship;

import kship.KSetupStrategy;
import kship.KTimer;

public class Main {

	public static final int roundsCount = 10;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Application app = new Application();
		// app.init();
		

		for (int i = 0; i < roundsCount; ++i) {
			app.startChampionship();
		}
		
		KSetupStrategy.print();
		
	}

}
