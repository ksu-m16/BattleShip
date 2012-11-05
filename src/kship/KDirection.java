package kship;

enum KDirection {
	LEFT(-1, 0, 0), UP(0, -1, 1), RIGHT(1, 0, 2), DOWN(0, 1, 3);
	
	KDirection(int dx, int dy, int idx) {
		delta = KPoint.getInstance(dx, dy);
		index = idx;
	}
	
	
	public final int index;	
	static final KDirection[] all = {LEFT, UP, RIGHT, DOWN};
        static final KDirection[] half = {LEFT, UP};
	
	static KDirection fromIndex(int index) {
		return all[index];
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