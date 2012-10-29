package battleship;

import model.IFieldModel;
import model.IPoint;
import model.IShipState;
import model.State;

public class GameController {

	private IStrategy[] strategies = new IStrategy[2];
	private IFieldModel[] models = new IFieldModel[2];
	
//	private boolean gameOver = false;
	
	int current = 0;
	
	public void setPlayer1Strategy(IStrategy str) {
		strategies[0] = str;
	}

	public void setPlayer2Strategy(IStrategy str) {
		strategies[1] = str;
	}
	
	public void setPlayer1Model(IFieldModel m) {
		models[0] = m;
	}

	public void setPlayer2Model(IFieldModel m) {
		models[1] = m;
	}
	
	public boolean next() {

		IStrategy s = strategies[current];
		 IPoint p = s.move();
		 System.out.println("Player " + ((current + 1) % 2) + " move " + p);
		 State result = models[(current + 1) % 2].attack(p.getX(), p.getY());
		 s.update(p, result);
	 
		 current = (current + 1) % 2;
		 return !isGameOver();

	}
	
	private boolean isGameOver() {
		boolean everybodeKilled = true;
		 for (IShipState s: (models[(current + 1) % 2].getShipStates())) {
				 everybodeKilled = everybodeKilled && s.isKilled();
		 }
		return everybodeKilled;
	}
	
}
