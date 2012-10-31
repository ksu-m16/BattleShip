package kship;

import battleship.GameDescription;

class KHelper {
	static final KPoint p0 = new KPoint(0, 0);
	static final KPoint pMax = new KPoint(GameDescription.XMAX, GameDescription.YMAX);
	
	static void printField(Object[][] field) { 
		for (Object[] line : field) {
			for (Object cell : line) {
				System.out.print(cell);
			}
			System.out.println();
		}
	}
	
	static boolean isInField(KPoint p) {
		if ((p.x < 0) || (p.y < 0) 
		 || (p.x >= GameDescription.XMAX) || (p.y >= GameDescription.YMAX)) {
			return false;
		}		
		return true;
	}	
}