package model;

import java.util.List;

import model.State;

public interface IFieldModel{

	State getState(int x, int y); // ���� ��������� ������ ����

	State attack(int x, int y); // �������� � xy � ������ ��������� ��������))

	List<? extends IShipState> getShipStates(); // ������ ������ �������� � ����� �
										// ������������.
	// void setState(int x, int y, State ship);

	// delete this method after debug
	void printField();
}
