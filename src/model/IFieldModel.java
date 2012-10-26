package model;

import java.util.List;

import model.State;

public interface IFieldModel{

	State getState(int x, int y); // дает состояние ячейки поля

	State attack(int x, int y); // стреляет в xy и отдает состояние отстрела))

	List<? extends IShipState> getShipStates(); // выдает список кораблей с инфой о
										// повреждениях.
	// void setState(int x, int y, State ship);

	// delete this method after debug
	void printField();
}
