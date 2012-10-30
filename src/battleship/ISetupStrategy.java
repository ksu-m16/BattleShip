package battleship;

import java.util.List;

import model.IShipDescription;

public interface ISetupStrategy {
	List<IShipDescription> getShips();

}
