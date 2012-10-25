package model;

import java.util.List;

public class ShipState implements IShipState {

	private IShipDescription sd;
	private List<State> state;

	@Override
	public int getSize() {
		return state.size();
	}

	@Override
	public boolean isKilled() {
		boolean dead = true;
		for (State st : state) {
			dead = dead && (st == State.MISS);

		}
		return dead;
	}

	@Override
	public boolean isHit() {
		for (State st : state) {
			if (st == State.MISS) {
				return true;
			}
		}
		return false;
	}

	@Override
	public double getLiveStatus() {
		int nhit = 0;
		for (State st : state) {
			if (st == State.MISS) {
				return nhit++;
			}
		}
		return ((state.size() - nhit) * 100 / state.size());
	}
	
	public boolean attack(int x, int y) {
		List<IPoint> position = sd.getPosition();
//		for (IPoint p: position) {
		for (int i = 0; i < position.size(); i++) {
			if ((position.get(i).getX() == x) && (position.get(i).getY() == y)) {
				state.set(i, State.HIT);
				return true;
			}
		}
		return false;
		
	}

}
