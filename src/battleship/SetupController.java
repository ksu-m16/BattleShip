package battleship;

import java.util.List;

import model.FieldModel;
import model.IFieldModel;
import model.IPoint;
import model.IShipDescription;
import model.State;

public class SetupController {

	private ISetupStrategy ss;

	public IFieldModel setup() {
		
		List<IShipDescription> ships = ss.getShips();
		
		if (! checkSetup(ships)) {
			System.out.println("YOU SHELL NOT PASS, CHEATER!!!");
			return null; 
		}
		
		return FieldModel.getFieldModel(ships);
	}

	public void setSetupStrategy(ISetupStrategy ss) {
		this.ss = ss;
	}

	public ISetupStrategy getSetupStrategy() {
		return ss;
	}
	
	private boolean checkSetup(List<IShipDescription> ships) {
		
		State[][] field = new State[GameDescription.XMAX][GameDescription.YMAX];
		for (int i = 0; i < GameDescription.XMAX; i++) {
			for (int j = 0; j < GameDescription.YMAX; j++) {
				field[i][j] = State.EMPTY;
			}
		}	
		
		
		for (IShipDescription s: ships) {
			if (!SetupHelper.checkShipPlacement(s, field)) {
				return false;
			}
			placeShip(s, field);
			makePerimeter(s, field);
			
		}

		return true;
	}
	
	private void placeShip(IShipDescription s, State[][] field) {
		List<IPoint> position = s.getPosition();
		for (IPoint p : position) {
			field[p.getX()][p.getY()] = State.SHIP;
		}
	}

	private void makePerimeter(IShipDescription s, State[][] field) {
		List<IPoint> position = s.getPosition();
		for (IPoint p : position) {
			makePerimeter(p, field);
		}
	}

	private void makePerimeter(IPoint p, State[][] field) {
		int x = p.getX();
		int y = p.getY();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (getAt(x - 1 + i, y - 1 + j, field) == State.EMPTY) {
					setAt(x - 1 + i, y - 1 + j, State.FORBIDDEN, field);
				}
			}
		}
		
	}
	
	private State getAt(int x, int y, State[][] field) {
		if (x < 0 || y < 0 || x >= GameDescription.XMAX
				|| y >= GameDescription.YMAX) {
			return State.EMPTY;
		}
		return field[x][y];
	}

	private void setAt(int x, int y, State st, State[][] field) {
		if (x < 0 || y < 0 || x >= GameDescription.XMAX
				|| y >= GameDescription.YMAX) {
			return;
		}
		field[x][y] = st;

	}

}
