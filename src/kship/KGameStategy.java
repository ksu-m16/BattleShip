package kship;

import java.util.Arrays;
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
	
	double getAt(int x, int y) {
		if (field[x][y] == 0) {
			return 1.;
		}
		return field[x][y] * 100. / count;
	}
	
	void print() {
		KHelper.printField(field, 100. / count);
	}	
}

public class KGameStategy implements IStrategy{	
	
	enum FState {
		KILL("X"), HIT("V"), VALID("."), IGNORE("*");
		
		private FState(String s) {
			sign = s;
		}
		
		public String toString() {
			return sign;
		};
		
		String sign;
	};

	static StatsField sf = new StatsField();
        
        
        public static final int MAXSHIP = 4;
	FState[][] field = new FState[GameDescription.XMAX + MAXSHIP*2][GameDescription.YMAX + MAXSHIP*2];
	int[] shipsLeft = {4, 3, 2, 1};
		
	double fNear;
	double fHit;
	
	public KGameStategy(double fNear, double fHit) {		
		this.fNear = fNear;
		this.fHit = fHit;
		
		for (FState[] line : field) {
			Arrays.fill(line, FState.IGNORE);
		}
        for (int x = 0; x < GameDescription.XMAX; ++x) {
            for (int y = 0; y < GameDescription.YMAX; ++y) {
                setState(KPoint.getInstance(x, y), FState.VALID);
            }
        }
	}
	
	
	FState getState(IPoint p) {
		return field[p.getX() + MAXSHIP][p.getY() + MAXSHIP];
	}
	
	void setState(KPoint p, FState s) {
		field[p.x + MAXSHIP][p.y + MAXSHIP] = s;
	}

	double[][] variants;
	
	int getLeftCount(int size) {
		return shipsLeft[size - 1];
	}
	
	boolean updateVariantsForShip(KShipDescription sd) {
		IPoint[] pos = sd.getPositionArray();
		
		if (getState(sd.getStart().next(sd.dir, -1)) == FState.HIT) {
			return false;
		}
		
		if (getState(sd.getEnd().next(sd.dir, 1)) == FState.HIT) {
			return false;
		}		
                
		int hitCount = 0;
		boolean hasValid = false;
		for (IPoint p : pos) {
			FState s = getState(p);
			switch(s) {
				case IGNORE:
				case KILL:
					return false;
				case VALID:
					hasValid = true;
					break;
				case HIT:
					hitCount++;
					break;
			}
		}
                
		if (!hasValid) {
			return false;		
        }
		                
		int count = getLeftCount(sd.getSize());
		for (IPoint p : pos) {
			if (getState(p) == FState.VALID) {
				variants[p.getX()][p.getY()] += count + hitCount * fHit;
			}
		}                
		return true;
	}
			
	boolean buildVariantsForShip(KShipDescription sd) {
		if (getLeftCount(sd.size) == 0) {
			return false;
		}
		
		for (int x = 0; x < GameDescription.XMAX; ++x) {
			for (int y = 0; y < GameDescription.YMAX; ++y) {
				sd.pos = KPoint.getInstance(x, y);
				for (KDirection d : KDirection.half) {
					sd.dir = d;
					if (!updateVariantsForShip(sd)) {
						continue;
					}
				}
			}
		}
		
		return false;
	}	
	
	void buildVariants() {
		variants = new double[GameDescription.XMAX][GameDescription.YMAX];
		
		//Add one point for all valid states
		//Add fNear points around hit states		
		for (int x = 0; x < GameDescription.XMAX; ++x) {
			for (int y = 0; y < GameDescription.YMAX; ++y) {
				KPoint p = KPoint.getInstance(x, y);
				if (getState(p) == FState.VALID) {
					variants[p.x][p.y] += 1.;
				}
				if (getState(p) == FState.HIT) {
					for (KDirection d : KDirection.all) {						
						KPoint pd = p.next(d);
						if (getState(pd) == FState.VALID) {
							variants[pd.x][pd.y] += fNear;
						}
					}
				}
			}
		}
				
		//Search for possible ships in all kinds of position 
		for (int s = 1; s < 4; ++s) {
			KShipDescription sd = new KShipDescription();
			sd.size = s + 1;			
			buildVariantsForShip(sd);
		}
		
		//Blend variants with statistics
/*		for (int x = 0; x < GameDescription.XMAX; ++x) {
			for (int y = 0; y < GameDescription.YMAX; ++y) {
				if (variants[x][y] != 0.) {
					variants[x][y] += sf.getAt(x, y) * 1.;
				}
			}
		}*/		
	}
	
	
	@Override
	public IPoint move() {
		
		buildVariants();

		//Select list of most probable positions from field		
		double maxV = -1;
		IPoint res = null;
		for (int x = 0; x < GameDescription.XMAX; ++x) {
			for (int y = 0; y < GameDescription.YMAX; ++y) {
				if (variants[x][y] < maxV) {
					continue;
				}
				res = KPoint.getInstance(x, y);
				maxV = variants[x][y];				
			}
		}
		
//		KHelper.printField(variants, 1.);
//		System.out.println("next move: " + vp.get(0));
//		System.out.println("---------------");
		
		return res;
	}
	
	void markHit(KPoint p) {
		sf.update(p.x, p.y);
		
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
				if ((getState(dp) != FState.HIT) && (getState(dp) != FState.KILL)) {
					setState(dp, FState.IGNORE);
				}
			}
		}
		setState(p, FState.KILL);		
	}
	
	void registerKill(KPoint p) {
		int dx = 0;
		int dy = 0;
		
		if ((getState(p.move(1,0)) == FState.HIT) 
		 || (getState(p.move(-1,0)) == FState.HIT)) {
			dx = 1;
		}
		
		if ((getState(p.move(0,1)) == FState.HIT) 
		 || (getState(p.move(0,-1)) == FState.HIT)) {
			dy = 1;
		}
		
		if ((dx == 0) && (dy == 0)) {
			markKill(p);
			shipsLeft[0]--;
			return;
		}
		
		int size = 1;
		KPoint pStart = p;
		KPoint dp = pStart.move(-dx, -dy);
		while(getState(dp) == FState.HIT) {
			size++;
			pStart = dp;
			dp = pStart.move(-dx, -dy);			
		}
		
		KPoint pEnd = p;
		dp = pEnd.move(dx, dy);
		while(getState(dp) == FState.HIT) {
			size++;
			pEnd = dp;
			dp = pEnd.move(dx, dy);						
		}
		
		dp = pStart;
		for (int i = 0; i < size; ++i) {
			markKill(dp);
			dp = dp.move(dx, dy);
		}		
		
		shipsLeft[size - 1]--;		
	}
	
	public boolean checkForWin() {
		for (int s : shipsLeft) {
			if (s != 0) {
				return false;
			}
		}
		return true;
	}
	
	boolean win = false;	

	@Override
	public void update(IPoint point, State state) {
		if (win) {
			throw new IllegalStateException("Had won a game!");
		}
		
		KTimer.start("update");
				
		KPoint p = KPoint.getInstance(point.getX(), point.getY());
		
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
				
//		KHelper.printField(field);
//		System.out.println("-----------------");
		
		if (checkForWin()) {
			win = true;				
			KSetupStrategy.winner();			
		}
		
		KTimer.stop("update");
	}
}
