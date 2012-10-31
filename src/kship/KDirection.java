package kship;

enum KDirection {
	LEFT(-1, 0), UP(0, -1), RIGHT(1, 0), DOWN(0, 1);
	
	KDirection(int dx, int dy) {
		delta = new KPoint(dx, dy);
	}
	
	static KDirection random() {
		double r = Math.random();
		if (r < 0.25) return LEFT;
		if (r < 0.5) return UP;
		if (r < 0.75) return RIGHT;
		return DOWN;
	}
	
	static KDirection hRandom() {
		return (Math.random() < 0.5)?LEFT:RIGHT;
	}
	
	static KDirection vRandom() {
		return (Math.random() < 0.5)?UP:DOWN;
	}	
	
	static KDirection hvRandom(double f) {
		return (Math.random() < f)?hRandom():vRandom();
	}

	final KPoint delta;
}