package battleship;

import model.FieldModel;
import model.IFieldModel;

public class SetupController {

	private ISetupStrategy ss;

	public IFieldModel setup() {
		return FieldModel.getFieldModel(ss.getShips());
	}

	public void setSetupStrategy(ISetupStrategy ss) {
		this.ss = ss;
	}

	public ISetupStrategy getSetupStrategy() {
		return ss;
	}

}
