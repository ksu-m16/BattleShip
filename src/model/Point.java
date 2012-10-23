package model;

public class Point implements IPoint{
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;		
	}
	
	private int x;
	private int y;
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
}
