package battleship;

	import java.util.ArrayList;
	import java.util.Arrays;
	import java.util.Collections;
	import java.util.HashMap;
	import java.util.Map;
import java.util.Random;
import java.util.Set;
	import java.util.TreeMap;

	import model.IPoint;
	import model.IShipDescription;
	import model.Point;
	import model.ShipDescription;
	import model.State;
import model.ShipDescription.Course;
	
	
// searches 4 ships at borders first
	public class XuStrategy5 implements IStrategy {	
		public XuStrategy5() {
			currentVictim = new ArrayList<Point>();
			
			fleet = new TreeMap<Integer, Integer>();
			fleet.put(1, 4);
			fleet.put(2, 3);
			fleet.put(3, 2);
			fleet.put(4, 1);

			killedFleet = new TreeMap<Integer, Integer>();
			
			states = new State[GameDescription.XMAX][GameDescription.YMAX];
			for (int i = 0; i < GameDescription.XMAX; i++) {
				for (int j = 0; j < GameDescription.YMAX; j++) {
					states[i][j] = State.EMPTY;
				}
			}
		}

		private State[][] states;
		private ArrayList<Point> currentVictim;

		private Map<Integer, Integer> fleet; // <int size, int count>
		private Map<Integer, Integer> killedFleet;
		private final int minAttacksCount = 5;

		private HashMap<ArrayList<IPoint>, Integer> whiteSpacesList; // position,
																		// size

		// private enum EnemyState {
		// MISS, HIT, FORBIDDEN;
		// }

		@Override
		public IPoint move() {

			if (currentVictim.size() != 0) {
				return finishHim();
			}

//			if (getOpenedPointsCount() > minAttacksCount) {
//				
//				if (fleet.get(4) > 0) {
//					IPoint p = new StatisticsAnalyser2().attack(4, states);
//					
////					if (p.getX() == 0 && p.getY() == 0) {
////					System.out.println(p);
////					}
//					return p;
//				}
//				
//				if (fleet.get(3) > 0) {
//					IPoint p = new StatisticsAnalyser2().attack(3, states);
//					return p;
//				}
//				
//				
////				if (p != null) {
////					return p;
////				}
////				return fireRandom();
//
//			}
			
			
			updateWhiteSpaceList();

			return fireBorders(getMaxAliveSize());

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

		// selects point to attack near the specified point (the biggest whitespace)
		private IPoint selectWhitespace(Point p,
				ArrayList<ArrayList<IPoint>> pointWhiteSpaceList) {

			ArrayList<IPoint> maxWhitespace = pointWhiteSpaceList.get(0);
			for (ArrayList<IPoint> whitespace : pointWhiteSpaceList) {
				if (whitespace.size() > maxWhitespace.size()) {
					maxWhitespace = whitespace;
				}
			}

			for (IPoint w : maxWhitespace) {
				if (Math.abs(w.getX() - p.getX()) == 1) {
					return w;
				}
				if (Math.abs(w.getY() - p.getY()) == 1) {
					return w;
				}

			}
			throw new IllegalArgumentException(
					"Unable to find emty point near the specified.");
		}

		// list of whitespaces around specified point
		private ArrayList<ArrayList<IPoint>> getPointWhiteSpaceList(Point p) {
			ArrayList<ArrayList<IPoint>> pointWhiteSpaceList = new ArrayList<ArrayList<IPoint>>();

			ArrayList<IPoint> whitespace = new ArrayList<IPoint>();
			// left
			for (int i = p.getX() - 1; i >= 0; i--) {
				if (!extendWhitespace(i, p.getY(), whitespace, states)) {
					break;
				}
			}

			if (whitespace.size() != 0) {
				pointWhiteSpaceList.add(whitespace);
				whitespace = new ArrayList<IPoint>();
			}

			// right
			for (int i = p.getX() + 1; i < states.length; i++) {
				if (!extendWhitespace(i, p.getY(), whitespace, states)) {
					break;
				}
			}

			if (whitespace.size() != 0) {
				pointWhiteSpaceList.add(whitespace);
				whitespace = new ArrayList<IPoint>();
			}

			// up
			for (int i = p.getY() - 1; i >= 0; i--) {
				if (!extendWhitespace(p.getX(), i, whitespace, states)) {
					break;
				}
			}

			if (whitespace.size() != 0) {
				pointWhiteSpaceList.add(whitespace);
				whitespace = new ArrayList<IPoint>();
			}

			// down
			for (int i = p.getY() + 1; i < states[0].length; i++) {
				if (!extendWhitespace(p.getX(), i, whitespace, states)) {
					break;
				}
			}

			if (whitespace.size() != 0) {
				pointWhiteSpaceList.add(whitespace);
				whitespace = new ArrayList<IPoint>();
			}

			return pointWhiteSpaceList;
		}

		private boolean extendWhitespace(int i, int j,
				ArrayList<IPoint> whitespace, State[][] field) {
			if (states[i][j] != State.EMPTY) {
				return false;
			}
			whitespace.add(new Point(i, j));

			return true;
		}

		private IPoint finishHim() {
			if (currentVictim.size() == 1) {
				return selectWhitespace(currentVictim.get(0),
						getPointWhiteSpaceList(currentVictim.get(0)));
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

		private IPoint fireBorders(int maxAliveSize) {

			Object[] sizes = whiteSpacesList.values().toArray();
			
			Set<ArrayList<IPoint>> whiteSpacesSet = whiteSpacesList.keySet();
			
			ArrayList<IPoint> pointsToFire = new ArrayList<IPoint>();
						
			for (ArrayList<IPoint> ws: whiteSpacesSet) {
				if (ws.size() >= maxAliveSize) {
					for (IPoint p: ws) {
						if (p.getX() == 0 || p.getY() == 0 || 
								p.getX() == states.length - 1 || p.getY() == states[0].length - 1) {
							pointsToFire.add(p);
//							return p;
						}
					}
				}
			}
			
			if (pointsToFire.size() > 0) {
				Random r = new Random();
				return pointsToFire.get(r.nextInt(pointsToFire.size()));
				
			}
			
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
				 
				if (!killedFleet.containsKey(currentVictim.size())) {
						killedFleet.put(currentVictim.size(), 1);
				} else {
						killedFleet.put(currentVictim.size(),
								killedFleet.get(currentVictim.size()) + 1);

				}

//				 System.out.println("killed! " + currentVictim);

				currentVictim = new ArrayList<Point>();

				break;
			}
			case HIT: {
				currentVictim.add((Point) point);
//				 System.out.println("hit! " + currentVictim);
				break;
			}
			default: {
//				 System.out.println("miss! " + currentVictim);
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
				throw new IllegalArgumentException("try to shoot out of field: "
						+ x + ", " + y);
			}
			states[x][y] = st;

		}
		
		private int getOpenedPointsCount() {
			int count = 0;
			for (int i = 0; i < states.length; i++) {
				for (int j = 0; j < states[0].length; j++) {
					if (states[i][j] != State.EMPTY) {
						count++;
					}
				}
			}
			return count;
		}

		private int getMaxAliveSize() {
			
			for (int i = 4; i > 0; i--) {
				if (fleet.get(i) > 0) {
					return i;
				}
				
			}
			System.out.print("Nobody alive");
			return 0;
		}
		
//		public void printField() {
//			for (int j = 0; j < states.length; j++) {
//				for (int i = 0; i < states.length; i++) {
//					System.out.print(states[i][j]);
//				}
//				System.out.println();
//			}
//		}
		
	}


