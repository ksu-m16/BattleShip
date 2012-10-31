package battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import model.IPoint;
import model.IShipDescription;
import model.Point;
import model.ShipDescription;
import model.State;
import model.ShipDescription.Course;


public class StatisticsAnalyser {
//	StatisticsAnalyser(State[][] states) {
//		this.states = states;
//	}
	StatisticsAnalyser(State[][] states, Map<Integer, Integer> fleet) {
		this.states = states;
		this.fleet = fleet;
		fleetArr = getShipsSizes();
//		System.out.println(Arrays.toString(fleetArr));
		
	}
	
	
	private State[][] states;
	
	private Map<Integer, Integer> fleet; //<int size, int count>
	private Integer[] fleetArr;
	
	private ArrayList<IPoint> whiteSpacesList; 
	
	
	private final int nSetups = 10;

	public IPoint attack() {
		
//		printField(states);
		return attack(getSetupStatistics(getPossibleSetups()));
	}
	
	
	private void makePerimeter(List<IPoint> ship, State[][] field) {
		for (IPoint p : ship) {
			makePerimeter(p, field);
		}
	}

	private void makePerimeter(IPoint p, State[][] field) {
		int x = p.getX();
		int y = p.getY();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (getAt(x - 1 + i, y - 1 + j, field) == State.EMPTY) {
					setAt(x - 1 + i, y - 1 + j, field, State.FORBIDDEN);
				}
			}
		}
	}

	private State getAt(int x, int y, State[][] field) {
		if (x < 0 || y < 0 || x >= field.length
				|| y >= field[0].length) {
			return State.FORBIDDEN;
		}
		return field[x][y];
	}

	private void setAt(int x, int y, State[][] field, State st) {
		if (x < 0 || y < 0 || x >= field.length
				|| y >= field[0].length) {
			// return;
			throw new IllegalArgumentException(
					"try to shoot out of field: " + x + ", " + y);
		}
		field[x][y] = st;

	}

	private ArrayList<State[][]> getPossibleSetups() {
		ArrayList<State[][]> setups = new ArrayList<State[][]>();
		
		for (int i = 0; i < nSetups; i++) {
			setups.add(getPossibleSetup());
		}

		return setups;
	}
	
	// counts total number of SHIP state for every point
	private int[][] getSetupStatistics(ArrayList<State[][]> setups) {
		int[][] statistics = new int[states.length][states[0].length];
		
		for (State[][] setup : setups) {
			for (int i = 0; i < states.length; i++) {
				for (int j = 0; j < states[0].length; j++) {
					if (setup[i][j] == State.SHIP){
						statistics[i][j]++;
					}
				}
			}
		}
		return statistics;
	} 
	
	private IPoint attack(int[][] statistics) {
		
		HashMap<IPoint, Integer> statsMap = new HashMap<IPoint, Integer>();
		
		for (int i = 0; i < statistics.length; i++) {
			for (int j = 0; j < statistics[0].length; j++) {
				statsMap.put(new Point(i,j), statistics[i][j]);
			}
		}
		StatsMapSorter ms = new StatsMapSorter(statsMap);
		TreeMap<IPoint, Integer> sortedStatsMap = ms.sortByCount();
		
//		for (Map.Entry<IPoint, Integer> entry : sortedStatsMap.entrySet()) {
////			System.out.println(entry);
//		}
		
		for (Map.Entry<IPoint, Integer> entry : sortedStatsMap.entrySet()) {
			if(states[entry.getKey().getX()][entry.getKey().getY()] == State.EMPTY){
				return entry.getKey();		
			}
		}
		
		System.out.println("Statistics is weak here =(");
		
		return null;
		
		
	}
	
	private class StatsMapSorter {
		StatsMapSorter(HashMap<IPoint, Integer> statsMap) {
			this.statsMap = statsMap;
		}

	    private HashMap<IPoint, Integer> statsMap;

	    public TreeMap<IPoint, Integer> sortByCount() {
	        TreeMap<IPoint, Integer> sortedStatsMap = new TreeMap<IPoint, Integer>(new Comparator<IPoint>() {
	            public int compare(IPoint p1, IPoint p2) {
	                int i1 = statsMap.get(p1);
	                int i2 = statsMap.get(p2);
	                if (i1 < i2) return 1;
	                return -1;
	            }
	        });
	        sortedStatsMap.putAll(statsMap);
	        return sortedStatsMap;
	    }
	}
	
	
	private State[][] getPossibleSetup() {
//		System.out.println("getPossibleSetup");
//		System.out.println("ships" + Arrays.toString(fleetArr));
		State[][] possibleSetup = new State[states.length][states[0].length];
		for (int i = 0; i < states.length; i++) {
			for (int j = 0; j < states[0].length; j++) {
				possibleSetup[i][j] = states[i][j];
			}
		}
		
		

//		List<IShipDescription> listOfShips = new ArrayList<IShipDescription>();

		for (int i = fleetArr.length - 1; i >= 0; i--) {
//			System.out.println("i " + i);
			updateWhiteSpaceList(possibleSetup);
//		for (Integer shipSize : shipsSizesArray) {
			int shipSize = fleetArr[i];
			boolean shipCanBePlaced = false;
			int nTries = 100;
			int n = 0;
			while (!shipCanBePlaced) {

				Random r = new Random();
				int index = r.nextInt(whiteSpacesList.size());
				int x = 0;
				int y = 0;

				// Course is random, too. 42 is meaning of life, the universe
				// and everything.
				Course course = r.nextInt(42) < 21 ? Course.HORIZONTAL
						: Course.VERTICAL;

					x = whiteSpacesList.get(index).getX();
					y = whiteSpacesList.get(index).getY();
			
				IShipDescription s = new ShipDescription(new Point(x, y),
						shipSize, course);

				if (SetupHelper.checkShipPlacement(s, possibleSetup)) {
					shipCanBePlaced = true;
//					listOfShips.add(s);
//					 System.out.println("ship placed:" +
//					 s.getPosition().size());
//					 System.out.println(s.getPosition());
					placeShip(s.getPosition(), possibleSetup);
					makePerimeter(s.getPosition(), possibleSetup);
					// printField();
					// System.out.println("-----------------");
					break;
				}
				n++;
//				System.out.println("n = " + n);
				if(n > nTries) {
//					System.out.println("too much tries");
//					printField(possibleSetup);
					getPossibleSetup();
				}
				
			}

		}
//		printField(possibleSetup);
		return possibleSetup;
	}
	

	private Integer[] getShipsSizes() {
//		System.out.println("fleet " + fleet.size());
		ArrayList<Integer> shipsSizes = new ArrayList<Integer>();
		for (Map.Entry<Integer, Integer> entry : fleet.entrySet()) {
			for (int i = 0; i < entry.getValue(); i++) {
				shipsSizes.add(entry.getKey());
			}
		}
		Integer[] shipsSizesArray = new Integer[shipsSizes.size()];
		shipsSizes.toArray(shipsSizesArray);
		Arrays.sort(shipsSizesArray);
		return shipsSizesArray;		
	}
	
	private State[][] placeShip(List<IPoint> ship, State[][] field) {
		for (IPoint p : ship) {
			field[p.getX()][p.getY()] = State.SHIP;
			// System.out.println("x: " + p.getX() + " y: " + p.getY());
		}
		return field;
	}
	
	
	
	private void updateWhiteSpaceList(State[][] field) {
		whiteSpacesList = new ArrayList<IPoint>();
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < states[0].length; j++) {
				if (field[i][j] == State.EMPTY) {
					whiteSpacesList.add(new Point(i, j));
				}

			}

		}

	}

	private void printField(State[][] field) {
		for (int j = 0; j < field.length; j++) {
			for (int i = 0; i < field.length; i++) {
				System.out.print(field[i][j]);
			}
			System.out.println();
		}
	}
	

}
