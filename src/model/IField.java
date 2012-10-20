package model;

import model.Field.State;

public interface IField {
	State getState(int x, int y);

	void setState(int x, int y, State ship);

	// delete this method after debug
	void printField();
}
