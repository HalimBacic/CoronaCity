package etf.pj2.cc.simulation;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;

import javafx.scene.layout.Pane;

public class MessageController {
	@FXML
	private Pane pane;
	@FXML
	private TextField mesage;
	
	public void setMessage(String message)
	{
		mesage.setText(message);
	}
}
