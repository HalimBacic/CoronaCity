package etf.pj2.cc.view;

import java.util.logging.Level;

import etf.pj2.cc.controllers.PersonControl;
import etf.pj2.cc.entity.Person;
import etf.pj2.cc.exceptions.CCLogger;
import etf.pj2.map.Position;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class AmbulanceCarView {

	private Rectangle ambulanceCar = new Rectangle();
	private Integer fieldsize;
	Position carPosition; 
	PersonControl personControl;
	
	public AmbulanceCarView(Integer fieldsize, Position position,PersonControl patientControl)
	{
		this.fieldsize=fieldsize;
		carPosition = position;
		ambulanceCar.setX(fieldsize * carPosition.getX());
		ambulanceCar.setY(fieldsize * carPosition.getY());
		ambulanceCar.setHeight(fieldsize/5);
		ambulanceCar.setWidth(fieldsize/5);
		ambulanceCar.setFill(Color.WHITE);
		ambulanceCar.setStroke(Color.RED);
		ambulanceCar.setStrokeWidth(4);
		this.personControl=patientControl;
	}
	
	public Person getPerson()
	{
		return personControl.getPerson();
	}
	
	public PersonView getPersonView()
	{
		return personControl.getPersonView();
	}

	public void moveCarX(Integer direction) {
		TranslateTransition transition = new TranslateTransition(Duration.millis(getFieldsize() * 17), ambulanceCar);
		transition.setByX(direction);
		transition.play();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			CCLogger.cclogger(getClass().getName(), Level.WARNING, e);
		}
	}

	public void moveCarY(Integer direction) {
		TranslateTransition transition = new TranslateTransition(Duration.millis(getFieldsize() * 17), ambulanceCar);
		transition.setByY(direction);
		transition.play();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			CCLogger.cclogger(getClass().getName(), Level.WARNING, e);
		}
	}

	public Rectangle getGfx() {
		return ambulanceCar;
	}

	public Integer getFieldsize() {
		return fieldsize;
	}

}
