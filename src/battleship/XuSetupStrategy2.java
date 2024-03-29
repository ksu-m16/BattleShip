package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.IPoint;
import model.IShipDescription;
import model.Point;
import model.ShipDescription;
import model.State;
import model.ShipDescription.Course;

public class XuSetupStrategy2 implements ISetupStrategy {
	private State[][] field;

	@Override
	public List<IShipDescription> getShips() {
		// System.out.println("SetupStrategy.getShips()");

		field = new State[GameDescription.XMAX][GameDescription.YMAX];
		for (int i = 0; i < GameDescription.XMAX; i++) {
			for (int j = 0; j < GameDescription.YMAX; j++) {
				field[i][j] = State.EMPTY;
			}
		}

		int shipsSizes[] = { 4, 3, 3, 2, 2, 2, 1, 1, 1, 1 };

		List<IShipDescription> listOfShips = new ArrayList<IShipDescription>();

		for (Integer shipSize : shipsSizes) {
			boolean shipCanBePlaced = false;

			while (!shipCanBePlaced) {

				Random r = new Random();
				int x = 0;
				int y = 0;

				// Course is random, too. 42 is meaning of life, the universe
				// and everything.
				Course course = r.nextInt(42) < 21 ? Course.HORIZONTAL
						: Course.VERTICAL;

				if (course == Course.HORIZONTAL) {
					x = r.nextInt(42) < 21 ? 0 : (field.length - shipSize);
					y = r.nextInt(field[0].length);
				}

				if (course == Course.VERTICAL) {
					x = r.nextInt(field.length);
					y = r.nextInt(42) < 21 ? 0 : (field[0].length - shipSize);
				}

				IShipDescription s = new ShipDescription(new Point(x, y),
						shipSize, course);

				if (SetupHelper.checkShipPlacement(s, field)) {
					shipCanBePlaced = true;
					listOfShips.add(s);
					// System.out.println("ship placed:" +
					// s.getPosition().size());
					placeShip(s);
					makePerimeter(s);
//					 printField();
//					 System.out.println("-----------------");
				}

			}

		}

		return listOfShips;
	}

	private void placeShip(IShipDescription s) {
		List<IPoint> position = s.getPosition();
		for (IPoint p : position) {
			field[p.getX()][p.getY()] = State.SHIP;
			// System.out.println("x: " + p.getX() + " y: " + p.getY());
		}
	}

	private void makePerimeter(IShipDescription s) {
		List<IPoint> position = s.getPosition();
		for (IPoint p : position) {
			makePerimeter(p);
		}
	}

	private void makePerimeter(IPoint p) {
		int x = p.getX();
		int y = p.getY();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (getAt(x - 1 + i, y - 1 + j) == State.EMPTY) {
					setAt(x - 1 + i, y - 1 + j, State.FORBIDDEN);
				}
			}
		}
	}

	private State getAt(int x, int y) {
		if (x < 0 || y < 0 || x >= GameDescription.XMAX
				|| y >= GameDescription.YMAX) {
			return State.EMPTY;
		}
		return field[x][y];
	}

	private void setAt(int x, int y, State st) {
		if (x < 0 || y < 0 || x >= GameDescription.XMAX
				|| y >= GameDescription.YMAX) {
			return;
		}
		field[x][y] = st;

	}

	private void printField() {
		for (int j = 0; j < field.length; j++) {
			for (int i = 0; i < field.length; i++) {
				System.out.print(field[i][j]);
			}
			System.out.println();
		}
	}
}
