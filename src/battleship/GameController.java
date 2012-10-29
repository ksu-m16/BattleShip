package battleship;

import model.IPoint;

public class GameController {
//	private XuStrategy str1, str2;
	private IStrategy[] st = new IStrategy[]{new XuStrategy() , new XuStrategy()};
	
	private boolean gameOver = false;
	int current = 0;
	
//	public void setStrategy1(XuStrategy str) {
//		str1 = str;
//	}
//
//	public void setStrategy2(XuStrategy str) {
//		str2 = str;
//	}
	
	public boolean next() {
//		if (!gameOver) {
//			str1.move();
//		}
//		if (!gameOver) {
//			str2.move();
//		}
//		return !gameOver;

		 IStrategy s = st[current];
		 IPoint p = s.move();
		 
//		 s.update(p, );
		 
		 current = (current + 1) % 2;
		 return !gameOver;

	}
}
