package kship;

import java.awt.Point;
import java.util.List;

import model.IPoint;
import model.State;
import battleship.GameDescription;
import battleship.IStrategy;

class StatsField {
	double[][] field = new double[GameDescription.XMAX][GameDescription.YMAX];
	int count;
	
	void update(int x, int y) {
		field[x][y]++;
		count++;
	}
	
	void print() {
		for (double[] line : field) {
			for(double cell : line) {
				System.out.print(String.format("%02.02f ", cell / count * 100));				
			}
			System.out.println();
		}
	}		
}

public class KGameStategy implements IStrategy{
	
	enum FState {KILL, HIT, VALID, IGNORE};

	static StatsField sf = new StatsField();
	FState[][] field = new FState[GameDescription.XMAX][GameDescription.YMAX];
	int[] ships = {4, 3, 2, 1};
	
	
	FState getState(KPoint p) {
		if (!KHelper.isInField(p)) {
			return FState.IGNORE;
		}		
		return field[p.x][p.y];
	}
	
	void setState(KPoint p, FState s) {
		if (!KHelper.isInField(p)) {
			return;
		}
		field[p.x][p.y] = s;
	}

	boolean tryPlace(KShipDescription sd) {		
		List<IPoint> pos = sd.getPosition();
		
		for (IPoint p : pos) {
			FState s = getState(new KPoint(p.getX(), p.getY()));			
			if ((s == FState.KILL) || (s == FState.IGNORE)) {	
				return false;
			}
		}
		
		return true;		
	}
	
	void buildVariants() {
		double[][] v = new double[GameDescription.XMAX][GameDescription.YMAX];
		
	}
	

	@Override
	public IPoint move() {
		return null;
	}
	
	void markHit(KPoint p) {
		setState(p, FState.HIT);
		setState(p.move(-1, -1), FState.IGNORE);
		setState(p.move(1, -1), FState.IGNORE);
		setState(p.move(-1, 1), FState.IGNORE);
		setState(p.move(1, 1), FState.IGNORE);
	}
	
	void markMiss(KPoint p) {
		setState(p, FState.IGNORE);
	}
	
	void markKill(KPoint p) {
		for (int dx = -1; dx <= 1; ++dx) {
			for (int dy = -1; dy <= 1; ++dy) {
				KPoint dp = p.move(dx, dy);
				if (getState(dp) != FState.HIT) {
					setState(dp, FState.IGNORE);
				}
			}
		}
		setState(p, FState.KILL);		
	}
	
	void registerKill(KPoint p) {
		int dx = 0;
		int dy = 0;
		
		if ((getState(p.move(1,0)) == FState.HIT) || (getState(p.move(-1,0)) == FState.HIT)) {
			dx = 1;
		}
		
		if ((getState(p.move(0,1)) == FState.HIT) || (getState(p.move(0,-1)) == FState.HIT)) {
			dy = 1;
		}
		
		if ((dx == 0) && (dy == 0)) {
			markKill(p);
			ships[0]--;
			return;
		}
		
		KPoint pStart = p;
		while(getState(pStart) == FState.HIT) {
			pStart = pStart.move(-dx, -dy);
		}
		pStart = pStart.move(dx, dy);
		
			KPoint dp = pStart.move(-dx, -dy);
			while (pStart.move(-dx, -dy))
		}
	}

	@Override
	public void update(IPoint point, State state) {
		KPoint p = new KPoint(point.getX(), point.getY());
		
		switch (state) {			
			case HIT:
				markHit(p);
				break;
			case MISS:
				markMiss(p);
				break;
			case KILLED:
				markHit(p);
				registerKill(p);
				break;
		}
	}
}
