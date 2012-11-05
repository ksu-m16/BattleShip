package kship;

import java.util.Arrays;
import java.util.List;

import model.IPoint;
import model.IShipDescription;
import battleship.GameDescription;

class KShipDescription implements IShipDescription {
	
	public KShipDescription() {
		pos = KPoint.getInstance(0, 0);
	}
	
	KPoint pos;
	KDirection dir;
	int size;
	
	
	private static KPoint positionCache[][][][][] = new KPoint[4][4][][][];
	static {
		for (int size = 0; size < 4; ++size) {
			for (int dir = 0; dir < 4; ++dir) {
				positionCache[size][dir] = makePositionCache(size, dir);
			}			
		}
	}
	
	private static KPoint[][][] makePositionCache(int size, int dir) {
		KPoint field[][][] = new KPoint[GameDescription.XMAX][GameDescription.YMAX][];
		for (int x = 0; x < GameDescription.XMAX; ++x) {
			for (int y = 0; y < GameDescription.YMAX; ++y) {
				field[x][y] = makePosition(KPoint.getInstance(x, y), 
					KDirection.fromIndex(dir), size);
			}
		}
		return field;
	}
	
	private static KPoint[] makePosition(KPoint pos, KDirection dir, int size) {
		KPoint[] list = new KPoint[size + 1];		
		KPoint p = pos;
		for(int i = 0; i < size + 1; ++i) {
			list[i] = p;
			p = p.next(dir);
		}		
		return list;		
	}
	
	public KPoint[] getPositionArray() {
		return positionCache[size - 1][dir.index][pos.x][pos.y];
	}

	@Override
	public List<IPoint> getPosition() {
		return Arrays.asList((IPoint[])positionCache[size - 1][dir.index][pos.x][pos.y]);
	}
	
	public KPoint getStart() {
		return pos;
	}
	
	public KPoint getEnd() {
		return pos.next(dir, size - 1);
	}

	@Override
	public int getSize() {
		return size;
	}	
}
