package battleship;

public class GameController {
	private Strategy str1, str2;
	private boolean gameOver = false;
	
	public void setStrategy1(Strategy str) {
		str1 = str;
	}
	public void setStrategy2(Strategy str) {
		str2 = str;
	}

	
	public boolean next(){
		if (! gameOver) {
			str1.move();
		}
		if (! gameOver) {
			str2.move();
		}
		return !gameOver;
	}
}
