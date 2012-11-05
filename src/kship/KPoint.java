package kship;

import model.IPoint;

class KPoint implements IPoint, Comparable<KPoint> {

	static KPoint[][] cache = new KPoint[20][20];	
	static {
		for (int x = -5; x < 15; ++x) {
			for (int y = -5; y < 15; ++y) {
				cache[x + 5][y + 5] = new KPoint(x, y);
			}
		}
	}
	
	public static KPoint getInstance(int x, int y) {
		return cache[x + 5][y + 5];
	}
	
	
	private KPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public final int x;
	public final int y;

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}
	
	KPoint move(KPoint d) {
		return move(d.x, d.y);
	}
	
	KPoint move(int dx, int dy) {
		return getInstance(x + dx, y + dy);
	}	
	
	KPoint random(KPoint end) {
		return getInstance(
			(int)(x + (end.x - x)*Math.random()),
			(int)(y + (end.y - y)*Math.random()));
	}
	
	KPoint next(KDirection d) {
		return move(d.delta);
	}
	
	KPoint next(KDirection d, int steps) {
		return move(d.delta.x*steps, d.delta.y*steps);
	}
	
	@Override
	public String toString() {	
		return x + ":" + y;
	}

	@Override
	public int compareTo(KPoint o) {		
		int cmp = x - o.x;
		return (cmp != 0)?cmp:(y - o.y);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof KPoint) {
			KPoint p = (KPoint)obj;
			return (p.x == x) && (p.y == y);
		}		
		return super.equals(obj);
	}
}