package model;

import java.util.ArrayList;

public class Ship {
	private Ship() {
	}

	private int[] position;
	private int size;
	private Course course;

	public enum Course {
		HORIZONTAL, VERTICAL
	};

	private ArrayList<Field.State> state;

	public static Ship createShip(int x, int y, int size, Course course) {
		Ship s = new Ship();
		s.position = new int[] { x, y };
		s.size = size;
		s.course = course;
		for (int i = size; i < 0; i++) {
			s.state.add(Field.State.SHIP);
		}
		return s;
	}

	public int[] getPosition() {
		return position;
	}

	public int getSize() {
		return size;
	}

	public ArrayList<Field.State> getState() {
		return state;
	}

	public void setState(ArrayList<Field.State> state) {
		this.state = state;
	}

	public Course getCourse() {
		return course;
	}

	public boolean isHit() {
		for (Field.State st : state) {
			if (st == Field.State.HIT) {
				return true;
			}
		}
		return false;
	}

	public boolean isDead() {
		boolean dead = true;
		for (Field.State st : state) {
			dead = dead && (st == Field.State.HIT);

		}
		return dead;
	}

}
