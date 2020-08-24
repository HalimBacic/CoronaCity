package etf.pj2.cc.controllers;

import java.util.Random;
import java.util.logging.Level;

import etf.pj2.cc.entity.Person;
import etf.pj2.cc.exceptions.CCLogger;
import etf.pj2.cc.view.ActiveKidView;
import etf.pj2.cc.view.PersonView;

public class ActiveKidControl extends PersonControl {
	
	Person personTest;
	ActiveKidView activePersonView;
	
	public ActiveKidControl(PersonView personview, Person person) {
		super(personview, person);
		setSpeed(1.5);
	}
	
	@Override
	public void run() {

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
					getPersonView().getCircle().setFill(getPersonView().getColor());
					Thread.sleep(500);
					if (checkTemperature()) {
						isolatePerson();
					}
					if (person.getHouse().getStatus())
					{
					synchronized(this) {
						goHome(getSpeed());
						wait();
						}
					}

					Integer side = rand.nextInt(5);
					System.out.println(person.getNamePerson()+"  "+side);
					if ((checkSide(side))) {
						System.out.println("Moving...");
						getPersonView().getGfxGroup().setVisible(true);
						processSide(side);
					} else {
						getPersonView().getTextField().setVisible(false);
					}
				}
				Thread.sleep(4000);
			}
				else {
					Thread.sleep(1500);
				}
			}
		} catch (InterruptedException e1) {
			CCLogger.cclogger(ActivePersonControl.class.getName(), Level.WARNING, e1);
		}
	}
	
	@Override
	public void moveUp() throws InterruptedException {
		Random random = new Random();
		Integer randomSleep = random.nextInt(1000) + 1500;
		person.setPosition(person.getPosition().getX(), person.getPosition().getY() - 1);
		
		getPersonView().animateTopDown(1,getSpeed(), person);
		Thread.sleep(500);
		Thread.sleep(randomSleep);
	}

	@Override
	public void moveDown() throws InterruptedException {
		Random random = new Random();
		Integer randomSleep = random.nextInt(1000) + 1000;
		person.setPosition(person.getPosition().getX(), person.getPosition().getY() + 1);
		getPersonView().animateTopDown(-1, getSpeed(), person);
		Thread.sleep(500);
		Thread.sleep(randomSleep);
	}

	@Override
	public void moveLeft() throws InterruptedException {
		Random random = new Random();
		Integer randomSleep = random.nextInt(1000) + 1000;
		person.setPosition(person.getPosition().getX() - 1, person.getPosition().getY());		
		getPersonView().animateLeftRight(1, getSpeed(), person);
		Thread.sleep(500);
		Thread.sleep(randomSleep);
	}

	@Override
	public void moveRight() throws InterruptedException {
		Random random = new Random();
		Integer randomSleep = random.nextInt(1000) + 1000;
		person.setPosition(person.getPosition().getX() + 1, person.getPosition().getY());
		getPersonView().animateLeftRight(-1, getSpeed(), person);
		Thread.sleep(500);
		Thread.sleep(randomSleep);
	}
	
	@Override
	public boolean checkField(Integer x, Integer y) {
		if (x < 0 || x > person.getCity().getDimension() || y < 0 || y > person.getCity().getDimension())
			return false;
	  return true;
	}
	
	@Override
	public synchronized boolean  checkSide(Integer side) {

		//TODO Tekst
		if (side == 1) {
			if(!checkField(person.getPosition().getX(), person.getPosition().getY()-1))
			{ System.out.println("Not moving, out of matrix");	return false; }
		}
		if (side == 3) {
			if(!checkField(person.getPosition().getX(), person.getPosition().getY()+1))
			{ System.out.println("Not moving, out of matrix");	return false; }
		}
		if (side == 2) {
			if(!checkField(person.getPosition().getX()+1, person.getPosition().getY()))
			{ System.out.println("Not moving, out of matrix");	return false; }
		}
		if (side == 4) {
			if(!checkField(person.getPosition().getX()-1, person.getPosition().getY()))
			{ System.out.println("Not moving, out of matrix");	return false; }
		} 	
		return true;
	}
	
	@Override
	public void goHome(Double speed)
	{	
		if(person.getPosition().getX()>person.getHousePosition().getX())
       	 getPersonView().animateHomeX(-(person.getPosition().getX()-person.getHousePosition().getX())*person.getCity().getFieldSize(), speed);
        else if(person.getPosition().getX()<person.getHousePosition().getX())
        getPersonView().animateHomeX(+(person.getHousePosition().getX()-person.getPosition().getX())*person.getCity().getFieldSize(), speed);
        
        if(person.getPosition().getY()>person.getHousePosition().getY())
       	 getPersonView().animateHomeY(-(person.getPosition().getY()-person.getHousePosition().getY())*person.getCity().getFieldSize(), speed);
        else if(person.getPosition().getY()<person.getHousePosition().getY())
       	 getPersonView().animateHomeY(+(person.getHousePosition().getY()-person.getPosition().getY())*person.getCity().getFieldSize(), speed);  
        
         getPersonView().getGfxGroup().setVisible(false);
	}
}
