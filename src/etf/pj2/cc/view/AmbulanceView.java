package etf.pj2.cc.view;

import etf.pj2.cc.simulation.ControlerControlGUI;
import javafx.scene.Group;

public class AmbulanceView {
	

	private Group groupCity;
	private ControlerControlGUI controlerControlGUI;

	public AmbulanceView(Group group)
	{
		this.setGroupCity(group);
	}
	
	public void setController(ControlerControlGUI controlGUI)
	{
		this.controlerControlGUI=controlGUI;
	}

	public ControlerControlGUI getControlerControlGUI() {
		return controlerControlGUI;
	}

	public void setControlerControlGUI(ControlerControlGUI controlerControlGUI) {
		this.controlerControlGUI = controlerControlGUI;
	}

	public Group getGroupCity() {
		return groupCity;
	}

	public void setGroupCity(Group groupCity) {
		this.groupCity = groupCity;
	}
	
}
