package model;

import java.util.List;

import battleship.GameDescription;
import battleship.ISetupStrategy;
import battleship.SetupHelper;

public class FieldModel implements IFieldModel {
	private FieldModel() {
//		init();
	}

	public static IFieldModel getFieldModel(List<IShipDescription> ships){
		FieldModel m = new FieldModel();
		m.init(ships);
		return (IFieldModel)m;
	}
	
//	public FieldModel() {
//		// fleet = new ArrayList<Ship>();
//		states = new State[GameDescription.XMAX][GameDescription.YMAX];
//		for (int i = 0; i < states[0].length; i++) {
//			for (int j = 0; j < states[0].length; j++) {
//				states[i][j] = State.EMPTY;
//			}
//		}
//
//	}

	private List<IShipState> shipStates;



	private State[][] states;

	// private ArrayList <Ship> fleet;
	//
	// public ArrayList <Ship> getFleet() {
	// return fleet;
	// }

	public State getState(int x, int y) {
		if (x < 0 || y < 0 || x >= GameDescription.XMAX || y >= GameDescription.YMAX) {
			return State.EMPTY;
		}
		return states[x][y];
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
		if (x < 0 || y < 0 || x >= GameDescription.XMAX || y >= GameDescription.YMAX) {
			return State.EMPTY;
		}
		if (getState(x, y) == State.SHIP) {
			states[x][y] = State.HIT;
			return State.HIT;
		}
		states[x][y] = State.MISS;
		return State.MISS;
	}

	@Override
	public List<IShipState> getShipStates() {
		// TODO Auto-generated method stub
		return shipStates;
	}

	private void init(List<IShipDescription> ships) throws IllegalArgumentException {
		states = new State[GameDescription.XMAX][GameDescription.YMAX];
		for (int i = 0; i < states[0].length; i++) {
			for (int j = 0; j < states[0].length; j++) {
				states[i][j] = State.EMPTY;
			}
		}
		for (IShipDescription s: ships) {
			if (!SetupHelper.checkShipPlacement(s, states)) {
				throw new IllegalArgumentException("Illegal ship position!");
			}
			
		}
	}
	
			
}
