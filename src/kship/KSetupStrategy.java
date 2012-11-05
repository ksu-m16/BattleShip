package kship;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import model.IPoint;
import model.IShipDescription;
import model.ShipDescription;
import battleship.GameDescription;
import battleship.ISetupStrategy;

abstract class AbstractSS implements ISetupStrategy {
	
	enum State { 		
		SET("X"), FREE(".");
		
		State(String name) {
			this.name = name;
		}
		public final String name;
		
		@Override
		public String toString() {		
			return name;
		}		
	};
		
	
	State[][] field = new State[GameDescription.XMAX][GameDescription.YMAX];
	int[] sizes = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
	
	void clear() {
		for (State[] line : field) {
			Arrays.fill(line, State.FREE);
		}
	}
	
	boolean isFree(IPoint ipos) {
		KPoint pos = KPoint.getInstance(ipos.getX(), ipos.getY());
		
		if (!KHelper.isInField(pos)) {
			return false;
		}
		
		for (int dx = -1; dx <= 1; ++dx) {
			for (int dy = -1; dy <= 1; ++dy) {				
				KPoint p = pos.move(dx, dy);
				if (!KHelper.isInField(p)) {
					continue;
				}
				if (field[p.x][p.y] != State.FREE) {
					return false;
				}
			}
		}
		return true;
	}
	
	boolean testPlace(KShipDescription sd) {
		List<IPoint> pos = sd.getPosition();
		for (IPoint p : pos) {
			if (!isFree(p)) {
				return false;
			}
		}
		return true;
	}
	
	boolean tryPlace(KShipDescription sd) {
		if (!testPlace(sd)) {
			return false;
		}
		
		List<IPoint> pos = sd.getPosition();		
		for (IPoint p : pos) {			
			field[p.getX()][p.getY()] = State.SET;
		}
		return true;
	}
}

class RandomSS extends AbstractSS {

	@Override
	public List<IShipDescription> getShips() {
	
		clear();
		
		List<IShipDescription> res = new LinkedList<IShipDescription>();
		
		for (int size : sizes) {
			KShipDescription sd = new KShipDescription();
			sd.size = size;
			do {
				sd.pos = KHelper.p0.random(KHelper.pMax);				
				sd.dir = KDirection.random();				
			} while(!tryPlace(sd));
			res.add(sd);
		}		
		return res;		
	}	
	
	@Override
	public String toString() {	
		return "random";
	}
}

class BoundSS extends AbstractSS {			
	static final double factor = 3.;
	
	public double random() {
		double r = Math.pow(Math.random(), factor);
		return (Math.random() < 0.5)?r:0.999999999-r;
	}
	
	@Override
	public List<IShipDescription> getShips() {
	
		clear();
		
		List<IShipDescription> res = new LinkedList<IShipDescription>();
		
		for (int size : sizes) {
			KShipDescription sd = new KShipDescription();
			sd.size = size;
			do {
				int x = (int)(random()*GameDescription.XMAX);
				int y = (int)(random()*GameDescription.YMAX);				
				sd.pos = KPoint.getInstance(x, y);
				sd.dir = KDirection.random();				
			} while(!tryPlace(sd));
			res.add(sd);
		}		
		return res;		
	}		
	
	@Override
	public String toString() {	
		return "bound";
	}
}

class DirectionSS extends AbstractSS {			
	static final double factor = 3.;
	
	public double random() {
		double r = Math.pow(Math.random(), factor);
		return (Math.random() < 0.5)?r:1-r;
	}
	
	@Override
	public List<IShipDescription> getShips() {
	
		clear();
		
		List<IShipDescription> res = new LinkedList<IShipDescription>();
		
		for (int size : sizes) {
			KShipDescription sd = new KShipDescription();
			sd.size = size;
			do {				
				sd.pos = KHelper.p0.random(KHelper.pMax); 
				sd.dir = KDirection.hvRandom(0.1);
			} while(!tryPlace(sd));
			res.add(sd);
		}		
		return res;		
	}		
	
	@Override
	public String toString() {
		return "direction";
	}
}

class CompactSS extends AbstractSS {
	
	enum SState {SET, FREE};
	SState sfield[][] = new SState[GameDescription.XMAX][GameDescription.YMAX];
	
	SState getSState(KPoint p) {
		if (!KHelper.isInField(p)) {
			return SState.SET;			
		}
		return sfield[p.x][p.y];
	}
	
	void setSState(KPoint p, SState s) {
		if (!KHelper.isInField(p)) {
			return;
		}
		sfield[p.x][p.y] = s;
	}
	
	Set<KPoint> getBorder(KShipDescription sd) {
		Set<KPoint> b = new TreeSet<KPoint>();
		KPoint[] pos = sd.getPositionArray();
		for (KPoint p : pos) {
			for (int x = -1; x <= 1; ++x) {
				for (int y = -1; y <= 1; ++y) {
					b.add(p.move(x, y));
				}
			}
		}
		
		for (KPoint p : pos) {
			b.remove(p);
		}
		
		return b;
	}
	
	List<KShipDescription> getVariants(int size) {
		
		int freeCnt = 15;
		List<KShipDescription> variants = new LinkedList<KShipDescription>();
		
		for (int x = 0; x < GameDescription.XMAX; ++x) {
			for (int y = 0; y < GameDescription.YMAX; ++y) {
				for (KDirection d : KDirection.all) {
					KShipDescription sd = new KShipDescription();
					sd.pos = KPoint.getInstance(x, y);
					sd.size = size;						
					sd.dir = d;
					
					if (!testPlace(sd)) {
						continue;
					}

					int curFreeCnt = 0;
					Set<KPoint> border = getBorder(sd);
					for (KPoint p : border) {
						if (getSState(p) == SState.FREE) {
							curFreeCnt++;
						}
					}
					
					if (curFreeCnt > freeCnt) {
						continue;
					}
					
					if (curFreeCnt < freeCnt) {
						freeCnt = curFreeCnt;
						variants.clear();
					}
					
					variants.add(sd);
				}
			}					
		}	
		return variants;
	}
	
	void markSet(KShipDescription sd) {
		KPoint[] pos = sd.getPositionArray();
		for (KPoint p : pos) {
			for (int x = -1; x <= 1; ++x) {
				for (int y = -1; y <= 1; ++y) {
					setSState(p.move(x, y), SState.SET);					
				}
			}
		}	
	}
	
	@Override
	void clear() {
		super.clear();
		for (SState[] line: sfield) {
			Arrays.fill(line, SState.FREE);
		}		
	}
	
	public List<IShipDescription> getShips() {
		
		clear();
			
		List<IShipDescription> res = new LinkedList<IShipDescription>();
		for (int size : sizes) {
			List<KShipDescription> variants = getVariants(size);
			KShipDescription sd = variants.get((int)(variants.size() * Math.random()));
			tryPlace(sd);
			markSet(sd);
			res.add(sd);			
		}
		
		return res;
	}
	
	@Override
	public String toString() {	
		return "compact";
	}
}

public class KSetupStrategy implements ISetupStrategy {
	
	public static ISetupStrategy getInstance(String name) {
		return strategyMap.get(name);
	}
	
	static double[] selectStats = {0., 0., 0., 0.};
	static double[] stats = {10., 10., 10., 400.};
	static double count = 0;
	static {
		for (double d : stats) {
			count += d;
		}
	}
	
	static int current;
	
	static ISetupStrategy[] strategies = { new RandomSS(), new BoundSS(), new DirectionSS(), new CompactSS() };
	
	static Map<String, ISetupStrategy> strategyMap = new TreeMap<String, ISetupStrategy>();
	static {
		for (ISetupStrategy s : strategies) {
			strategyMap.put(s.toString(), s);
		}
	}

	static void winner() {
		stats[current]++;
		count++;		
	}
	
	public static void print() {		
		System.out.println(Arrays.toString(stats));
		System.out.println(Arrays.toString(selectStats));
	}

	@Override
	public List<IShipDescription> getShips() {
		
		double r = Math.random();
		double ss = 0;
		for (current = 0; current < stats.length; ++current) {
			ss += stats[current] / count;
			if (r < ss) {
				selectStats[current]++;
				return strategies[current].getShips();				
			}			
		}

		current = 0;
		selectStats[current]++;
		return strategies[current].getShips();
	}	

	public static void main(String[] args) {
		
		CompactSS css = new CompactSS();		
		StatsField sf = new StatsField();	
		for (int i = 0; i < 1024; ++i) {
			List<IShipDescription> ships = css.getShips();
			for (IShipDescription ship : ships) {
				List<IPoint> pos = ship.getPosition();
				for (IPoint p : pos) {
					sf.update(p.getX(), p.getY());					
				}
			}
		}
		sf.print();
	}
}

