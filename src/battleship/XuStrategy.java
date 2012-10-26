package battleship;

import java.util.ArrayList;
import java.util.List;

import model.FieldModel;
import model.IPoint;
import model.IShipDescription;
import model.State;

public class XuStrategy implements IStrategy {
	private State[][] state;
	private ArrayList<IPoint> currentVictim;
	
//	private enum EnemyState {
//		MISS, HIT, FORBIDDEN;
//	}
	
	@Override
	public IPoint move() {		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(IPoint point, State state) {

		
	}
	
	private void surroundCorpse(ArrayList<IPoint> corpse){
		for (IPoint p : corpse) {
			makePerimeter(p);
		}
	}

	private void makePerimeter(IPoint p) {
		int x = p.getX();
		int y = p.getY();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (getAt(x - 1 + i, y - 1 + j) == State.EMPTY) {
					setAt(x - 1 + i, y - 1 + j, State.FORBIDDEN);
				}
			}
		}
	}
	private State getAt(int x, int y) {
		if (x < 0 || y < 0 || x >= GameDescription.XMAX || y >= GameDescription.YMAX) {
			return State.EMPTY;
		}
		return state[x][y];
	}
	
	private void setAt(int x, int y, State st) {
		if (x < 0 || y < 0 || x >= GameDescription.XMAX || y >= GameDescription.YMAX) {
			return;
		}
		state[x][y] = st;
		
	}
	
}
