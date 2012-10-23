package model;

import java.util.List;

public class FieldModel implements IFieldModel {

	public FieldModel() {
		// fleet = new ArrayList<Ship>();
		states = new State[XSIZE][YSIZE];
		for (int i = 0; i < states[0].length; i++) {
			for (int j = 0; j < states[0].length; j++) {
				states[i][j] = State.EMPTY;
			}
		}

	}



	public final static int XSIZE = 10;
	public final static int YSIZE = 10;

	private State[][] states;

	// private ArrayList <Ship> fleet;
	//
	// public ArrayList <Ship> getFleet() {
	// return fleet;
	// }

	public State getState(int x, int y) {
		if (x < 0 || y < 0 || x >= XSIZE || y >= YSIZE) {
			return State.EMPTY;
		}
		return states[x][y];
	}

	public void setState(int x, int y, State st) {
		if ((x >= 0 && x < XSIZE) && (y >= 0 && y < YSIZE)) {
			states[x][y] = st;
		}
	}

	public void printField() {
		for (int j = 0; j < states.length; j++) {
			for (int i = 0; i < states.length; i++) {
				System.out.print(states[i][j]);
			}
			System.out.println();
		}
	}

	@Override
	public State attack(int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IShipState> getShipStates() {
		// TODO Auto-generated method stub
		return null;
	}
}
