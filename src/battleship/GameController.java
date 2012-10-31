package battleship;

import model.IFieldModel;
import model.IPoint;
import model.IShipState;
import model.State;

public class GameController {

	private IStrategy[] strategies = new IStrategy[2];
	private IFieldModel[] models = new IFieldModel[2];
	
	private int winner = 0;
	
	int current = (int)Math.round(Math.random());

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
//		System.out.println("Player " + (current + 1) + " move " + p);
		State result = models[(current + 1) % 2].attack(p.getX(), p.getY());
		s.update(p, result);

		if (result == State.MISS) {
			current = (current + 1) % 2;
		}
		return !isGameOver();

	}

	private boolean isGameOver() {

		boolean everybodeKilled = true;
		for (IShipState s : (models[((current + 1) % 2)].getShipStates())) {
			everybodeKilled = everybodeKilled && s.isKilled();
		}
		if (everybodeKilled) {
			winner = current;
//			System.out.println("Player " + (current + 1)  + " win!");
		}
		return everybodeKilled;
	}

	public int getWinner() {
		return winner;
	}
	
}
