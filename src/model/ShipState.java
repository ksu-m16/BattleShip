package model;

import java.util.List;

public class ShipState implements IShipState{

	private List<IPoint> position;
	
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isKilled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getLiveStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

}
