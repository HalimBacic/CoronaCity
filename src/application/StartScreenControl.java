package application;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;
//import java.util.Random;
import java.util.logging.Level;

import etf.pj2.cc.exceptions.BadCCNumber;
import etf.pj2.cc.exceptions.BadChildsNumber;
import etf.pj2.cc.exceptions.CCLogger;
import etf.pj2.cc.simulation.StartSimulation;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;


@SuppressWarnings("serial")
public class StartScreenControl implements Serializable {

	@FXML
	TextField textfieldHouses;
	@FXML
	TextField textFieldPersons;
	@FXML
	TextField textFieldRetirees;
	@FXML
	TextField textFieldChilds;
	@FXML
	TextField textFieldControls;
	@FXML
	TextField textFieldAmbulances;
	@FXML
	Button btnStart;
	@FXML
	ComboBox<String> screenRes;

	public StartScreenControl() {
	}

	@FXML
	private void initialize() {
	}

	@FXML
	private void clicked() throws IOException, InterruptedException {
		try {
			Random random=new Random();
			
			Integer size = random.nextInt(15)+15;
		    File fileWatcher = new File("FileWatcher.txt");
		    fileWatcher.createNewFile();
			String[] screen=screenRes.getValue().split(" x ");
			Integer screenWidth=Integer.parseInt(screen[1]);
//			Integer size=15;
			Integer houses = Integer.parseInt(textfieldHouses.getText());
			Integer persons = Integer.parseInt(textFieldPersons.getText());
			Integer retiree = Integer.parseInt(textFieldRetirees.getText());
			Integer childs = Integer.parseInt(textFieldChilds.getText());
			Integer controls = Integer.parseInt(textFieldControls.getText());
			Integer ambulanceCars = Integer.parseInt(textFieldAmbulances.getText());
			StartSimulation startSimulation;
			startSimulation = new StartSimulation(screenWidth,size , houses, persons, retiree, childs, controls, ambulanceCars);
			startSimulation.start();
		} catch (NumberFormatException ex) {
			CCLogger.cclogger(StartScreenControl.class.getName(), Level.WARNING, ex);
		} catch (BadCCNumber e) {
			CCLogger.cclogger(StartScreenControl.class.getName(), Level.WARNING, e);
		} catch (IOException ex) {
			CCLogger.cclogger(StartScreenControl.class.getName(), Level.INFO, ex);
		} catch (BadChildsNumber e) {
			CCLogger.cclogger(StartScreenControl.class.getName(), Level.WARNING, e);
		}
	}
}
