package model;

import java.util.List;

import model.State;

public interface IFieldModel {

	State getState(int x, int y); 

	State attack(int x, int y); 

	List<? extends IShipState> getShipStates(); 

	// delete this method after debug
	void printField();
}
