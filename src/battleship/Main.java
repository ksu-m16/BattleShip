package battleship;

public class Main {

	public static final int roundsCount = 1;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Application app = new Application();
		// app.init();
		

		for (int i = 0; i < roundsCount; ++i) {
			app.startChampionship();
		}				
		
	}

}
