package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.FieldModel;
import model.IFieldModel;
import model.Ship;
import model.Ship.Course;
import model.State;

public class SetupStrategy implements ISetupStrategy {

	IFieldModel field;

	SetupStrategy(IFieldModel field) {
		this.field = field;
	}

	@Override
	public List<Ship> getShips() {

		ArrayList<Integer> shipsDescription = getShipsDescription();
		List<Ship> listOfShips = new ArrayList<Ship>();

		for (Integer shipSize : shipsDescription) {

			boolean shipCanBePlaced = false;

			while (!shipCanBePlaced) {

				Random r = new Random();
				int x = 0;
				int y = 0;

				// Course is random, too. 42 is meaning of life, the universe
				// and everything.
				Ship.Course course = r.nextInt(42) < 21 ? Ship.Course.HORIZONTAL
						: Ship.Course.VERTICAL;

				if (course == Course.HORIZONTAL) {
					x = r.nextInt(FieldModel.XSIZE - shipSize + 1);
					y = r.nextInt(FieldModel.YSIZE);

					if (course == Course.VERTICAL) {
						x = r.nextInt(FieldModel.XSIZE);
						y = r.nextInt(FieldModel.YSIZE - shipSize + 1);
					}
					Ship s = Ship.createShip(x, y, shipSize, course);
					if (tryToPlaceShip(s)) {
						shipCanBePlaced = true;
						listOfShips.add(s);
						System.out.println("ship placed:" + s.getSize());
						placeShip(s);
						makePerimeter(s);
						field.printField();
						System.out.println("-----------------");
					}

				}

			}

		}

		return listOfShips;
	}

	private int[][] getShipCoords(Ship s) {
		int[][] shipCoords = new int[s.getSize()][2];
		int x = s.getPosition()[0];
		int y = s.getPosition()[1];
		switch (s.getCourse()) {
		case HORIZONTAL: {
			for (int i = 0; i < s.getSize(); i++) {
				shipCoords[i] = new int[] { x + i, y };
			}
			break;

		}
		case VERTICAL: {
			for (int i = 0; i < s.getSize(); i++) {
				shipCoords[i] = new int[] { x, y + i };
			}
		}
		}
		return shipCoords;
	}

	public boolean tryPlace(int x, int y) {
		switch (getState(x, y)) {
		case EMPTY:
			return true;
		default:
			return false;
		}
	}
	
	
	private boolean trySector(int x, int y) {
		if (field.getState(x, y) != State.EMPTY) {
			return false;
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (field.getState(x - 1 + i, y - 1 + j) == State.EMPTY) {
					continue;
				}
				if (field.getState(x - 1 + i, y - 1 + j) == State.NEAR_SHIP) {
					continue;
				}
				return false;
			}

		}
		return true;
	}

	private boolean tryToPlaceShip(Ship s) {
		int[][] shipCoords = getShipCoords(s);
		for (int i = 0; i < s.getSize(); i++) {
			if (!trySector(shipCoords[i][0], shipCoords[i][1])) {
				return false;
			}
		}
		return true;
	}

	private void placeShip(Ship s) {
		int[][] shipCoords = getShipCoords(s);
		for (int i = 0; i < s.getSize(); i++) {
			field.setState(shipCoords[i][0], shipCoords[i][1], State.SHIP);
			System.out.println("x: " + shipCoords[i][0] + " y: "
					+ shipCoords[i][1]);
		}
	}

	private void makePerimeter(Ship s) {
		int[][] shipCoords = getShipCoords(s);
		for (int i = 0; i < s.getSize(); i++) {
			makePerimeter(shipCoords[i][0], shipCoords[i][1]);
		}

	}

	private void makePerimeter(int x, int y) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (field.getState(x - 1 + i, y - 1 + j) == State.EMPTY) {
					field.setState(x - 1 + i, y - 1 + j, State.NEAR_SHIP);
				}
			}
		}
	}
	
	
	//returns 1x 4-point ship, 2x 3-point, 3x 2-point, 4x 1-point.
	public ArrayList<Integer> getShipsDescription() {
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
	

}
