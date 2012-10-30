package model;

import java.util.ArrayList;
import java.util.Arrays;
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
//		m.printField();
		return m;
	}
	
	private ArrayList<ShipState> shipStates;

	private State[][] states;

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
		if (states[x][y].getHitState() == State.HIT) {
			for (ShipState s: shipStates) {
				if (! s.attack(x, y)){
				continue;	
				}
				System.out.println("model: hit" + s.getSize());
				if (s.isKilled()) {
					System.out.println("model: killed");
					states[x][y] = states[x][y].getHitState();
					return State.KILLED;
				}
				break;
			}

		}
	
		return states[x][y] = states[x][y].getHitState();
	}

	@Override
	public List<? extends IShipState> getShipStates() {
//	public List<IShipState> getShipStates() {
		return shipStates;
//		return new ArrayList<IShipState>(shipStates);
	}

	private void init(List<IShipDescription> ships) throws IllegalArgumentException {
		states = new State[GameDescription.XMAX][GameDescription.YMAX];
		shipStates = new ArrayList<ShipState>();
		
		for (int i = 0; i < states[0].length; i++) {
			Arrays.fill(states[i], State.EMPTY);
		}
		
		for (IShipDescription s: ships) {
			if (!SetupHelper.checkShipPlacement(s, states)) {
				throw new IllegalArgumentException("Illegal ship position!");
			}
			placeShip(s);
			shipStates.add(new ShipState(s));
		}
	}
	
	private void placeShip(IShipDescription s) {
		List<IPoint> position = s.getPosition();
		for (IPoint p : position) {
			states[p.getX()][p.getY()] = State.SHIP;
		}
	}
	
			
}
