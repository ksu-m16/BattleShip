package model;

import java.util.ArrayList;

public class ClassicFleet implements IFleet {

	private int maxsize = 4;
	private int numberOfShips = 10;

	@Override
	public ArrayList<Integer> getShipsDescription() {
		ArrayList<Integer> fleet = new ArrayList<Integer>();
		int nships = 0;
		for (int i = 0; i < numberOfShips; i++) {
			fleet.add(maxsize);
			nships++;
			if ((nships + maxsize) == 5) {
				maxsize--;
				nships = 0;
			}
		}
		return fleet;
	}
}
