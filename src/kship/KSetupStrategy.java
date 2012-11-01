package kship;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import model.IPoint;
import model.IShipDescription;
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
		KPoint pos = new KPoint(ipos.getX(), ipos.getY());
		
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
	
	boolean tryPlace(KShipDescription sd) {
		List<IPoint> pos = sd.getPosition();
		for (IPoint p : pos) {
			if (!isFree(p)) {
				return false;
			}
		}
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
}

class BoundSS extends AbstractSS {			
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
				int x = (int)(random()*GameDescription.XMAX);
				int y = (int)(random()*GameDescription.YMAX);				
				sd.pos = new KPoint(x, y);
				sd.dir = KDirection.random();				
			} while(!tryPlace(sd));
			res.add(sd);
		}		
		return res;		
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
}

public class KSetupStrategy implements ISetupStrategy {
	
	static double[] selectStats = {0., 0., 0.};
	static double[] stats = {10.,10.,10.};
	static int count = 30;
	static int current;
	
	static ISetupStrategy[] strategies = { new RandomSS(), new BoundSS(), new DirectionSS() };
	
	static void winner() {
		stats[current]++;
		count++;		
	}
	
	static void print() {		
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
		StatsField sf = new StatsField();	
		for (int i = 0; i < 1024*128; ++i) {
			List<IShipDescription> ships = new KSetupStrategy().getShips();
			for (IShipDescription ship : ships) {
				List<IPoint> pos = ship.getPosition();
				for (IPoint p : pos) {
					sf.update(p.getX(), p.getY());					
				}
			}
		}
	}
}

