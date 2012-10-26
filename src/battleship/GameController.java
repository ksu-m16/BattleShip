package battleship;

public class GameController {
	private XuStrategy str1, str2;
	private boolean gameOver = false;

	public void setStrategy1(XuStrategy str) {
		str1 = str;
	}

	public void setStrategy2(XuStrategy str) {
		str2 = str;
	}

	public boolean next() {
		if (!gameOver) {
			str1.move();
		}
		if (!gameOver) {
			str2.move();
		}
		return !gameOver;

		// int current = 0;
		// Strategy st = new Strategy[2];
		// public boolean next(){
		// Strategy s = st[current];
		// Move m = s.move();
		// //Update model here
		// s.update(//some result);
		// current = (current + 1) % 2;
		// return !gameOver;

	}
}
