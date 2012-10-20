package battleship;

import java.util.ArrayList;
import java.util.List;

import model.Ship;

public interface ISetupStrategy {
	List<Ship> getListOfShips(ArrayList<Integer> shipsDescription);
}
