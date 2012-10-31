package battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

public class XuStrategy2 implements IStrategy{

	public XuStrategy2() {
		currentVictim = new ArrayList<Point>();
		 fleet = new TreeMap <Integer, Integer>();
		 fleet.put(1, 4);
		 fleet.put(2, 3);
		 fleet.put(3, 2);
		 fleet.put(4, 1);
		
		 killedFleet = new TreeMap <Integer, Integer>();
		 
		states = new State[GameDescription.XMAX][GameDescription.YMAX];
		for (int i = 0; i < GameDescription.XMAX; i++) {
			for (int j = 0; j < GameDescription.YMAX; j++) {
				states[i][j] = State.EMPTY;
			}
		}
	}

	private State[][] states;
	private ArrayList<Point> currentVictim;

	private Map<Integer, Integer> fleet; //<int size, int count>
	private Map<Integer, Integer> killedFleet;
	
	private HashMap<ArrayList<IPoint>, Integer> whiteSpacesList; // position,
																	// size
	private final int minAttacksCount = 10; 
	
	// private enum EnemyState {
	// MISS, HIT, FORBIDDEN;
	// }

	@Override
	public IPoint move() {

		if (currentVictim.size() != 0) {
			return finishHim();
		}
		
		if (getOpenedPointsCount() > minAttacksCount) {
			IPoint p = new StatisticsAnalyser(states, fleet).attack();
			if ( p != null) {
				return p;
			}
			return fireRandom();
//			return fireSophisticated(getSetupStatistics(getPossibleSetups()));
		}

		updateWhiteSpaceList();

		return fireRandom();

	}

	private void updateWhiteSpaceList() {
		whiteSpacesList = new HashMap<ArrayList<IPoint>, Integer>();
		ArrayList<IPoint> whiteSpace;

		// vertical
		for (int i = 0; i < states.length; i++) {
			whiteSpace = new ArrayList<IPoint>();
			for (int j = 0; j < states[0].length; j++) {
				if (states[i][j] == State.EMPTY) {
					whiteSpace.add(new Point(i, j));
				}

				if (((states[i][j] == State.EMPTY) && (j == states[0].length - 1))
						|| ((states[i][j] != State.EMPTY) && (whiteSpace.size() > 0))) {

					whiteSpacesList.put(whiteSpace, whiteSpace.size());

					whiteSpace = new ArrayList<IPoint>();
				}
			}

		}

		// horizontal
		for (int j = 0; j < states[0].length; j++) {
			whiteSpace = new ArrayList<IPoint>();
			for (int i = 0; i < states.length; i++) {
				if (states[i][j] == State.EMPTY) {
					whiteSpace.add(new Point(i, j));
				}

				if (((states[i][j] == State.EMPTY) && (i == states.length - 1))
						|| ((states[i][j] != State.EMPTY) && (whiteSpace.size() > 0))) {

					whiteSpacesList.put(whiteSpace, whiteSpace.size());

					whiteSpace = new ArrayList<IPoint>();
				}
			}
		}
	}

	private IPoint finishHim() {
		if (currentVictim.size() == 1) {
			return (fireHorizontal(currentVictim) != null) ? fireHorizontal(currentVictim)
					: fireVertical(currentVictim);
		}

		Collections.sort(currentVictim);

		if (isCourseHorisontal(currentVictim)) {
			return fireHorizontal(currentVictim);
		}
		return fireVertical(currentVictim);
	}

	private IPoint fireHorizontal(ArrayList<Point> victim) {
		IPoint left = new Point(victim.get(0).getX() - 1, victim.get(0).getY());
		if (getAt(left.getX(), left.getY()) == State.EMPTY) {
			return left;
		}

		IPoint right = new Point((victim.get(victim.size() - 1).getX() + 1),
				victim.get(victim.size() - 1).getY());
		if (getAt(right.getX(), right.getY()) == State.EMPTY) {
			return right;
		}
		return null;
	}

	private IPoint fireVertical(ArrayList<Point> victim) {
		IPoint top = new Point(victim.get(0).getX(), victim.get(0).getY() - 1);
		if (getAt(top.getX(), top.getY()) == State.EMPTY) {
			return top;
		}

		IPoint bottom = new Point(victim.get(victim.size() - 1).getX(), victim
				.get(victim.size() - 1).getY() + 1);
		if (getAt(bottom.getX(), bottom.getY()) == State.EMPTY) {
			return bottom;
		}
		return null;
	}

	private IPoint fireRandom() {

		Object[] sizes = whiteSpacesList.values().toArray();
		Arrays.sort(sizes);
		int maxSize = (Integer) sizes[sizes.length - 1];

		for (Map.Entry<ArrayList<IPoint>, Integer> whiteSpace : whiteSpacesList
				.entrySet()) {
			if (whiteSpace.getValue() == maxSize) {
				ArrayList<IPoint> whiteSpaceCoords = whiteSpace.getKey();
				int medianIndex = whiteSpaceCoords.size() / 2;
				return whiteSpace.getKey().get(medianIndex);
			}
		}

		throw new IllegalStateException("It's nowhere to fire =(");

	}

	private boolean isCourseHorisontal(ArrayList<Point> victim) {
		boolean isHorizontal = true;
		boolean isVertical = true;
		if (victim.size() == 0) {
			throw new IllegalArgumentException("ship with size = 0");
		}

		int x = victim.get(0).getX();
		int y = victim.get(0).getY();

		for (IPoint p : victim) {
			isHorizontal = isHorizontal && (p.getY() == y);
			isVertical = isVertical && (p.getX() == x);
		}
		if (isHorizontal != isVertical) {
			return isHorizontal;
		}
		throw new IllegalArgumentException("ship with strange form"
				+ victim.toString());
	}

	@Override
	public void update(IPoint point, State state) {
		states[point.getX()][point.getY()] = state;
		switch (state) {
		case KILLED: {
			states[point.getX()][point.getY()] = State.HIT;
			currentVictim.add((Point) point);
			surroundCorpse(currentVictim);
			 fleet.put(currentVictim.size(), (fleet.get(currentVictim.size())
			 - 1));
			 
			 if (! killedFleet.containsKey(currentVictim.size())) {
				 killedFleet.put(currentVictim.size(), 1);
			 }
			 else {
				 killedFleet.put(currentVictim.size(), 
						 killedFleet.get(currentVictim.size()) + 1);
				 
			 }
 
//			System.out.println("killed! " + currentVictim);

			currentVictim = new ArrayList<Point>();

			break;
		}
		case HIT: {
			currentVictim.add((Point) point);
//			System.out.println("hit! " + currentVictim);
			break;
		}
		default: {
//			System.out.println("miss! " + currentVictim);
		}
		}
	}

	private void surroundCorpse(ArrayList<Point> corpse) {
		for (Point p : corpse) {
			makePerimeter(p);
		}
	}

	private void makePerimeter(Point p) {
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
			return State.FORBIDDEN;
		}
		return states[x][y];
	}

	private void setAt(int x, int y, State st) {
		if (x < 0 || y < 0 || x >= GameDescription.XMAX
				|| y >= GameDescription.YMAX) {
			// return;
			throw new IllegalArgumentException(
					"try to shoot out of field: " + x + ", " + y);
		}
		states[x][y] = st;

	}

	private int getOpenedPointsCount() {
		int count = 0;
		for (int i = 0; i < states.length; i++) {
			for (int j = 0; j < states[0].length; j++) {
				if (states[i][j] != State.EMPTY){
					count++;
				}
			}
		}
		return count;
	}

}
