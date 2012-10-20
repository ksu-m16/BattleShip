package battleship;

import java.util.ArrayList;
import java.util.List;

import model.ClassicFleet;
import model.IField;
import model.Ship;

public class SetupController {

	private IField f;
	private ISetupStrategy ss;

	public void setField(IField f) {
		this.f = f;
	}

	public void setSetupStrategy(ISetupStrategy s) {
		this.ss = s;
	}

	public List<Ship> getListOfShips() {
		ss = new SetupStrategy(f);
		ArrayList<Integer> shipsDescription = new ClassicFleet()
				.getShipsDescription();
		return ss.getListOfShips(shipsDescription);
	}
}
