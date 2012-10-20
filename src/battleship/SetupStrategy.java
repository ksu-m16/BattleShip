package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Field;
import model.Field.State;
import model.IField;
import model.Ship;
import model.Ship.Course;

public class SetupStrategy implements ISetupStrategy {

	IField field;

	SetupStrategy(IField field) {
		this.field = field;
	}

	@Override
	public List<Ship> getListOfShips(ArrayList<Integer> shipsDescription) {

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
					x = r.nextInt(Field.XSIZE - shipSize + 1);
					y = r.nextInt(Field.YSIZE);

					if (course == Course.VERTICAL) {
						x = r.nextInt(Field.XSIZE);
						y = r.nextInt(Field.YSIZE - shipSize + 1);
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

	public int[][] getShipCoords(Ship s) {
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
			field.setState(shipCoords[i][0], shipCoords[i][1], Field.State.SHIP);
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
					field.setState(x - 1 + i, y - 1 + j, Field.State.NEAR_SHIP);
				}
			}
		}
	}

}
