package kship;

import model.IPoint;
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
	
	static void printField(double[][] field, double factor) {
		for (double[] line : field) {
			for(double cell : line) {
				System.out.print(String.format("%02.02f ", cell * factor));				
			}
			System.out.println();
		}					
	}
	
	static boolean isInField(IPoint p) {
		if ((p.getX() < 0) || (p.getY() < 0) 
		 || (p.getX() >= GameDescription.XMAX) || (p.getY() >= GameDescription.YMAX)) {
			return false;
		}		
		return true;
	}		
}