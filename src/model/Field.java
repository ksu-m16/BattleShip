package model;

import java.util.Arrays;
import java.util.LinkedList;


public class Field {
	public Field(boolean own) {
		this.own = own;
		fleet = new Ship[10];
		
		{
		int n = 4;
		int nships = 0;
		for(int i = 0; i < fleet.length; i++){
			fleet[i] = new Ship(n);
			nships++;
			if ((nships + n) == 5 ) {
				n--;
				nships = 0;
			}
		}
//		for(int i = 0; i < shipsStatus.length; i++){
//			System.out.println(shipsStatus[i].getSize());
//		}
		}
		
		states = new States[SIZE][SIZE];
		for (int i = 0; i < states[0].length; i++) {
			for (int j = 0; j < states[0].length; j++) {
				states[i][j] = States.EMPTY;
			}
		}
		
	}
	private boolean own;
	public final static int SIZE = 10;
	public enum States {
		EMPTY, SHIP, NEAR_SHIP, HIT, HIT_SHIP
	}
	private States[][] states;
	private Ship[] fleet;
	
	public Ship[] getFleet() {
		return fleet;
	}




	public States getState(int i, int j) {
		if(own) {
			return states[i][j];
		}
		if( states[i][j].equals(States.HIT) || states[i][j].equals(States.HIT_SHIP)){
			return states[i][j];
		}
		return States.EMPTY;
	}

	public void setState(int i, int j, States st) {
		states[i][j] = st;
	}
	
	public void printField(){
		for(int j = 0; j < states.length; j++) {
			for(int i = 0; i < states.length; i++) {
				if (states[i][j].equals(States.EMPTY)) {
					System.out.print("0");
				}
				if (states[i][j].equals(States.SHIP)) {
					System.out.print("P");
				}
				if (states[i][j].equals(States.HIT)) {
					System.out.print(":");
				}
				if (states[i][j].equals(States.HIT_SHIP)) {
					System.out.print("X");
				}
				if (states[i][j].equals(States.NEAR_SHIP)) {
					System.out.print("?");
				}
			}
			System.out.println();
		}
	}
		
}
	
	
