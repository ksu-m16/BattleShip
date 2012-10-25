package battleship;

import java.util.ArrayList;
import java.util.List;

import model.IShipDescription;

public interface ISetupStrategy {
	// List<Ship> getShips(ArrayList<Integer> shipsDescription);
	List<IShipDescription> getShips();

}
