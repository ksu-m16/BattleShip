package model;

public class Point implements IPoint, Comparable<IPoint> {

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	private int x;
	private int y;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String toString() {
		return "[" + x + "," + y + "]";
	}

	@Override
	public int compareTo(IPoint p) {
		if (x == p.getX()) {

			if (y == p.getY()) {
				return 0;
			}

			return y > p.getY() ? 1 : (-1);
		}

		return x > p.getX() ? 1 : (-1);

	}
}
