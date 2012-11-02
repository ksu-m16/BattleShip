package kship;

import java.util.LinkedList;
import java.util.List;

import model.IPoint;
import model.IShipDescription;

class KShipDescription implements IShipDescription {
	
	public KShipDescription() {
		pos = KPoint.getInstance(0, 0);
	}
	
	KPoint pos;
	KDirection dir;
	int size;	

	@Override
	public List<IPoint> getPosition() {
		List<IPoint> plist = new LinkedList<IPoint>();
		
		KPoint p = pos;
		for(int i = 0; i < size; ++i) {
			plist.add(p);
			p = p.next(dir);
		}
		
		return plist;
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