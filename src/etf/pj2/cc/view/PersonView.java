package etf.pj2.cc.view;

import java.util.logging.Level;

import etf.pj2.cc.entity.Person;
import etf.pj2.cc.exceptions.CCLogger;
import etf.pj2.map.Position;
import interfaces.Movements;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class PersonView implements Movements{
	
	protected Circle circle=new Circle();  
	protected TextField info=new TextField();
	protected Group groupUser = new Group();
	protected Color color;
	protected Integer fieldsize;
	protected Position position;

	public PersonView(Integer fieldSize,Position position)
	{
		circle.setStroke(Color.BLACK);
		circle.setVisible(false);
		info.setEditable(false);
		info.setLayoutX(position.getX() * fieldSize - 107);
		info.setLayoutY(position.getY() * fieldSize - 7);
		info.setVisible(false);
		info.getStyleClass().add("textField");	
		this.fieldsize=fieldSize;
		this.position=position;
	}
		
	public PersonView() {

	}

	public Color getColor()
	{
		return color;
	}
	
	public Circle getCircle()
	{
		return circle;
	}
	
	public TextField getTextField()
	{
		return info;
	}
	
	public Group addToGroup()
	{
		groupUser.getChildren().addAll(info,circle);
		return groupUser;
	}
	
	public Group getGfxGroup() {
		return groupUser;
	}
	
	public void setVisible(boolean status)
	{
		circle.setVisible(status);
		info.setVisible(status);
	}
	
	@Override
	public void animateTopDown(Integer dir, Double speed,Person person) {
		TranslateTransition transition = new TranslateTransition(Duration.seconds(speed), groupUser);
		info.setVisible(true);
		circle.setVisible(true);
		transition.setDelay(Duration.seconds(2));
		if (dir > 0) // Up
		{
			info.setText(infoText(person) + "_up");
			info.setVisible(true);
			transition.setByY(-person.getCity().getFieldSize());

		}
		if (dir < 0) // Down
		{
			info.setText(infoText(person) + "_down");
			info.setVisible(true);			
			transition.setByY(+person.getCity().getFieldSize());
		}
		transition.play();
		
	}


	@Override
	public void animateLeftRight(Integer dir, Double speed,Person person) {
		TranslateTransition transition = new TranslateTransition(Duration.seconds(speed), groupUser);
		info.setVisible(true);
		circle.setVisible(true);
		transition.setDelay(Duration.seconds(2));
		if (dir > 0) {
			info.setText(infoText(person) + "_left");
			info.setVisible(true);
			transition.setByX(-person.getCity().getFieldSize());
		}
		if (dir < 0) {
			info.setText(infoText(person) + "_right");
			info.setVisible(true);
			transition.setByX(+person.getCity().getFieldSize());
		}
		transition.play();
		
		
	}
	
	public void animateHomeX(Integer direction, Double speed)
	{
		TranslateTransition transition=new TranslateTransition(Duration.millis(speed), groupUser);
		transition.setByX(direction);
		transition.play();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			CCLogger.cclogger(getClass().getName(), Level.WARNING, e);
		}
	}
	
    
	public void animateHomeY(Integer direction,Double speed)
	{
		TranslateTransition transition=new TranslateTransition(Duration.millis(speed), groupUser);
		transition.setByY(direction);
		transition.play();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			CCLogger.cclogger(getClass().getName(), Level.WARNING, e);
		}
	}
	
	public String infoText(Person person) {
		return person.getNamePerson() + person.getSurname() + person.getID() + "(" + person.getPosition().toString() + ")";
	}
	
	public void setColor(Color color)
	{
		this.color=color;
	}

}
