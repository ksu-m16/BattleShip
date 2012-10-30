package battleship;

import model.IPoint;
import model.State;

public interface IStrategy {
	IPoint move();

	void update(IPoint point, State state);
}
