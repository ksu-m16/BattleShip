package battleship;

import java.util.List;

import model.IFieldModel;
import model.IShipDescription;
import model.ShipDescription;

public class SetupController {

	private IFieldModel fieldModel1;
	private IFieldModel fieldModel2;
	private ISetupStrategy ss1;
	
	public ISetupStrategy getSs1() {
		return ss1;
	}

	public ISetupStrategy getSs2() {
		return ss2;
	}

	private ISetupStrategy ss2;

	public IFieldModel getFieldModel1() {
		return fieldModel1;
	}

	public IFieldModel getFieldModel2() {
		return fieldModel2;
	}
	
	public void setFieldModel1(IFieldModel fieldModel1) {
		this.fieldModel1 = fieldModel1;
	}

	public void setFieldModel2(IFieldModel fieldModel2) {
		this.fieldModel2 = fieldModel2;
	}

	public void setSetupStrategy1(ISetupStrategy ss) {
		this.ss1 = ss;
	}
	
	public void setSetupStrategy2(ISetupStrategy ss) {
		this.ss2 = ss;
	}

	public ISetupStrategy getSetupStrategy1() {
		return ss1;
	}

	public ISetupStrategy getSetupStrategy2() {
		return ss2;
	}
	
	public List<IShipDescription> getShips(ISetupStrategy ss) {
		return ss.getShips();
	}
	

}
