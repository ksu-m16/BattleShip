package battleship;

import java.util.List;

import model.IFieldModel;
import model.IPoint;
import model.IShipDescription;
import model.State;

public class SetupHelper {

	public static boolean checkShipPlacement(IShipDescription s, State[][] field) {

		List<IPoint> shipCoords = s.getPosition();
		for (IPoint p : shipCoords) {
			if (!trySector(p, field)) {
				return false;
			}
		}
		return true;
	}

	private static boolean trySector(IPoint p, State[][] field) {
		int x = p.getX();
		int y = p.getY();

		if (getAt(x, y, field) != State.EMPTY) {
			return false;
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (getAt(x - 1 + i, y - 1 + j, field) == State.EMPTY) {
					continue;
				}
				if (getAt(x - 1 + i, y - 1 + j, field) == State.FORBIDDEN) {
					continue;
				}
				return false;
			}

		}
		return true;
	}

	private static State getAt(int x, int y, State[][] field) {
		if (x < 0 || y < 0 || x >= GameDescription.XMAX
				|| y >= GameDescription.YMAX) {
			return State.EMPTY;
		}
		return field[x][y];

	}

}
