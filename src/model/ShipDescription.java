package model;

import java.util.ArrayList;

public class ShipDescription implements IShipDescription {
	
	public ShipDescription(IPoint position, int size, Course course) {
		this.position = position;
		this.size = size;
		this.course = course;
	}
	
	private IPoint position;
	private int size;
	private Course course;

	public enum Course {
		HORIZONTAL, VERTICAL
	};

	@Override
	public ArrayList<IPoint> getPosition() {
		ArrayList<IPoint> positions = new ArrayList<IPoint>();
		switch (course) {
	case HORIZONTAL: {
		for (int i = 0; i < size; i++) {
			positions.add(new Point(position.getX() + i, position.getY()));
		}
		break;

	}
	case VERTICAL: {
		for (int i = 0; i < size; i++) {
			positions.add(new Point(position.getX(), position.getY() + i));
		}
	}
	}
		return positions;
	}

//	public static ShipDescription createShip(int x, int y, int size,
//			Course course) {
//		ShipDescription s = new ShipDescription();
//		s.position = new ArrayList<IPoint>();
//		// s.size = size;
//		// s.course = course;
//
//		switch (course) {
//		case HORIZONTAL: {
//			for (int i = 0; i < size; i++) {
//				s.position.add(new Point(x + i, y));
//			}
//			break;
//
//		}
//		case VERTICAL: {
//			for (int i = 0; i < size; i++) {
//				s.position.add(new Point(x, y + i));
//			}
//		}
//		}
//
//		return s;
//	}

}
