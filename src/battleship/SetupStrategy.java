package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.FieldModel;
import model.IFieldModel;
import model.IPoint;
import model.IShipDescription;
import model.Point;
import model.ShipDescription;
import model.ShipDescription.Course;
import model.State;

public class SetupStrategy implements ISetupStrategy {

	private State[][] field;

	@Override
	public List<IShipDescription> getShips() {
		System.out.println("SetupStrategy.getShips()");

		field = new State[GameDescription.XMAX][GameDescription.YMAX];
		for (int i = 0; i < GameDescription.XMAX; i++) {
			for (int j = 0; j < GameDescription.YMAX; j++) {
				field[i][j] = State.EMPTY;
			}
		}

		ArrayList<Integer> shipsSizes = getShipsSizes();

		List<IShipDescription> listOfShips = new ArrayList<IShipDescription>();

		for (Integer shipSize : shipsSizes) {
			System.out.println("size " + shipSize);
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
					x = r.nextInt(field.length - shipSize + 1);
					y = r.nextInt(field[0].length);
				}

				if (course == Course.VERTICAL) {
					x = r.nextInt(field.length);
					y = r.nextInt(field[0].length - shipSize + 1);
				}
				
				IShipDescription s = new ShipDescription(new Point(x, y), shipSize,course);

				if (SetupHelper.checkShipPlacement(s, field)) {
					shipCanBePlaced = true;
					listOfShips.add(s);
					System.out.println("ship placed:" + s.getPosition().size());
					placeShip(s);
					makePerimeter(s);
					printField();
					System.out.println("-----------------");
				}

			}

		}

		return listOfShips;
	}

	private void placeShip(IShipDescription s) {
		List<IPoint> position = s.getPosition();
		for (IPoint p : position) {
			field[p.getX()][p.getY()] = State.SHIP;
			System.out.println("x: " + p.getX() + " y: " + p.getY());
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
					setAt(x - 1 + i, y - 1 + j, State.NEAR_SHIP);
				}
			}
		}
	}

	// returns 1x 4-point ship, 2x 3-point, 3x 2-point, 4x 1-point.
	public ArrayList<Integer> getShipsSizes() {
		int maxsize = 4;
		int numberOfShips = 10;
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

	private State getAt(int x, int y) {
		if (x < 0 || y < 0 || x >= GameDescription.XMAX || y >= GameDescription.YMAX) {
			return State.EMPTY;
		}
		return field[x][y];
	}
	
	private void setAt(int x, int y, State st) {
		if (x < 0 || y < 0 || x >= GameDescription.XMAX || y >= GameDescription.YMAX) {
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
