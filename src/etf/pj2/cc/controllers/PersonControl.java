package etf.pj2.cc.controllers;

import java.util.Random;
import java.util.Timer;
import java.util.logging.Level;

import etf.pj2.cc.entity.Alarm;
import etf.pj2.cc.entity.Person;
import etf.pj2.cc.entity.Watchman;
import etf.pj2.cc.exceptions.CCLogger;
import etf.pj2.cc.simulation.SimulateTemperature;
import etf.pj2.cc.view.PersonView;
import etf.pj2.map.Field;
import interfaces.MovementOperations;
import interfaces.Types;
import interfaces.WorkEntityInterface;
import javafx.scene.paint.Color;

public class PersonControl extends Thread implements WorkEntityInterface,MovementOperations {

	
	PersonView personView;
	Person person;
	Integer startOrStop=1;
	Integer pause=0;
	private Integer radius;
	private Double speed;
	private Timer temperatureTimer = new Timer();
    private SimulateTemperature simTemperature = new SimulateTemperature();
	
	public PersonControl(PersonView personview, Person person) {
		this.personView=personview;
		this.person=person;
	}
	

	public void setPersonControl(PersonControl personControl) {
		this.person=personControl.getPerson();
		this.personView=personControl.getPersonView();
		this.startOrStop=personControl.startOrStop;
		this.radius=personControl.getRadius();
		this.speed=personControl.getSpeed();	
		System.out.println("");
	}
	
	public PersonControl() {
	}


	public PersonView getPersonView() {
		return personView;
	}

	public Person getPerson() {
		return person;
	}
	

	@Override
	public void run()
	{

		Random rand = new Random();
		
		Integer delay1=rand.nextInt(1000);
		Integer delay2=rand.nextInt(2000);
		
		try {
			Thread.sleep(delay1+delay2);
		} catch (InterruptedException e) {
			CCLogger.cclogger(getClass().getName(), Level.WARNING, e);
		}
		
		getTemperatureTimer().schedule(getSimTemperature(), 0, 3000);
		try {
			
			while (getStartOrStop()!=0) {
			if(pause==0) {
				if (!person.getIll()) {
					personView.getCircle().setFill(personView.getColor());
					Thread.sleep(500);
					if (checkTemperature()) {
						isolatePerson();
					}
					if (person.getHouse().getStatus())
					{
					synchronized(this) {
						person.unlockCity(person.getPosition().getX(),person.getPosition().getY());	
						goHome(speed);
						wait();
						}
					}

					Integer side = rand.nextInt(5);
					System.out.println(person.getNamePerson()+"  "+side);
					if ((checkSide(side) || checkOposite(side))) {
						System.out.println("Moving...");
						personView.getGfxGroup().setVisible(true);
						processSide(side);
					} else {
						personView.getTextField().setVisible(false);
					}
				}
				Thread.sleep(4000);
			 }
			else 
				Thread.sleep(1500);	
		  }
		} catch (InterruptedException e1) {
			CCLogger.cclogger(ActivePersonControl.class.getName(), Level.WARNING, e1);
		}
	}
	
	public void processSide(Integer side) throws InterruptedException {
		if (side == 1)
			moveUp();
		if (side == 3)
			moveDown();
		if (side == 2)
			moveRight();
		if (side == 4)
			moveLeft();
	}

	@Override
	public void moveUp() throws InterruptedException {
		Random random = new Random();
		Integer randomSleep = random.nextInt(1000) + 1500;
		relaiseRow(person.getPosition().getX() - 1, person.getPosition().getY() + 1);
		person.setPosition(person.getPosition().getX(), person.getPosition().getY() - 1);

		Integer step1 = 3;
		Integer step2 = 3;
		Integer x, y;
		x = person.getPosition().getX() - 1;
		y = person.getPosition().getY() - 1;
		
		if (x < 0) {
			step1 += x;
			x = 0;
		}
		if (y < 0) {
			step2 += y;
			y = 0;
		}
		if (x + step1 > person.getCity().getDimension() - 1) {
			step1 = x+step1 - person.getCity().getDimension();
		}
		if (y + step2 > person.getCity().getDimension() - 1) {
			step2 = y+step2 - person.getCity().getDimension();
		}
		person.lockCity(x, y, step1, step2);  

		personView.animateTopDown(1,speed, person);
		
		Thread.sleep(500);
		Thread.sleep(randomSleep);
	}

	@Override
	public void moveDown() throws InterruptedException {
		Random random = new Random();
		Integer randomSleep = random.nextInt(1000) + 1000;
		relaiseRow(person.getPosition().getX() - 1, person.getPosition().getY() - 1);
		person.setPosition(person.getPosition().getX(), person.getPosition().getY() + 1);
		
		
		Integer step1 = 3;
		Integer step2 = 3;
		Integer x, y;
		x = person.getPosition().getX() - 1;
		y = person.getPosition().getY() - 1;
		
		if (x < 0) {
			step1 += x;
			x = 0;
		}
		if (y < 0) {
			step2 += y;
			y = 0;
		}
		if (x + step1 > person.getCity().getDimension() - 1) {
			step1 = x+step1 - person.getCity().getDimension();
		}
		if (y + step2 > person.getCity().getDimension() - 1) {
			step2 = y+step2 - person.getCity().getDimension();
		}
		person.lockCity(x, y, step1, step2);  
		
		
		
		personView.animateTopDown(-1, speed, person);
		Thread.sleep(500);
		Thread.sleep(randomSleep);
	}

	@Override
	public void moveLeft() throws InterruptedException {
		Random random = new Random();
		Integer randomSleep = random.nextInt(1000) + 1000;
		relaiseColumn(person.getPosition().getX() + 1, person.getPosition().getY() - 1);
		person.setPosition(person.getPosition().getX() - 1, person.getPosition().getY());
		
		
		Integer step1 = 3;
		Integer step2 = 3;
		Integer x, y;
		x = person.getPosition().getX() - 1;
		y = person.getPosition().getY() - 1;
		
		if (x < 0) {
			step1 += x;
			x = 0;
		}
		if (y < 0) {
			step2 += y;
			y = 0;
		}
		if (x + step1 > person.getCity().getDimension() - 1) {
			step1 = x+step1 - person.getCity().getDimension();
		}
		if (y + step2 > person.getCity().getDimension() - 1) {
			step2 = y+step2 - person.getCity().getDimension();
		}
		person.lockCity(x, y, step1, step2);  
		
		personView.animateLeftRight(1, speed, person);
		Thread.sleep(500);
		Thread.sleep(randomSleep);
	}

	@Override
	public void moveRight() throws InterruptedException {
		Random random = new Random();
		Integer randomSleep = random.nextInt(1000) + 1000;
		relaiseColumn(person.getPosition().getX() - 1, person.getPosition().getY() - 1);
		person.setPosition(person.getPosition().getX() + 1, person.getPosition().getY());
		
		
		Integer step1 = 3;
		Integer step2 = 3;
		Integer x, y;
		x = person.getPosition().getX() - 1;
		y = person.getPosition().getY() - 1;
		
		if (x < 0) {
			step1 += x;
			x = 0;
		}
		if (y < 0) {
			step2 += y;
			y = 0;
		}
		if (x + step1 > person.getCity().getDimension() - 1) {
			step1 = x+step1 - person.getCity().getDimension();
		}
		if (y + step2 > person.getCity().getDimension() - 1) {
			step2 = y+step2 - person.getCity().getDimension();
		}
		person.lockCity(x, y, step1, step2);  
		
		
		personView.animateLeftRight(-1, speed, person);
		Thread.sleep(500);
		Thread.sleep(randomSleep);
	}
	
	

	@Override
	public Integer setStartorStop(Integer move) {
		startOrStop=move;
		return getStartOrStop();
	}

	@Override
	public void relaiseRow(Integer x, Integer y) {
		Integer step = 3;
		if (x < 0) {
			x = 0;
			step += x;
		}
		if (y < 0) {
			y = 0;
		}
		if (x + step > person.getCity().getDimension() - 1) {
			step = x+step-person.getCity().getDimension();
		}
		if (y > person.getCity().getDimension() - 1) {
			y = person.getCity().getDimension() - 1;
		}
		for (int i = 0; i < step; i++) {
			if (person.getCity().getField(x + i, y).getType().equals(Types.FREE)
					|| person.getCity().getField(x + i, y).getType().equals(Types.CONTROL_POINT)) {
				person.getCity().getField(x + i, y).setAccess(0);
			}
		}
		
	}

	@Override
	public void relaiseColumn(Integer x, Integer y) {
		Integer step = 3;
		if (y < 0) {
			y = 0;
			step += y;
		}
		if (x < 0)
			x = 0;
		if (y + step > person.getCity().getDimension() - 1) {
			step = y+step-person.getCity().getDimension();
		}
		for (int i = 0; i < step; i++) {
			if (person.getCity().getField(x, y + i).getType().equals(Types.FREE)
					|| person.getCity().getField(x + i, y).getType().equals(Types.CONTROL_POINT)) {
				person.getCity().getField(x, y + i).setAccess(0);
			}
		}
		
	}

	public boolean checkDist(Integer x, Integer y) {
		if (distance(x, y) <= getRadius())
			return true;
		else
			System.out.println("Short distance..."); 
			return false;
	}

	private Double distance(Integer x, Integer y) {
		Double first = (double) (x - person.getHousePosition().getX());
		Double second = (double) (y - person.getHousePosition().getY());
		return Math.sqrt(Math.pow(first, 2) + Math.pow(second, 2));
	}

	public boolean checkField(Integer x, Integer y) {
		if (x < 0 || x > person.getCity().getDimension() || y < 0 || y > person.getCity().getDimension())
			return false;
		Field field = person.getCity().getField(x, y);
		if (field.isAccessible(person.getHouseID()) && checkDist(x, y))
			return true;
		else
			return false;
	}

	public boolean checkOposite(Integer side) {
		
		if (side == 1)
			if (person.getPosition().getY() + getRadius() > person.getCity().getDimension()) {
				setRadius(getRadius() + (person.getPosition().getY() + getRadius() - person.getCity().getDimension()));
				return checkDist(person.getPosition().getX(), person.getPosition().getY() - 1);
			}
		if (side == 3)
			if (person.getPosition().getY() - getRadius() < 0) {
				setRadius(getRadius() + (getRadius() - person.getPosition().getY()));
				return checkDist(person.getPosition().getX(), person.getPosition().getY() + 1);
			}
		if (side == 4)
			if (person.getPosition().getX() + getRadius() > person.getCity().getDimension()) {
				setRadius(getRadius() + (person.getPosition().getX() + getRadius() - person.getCity().getDimension()));
				return checkDist(person.getPosition().getX() - 1, person.getPosition().getY());
			}
		if (side == 2)
			if (person.getPosition().getX() - getRadius() < 0) {
				setRadius(getRadius() + (getRadius() - person.getPosition().getX()));
				return checkDist(person.getPosition().getX() + 1, person.getPosition().getY());
			}
		return false;
	}
	
	
	public synchronized boolean  checkSide(Integer side) {
		
		Integer x = person.getPosition().getX();
		Integer y = person.getPosition().getY();
		Integer step=3;
		Integer goOrStop=0;
		//TODO Tekst
		if (side == 1) {
			if(person.getPosition().getY()==0)
			{ System.out.println("Not moving, out of matrix");	return false; }
			x-=1; y-=2;
			if(x<0)
			{ step+=x;	x=0;  }
			if(y<0)
			{ y=0; }
			if(x+step>person.getCity().getDimension()-1) step=x+step-person.getCity().getDimension()-1;
			for(int i=0; i<step; i++)
			if(checkField(person.getCity().getField(x+i, y).getX(), person.getCity().getField(x+i, y).getY()))
				goOrStop++;
			if(goOrStop<3) {  System.out.println("Field occupyed"); return false; }
		}
		if (side == 3 && person.getPosition().getY() < person.getCity().getDimension() - 1) {
			if(person.getPosition().getY() > person.getCity().getDimension() - 1)
				return false;
			x-=1; y+=2;
			if(x < 0)
			{ step+=x;	x=0;  }
			if(y > person.getCity().getDimension()-1)
			{ y=person.getCity().getDimension(); }
			if(x+step>person.getCity().getDimension()) step=x+step-person.getCity().getDimension()-1;
			for(int i=0; i<step; i++)
				//TODO Izbacuje exception, provjeriti je li OK
			if(checkField(person.getCity().getField(x+i, y).getX(), person.getCity().getField(x+i, y).getY()))
				goOrStop++;
			if(goOrStop<3) return false;
		}
		if (side == 2 && person.getPosition().getX() < person.getCity().getDimension() - 1) {
			if(person.getPosition().getX() > person.getCity().getDimension() - 1)
				return false;
			x+=2; y-=1;
			if(x>person.getCity().getDimension())
			{ x=person.getCity().getDimension();  }
			if(y<0)
			{ step+=y; y=0; }
			if(y+step>person.getCity().getDimension()) step=y+step-person.getCity().getDimension()-1;
			for(int i=0; i<step; i++)
			if(checkField(person.getCity().getField(x, y+i).getX(), person.getCity().getField(x, y+i).getY()))
				goOrStop++;
			if(goOrStop<3) return false;
		}
		if (side == 4 && person.getPosition().getX() > 0) {
			if(person.getPosition().getX() <= 0)
				return false;
			x-=2; y-=1;
			if(x<0)
			{ x=0;  }
			if(y<0)
			{ step+=y; y=0; }
			if(y+step>person.getCity().getDimension()) step=y+step-person.getCity().getDimension()-1;
			for(int i=0; i<step; i++)
			if(checkField(person.getCity().getField(x, y+i).getX(), person.getCity().getField(x, y+i).getY()))
				goOrStop++;
			if(goOrStop<3) return false;
		} 	
		return true;
	}
	
	public void goHome(Double speed)
	{
		person.unlockCity(person.getPosition().getX(), person.getPosition().getY());
		
		if(person.getPosition().getX()>person.getHousePosition().getX())
       	 personView.animateHomeX(-(person.getPosition().getX()-person.getHousePosition().getX())*person.getCity().getFieldSize(), speed);
        else if(person.getPosition().getX()<person.getHousePosition().getX())
        personView.animateHomeX(+(person.getHousePosition().getX()-person.getPosition().getX())*person.getCity().getFieldSize(), speed);
        
        if(person.getPosition().getY()>person.getHousePosition().getY())
       	 personView.animateHomeY(-(person.getPosition().getY()-person.getHousePosition().getY())*person.getCity().getFieldSize(), speed);
        else if(person.getPosition().getY()<person.getHousePosition().getY())
       	 personView.animateHomeY(+(person.getHousePosition().getY()-person.getPosition().getY())*person.getCity().getFieldSize(), speed);  
        
      
         personView.getGfxGroup().setVisible(false);
	}
	
	public void isolatePerson()
	{
		person.setIll(true);
		callAmbulance();	
	}
	
	
	public void callAmbulance()
	{
		person.setIll(true);
		personView.getCircle().setFill(Color.RED);
		Alarm alarm=new Alarm(person.getPosition(), this);
		
		
			Watchman.addAlarm(alarm);
		
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Integer getStartOrStop() {
		return startOrStop;
	}
	
	public Timer getTemperatureTimer() {
		return temperatureTimer;
	}

	public void setTemperatureTimer(Timer temperatureTimer) {
		this.temperatureTimer = temperatureTimer;
	}

	public SimulateTemperature getSimTemperature() {
		return simTemperature;
	}

	public void setSimTemperature(SimulateTemperature simTemperature) {
		this.simTemperature = simTemperature;
	}
	
	public Float getTemperature()
	{
		return getSimTemperature().getTemp();
	}
	
	public boolean checkTemperature(){     

	       Integer x=person.getPosition().getX()-1;
	       if(x<0) x=0;
	       
	       Integer y=person.getPosition().getY()-1;
	       if(y<0) y=0;
	       
	       Integer step1=2;
	       Integer step2=2;
		   
	       if(x+step1>person.getCity().getDimension()-1) step1--;
	       if(y+step2>person.getCity().getDimension()-1) step2--;

			for (Integer i=x; i < person.getPosition().getX()-1+step1; i++)
				for (Integer j=person.getPosition().getY()-1; j < person.getPosition().getY()+step2; j++) {
					if (person.getCity().getField(i, j).isControlPoint()) {
						if (getSimTemperature().getTemp() > 37) {
							person.unlockCity(person.getPosition().getX(),person.getPosition().getY());
							return true;
						}
					}
				}
			return false;
		}


	@Override
	public Integer setPause(Integer pause) {
		this.pause=pause;
		return pause;
	}
}
