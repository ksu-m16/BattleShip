package model;

import java.util.Arrays;
import java.util.List;

public class ShipState implements IShipState {
	public ShipState (IShipDescription sd) {
		this.sd = sd;
		state = new State[sd.getSize()];
		Arrays.fill(state, State.SHIP);
	}
	
	private IShipDescription sd;
	private State[] state;

	@Override
	public int getSize() {
		return state.length;
	}

	@Override
	public boolean isKilled() {
		boolean dead = true;
		for (State st : state) {
			dead = dead && (st == State.HIT);
		}
		return dead;
	}

	@Override
	public boolean isHit() {
		for (State st : state) {
			if (st == State.HIT) {
				return true;
			}
		}
		return false;
	}

	@Override
	public double getLiveStatus() {
		int nhit = 0;
		for (State st : state) {
			if (st == State.HIT) {
				return nhit++;
			}
		}
		return ((state.length - nhit) * 100 / state.length);
	}
	
	
	public boolean attack(int x, int y) {
		List<IPoint> position = sd.getPosition();

		for (int i = 0; i < position.size(); i++) {
			if ((position.get(i).getX() == x) && (position.get(i).getY() == y)) {
				state[i] = State.HIT;
				return true;
			}
		}
		return false;
		
	}

}
