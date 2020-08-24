package etf.pj2.cc.simulation;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Vector;
import java.util.logging.Level;

import etf.pj2.cc.controllers.AmbulanceControl;
import etf.pj2.cc.controllers.PersonControl;
import etf.pj2.cc.entity.Ambulance;
import etf.pj2.cc.exceptions.CCLogger;
import etf.pj2.cc.view.AmbulanceView;
import etf.pj2.map.City;
import etf.pj2.map.Position;
import interfaces.Locations;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@SuppressWarnings("serial")
public class NewAmbulanceControl implements Locations,Serializable{
	
	@FXML
	TextField fieldX;
	@FXML
	TextField fieldY;
    @FXML
    TextField fieldCars;
	@FXML
	Button btnNewAmbulance;

	private City city;
	private Group groupCity;
	private Stage stage;
	private Vector<AmbulanceControl> ambulanceControls;
	private Vector<PersonControl> personControls;
	
	public NewAmbulanceControl() {
	}

	@FXML
	private void initialize() {
	}
	
	public void close()
	{
		stage.close();
	}
	
	public void setElements(City city, Group group,Stage stage,Vector<AmbulanceControl> ambulanceControls,Vector<PersonControl> personControls)
	{
		this.city=city;
		this.groupCity=group;
		this.stage=stage;
		this.ambulanceControls=ambulanceControls;
		this.personControls=personControls;
	}
	
	
	public void clicked()
	{
		Integer x=Integer.parseInt(fieldX.getText());
		Integer y=Integer.parseInt(fieldY.getText());
		Integer cars=Integer.parseInt(fieldCars.getText());
		
		Integer population=city.getPersonVector().size();
		
		Position position=new Position(x, y);
		

        Ambulance ambulance = city.setAmbulance(population, position, cars, this.groupCity);
        AmbulanceView ambulanceView=new AmbulanceView(this.groupCity);
        AmbulanceControl ambulanceControl = new AmbulanceControl(ambulance, ambulanceView, personControls);
        
        ambulanceControls.add(ambulanceControl);
		
        try {
			StartSimulation.setImage(Locations.AMBULANCE, x*city.getFieldSize(), y*city.getFieldSize(), city.getFieldSize(), this.groupCity);
		} catch (FileNotFoundException e) {
			CCLogger.cclogger(NewAmbulanceControl.class.getName(), Level.WARNING, e);
		}
		
		close();
	    
	}
	
	public void clickedX()
	{
		fieldY.setText("0");
	}
	
	public void clickedY()
	{
		fieldX.setText("0");
	}
}
