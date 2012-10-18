package model;


public class Ship {
	Ship(int size) {
		this.size = size;
		state = new Field.States[size];
		for(int i = 0; i < size; i++) {
			state[i] = Field.States.SHIP;
		}
	}
	
	private int[] position;
	private int size;
	private Course course;
	

	public enum Course {HORIZONTAL, VERTICAL};
//	private boolean dead;
//	private boolean hit;
	private Field.States[] state;
	
	public int[] getPosition() {
		return position;
	}

	public void setPosition(int[] position) {
		this.position = position;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Field.States[] getState() {
		return state;
	}

	public void setState(Field.States[] state) {
		this.state = state;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	
	public boolean isHit(){
		for(Field.States st: state) {
			if (st == Field.States.HIT) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isDead(){
		boolean dead = true;
		for(Field.States st: state) {
			dead = dead && (st == Field.States.HIT);
			
		}
		return dead;
	}
	

	
	
//	private enum Size {
//		ONE(1), TWO(2), THREE(3), FOUR(4);
//		private int size;
//		Size(int size) {
//			this.size = size;
//		}
//		
//		}
	
}
