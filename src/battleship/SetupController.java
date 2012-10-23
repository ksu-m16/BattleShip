package battleship;

import java.util.List;

import model.IFieldModel;
import model.Ship;

public class SetupController {

	private IFieldModel fieldModel1;
	private IFieldModel fieldModel2;
	private ISetupStrategy ss;

	IFieldModel getFieldModel1(){
		return fieldModel1;
	}
	IFieldModel getFieldModel2(){
		return fieldModel2;
	}
	
	public void setSetupStrategy(ISetupStrategy s) {
		this.ss = s;
	}

	public List<Ship> getShips() {
		return ss.getShips();
	}
}
