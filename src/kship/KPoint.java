package kship;

import model.IPoint;

class KPoint implements IPoint {
	public KPoint(int x, int y) {
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
		return new KPoint(x + dx, y + dy);
	}	
	
	KPoint random(KPoint end) {
		return new KPoint(
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
}