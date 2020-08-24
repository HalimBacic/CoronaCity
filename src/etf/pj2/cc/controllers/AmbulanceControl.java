package etf.pj2.cc.controllers;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import etf.pj2.cc.entity.Ambulance;
import etf.pj2.cc.entity.AmbulanceCar;
import etf.pj2.cc.entity.HealthCard;
import etf.pj2.cc.entity.Person;
import etf.pj2.cc.exceptions.CCLogger;
import etf.pj2.cc.view.AmbulanceCarView;
import etf.pj2.cc.view.AmbulanceView;
import etf.pj2.map.Position;
import interfaces.Types;
import interfaces.WorkEntityInterface;

@SuppressWarnings("serial")
public class AmbulanceControl extends Thread implements Serializable,WorkEntityInterface {

	Ambulance ambulance;
	AmbulanceView ambulanceView;
	Integer startOrStop=1;
	Integer pause=0;
	Vector<PersonControl> allPersonsControls=new Vector<PersonControl>();

	public AmbulanceControl(Ambulance ambulance, AmbulanceView ambulanceView,Vector<PersonControl> personControls) {
		this.ambulance = ambulance;
		this.ambulanceView = ambulanceView;
		this.allPersonsControls=personControls;
	}

	
	
	public Ambulance getAmbulance() {
		return ambulance;
	}



	@Override
	public void run() {
		try {
			while (startOrStop != 0) {
			if(pause==0) {	
				if (!ambulance.getVirusActive().isEmpty()) {
					for (Person person : ambulance.getVirusActive()) {
						for (PersonControl personControl : this.allPersonsControls) {
							if (person.getID() == personControl.getPerson().getID())
								person.getHealthCard().addTemperature(personControl.getTemperature());
						}

					}

					Iterator<Person> iterator = ambulance.getVirusActive().iterator();
					while (iterator.hasNext()) {
						Person person = iterator.next();
						if (person.getHealthCard().healthCardSize() > 3)
							if (person.getHealthCard().lastThree() < 37) {
								leaveAmb(person);
							}
					}
				}
				Thread.sleep(2000);
			}
			else 
				Thread.sleep(1500);
			}
			
		} catch (InterruptedException e) {
			CCLogger.cclogger(getClass().getName(), Level.WARNING, e);

		}
	}
	
	public void sendCarFromAmbulance(Person person) {
		AmbulanceCar car = new AmbulanceCar(ambulance.getPosition());
		car.sendCar(person);
		
		
		PersonControl personControl = new PersonControl();

		for (PersonControl patientControl : allPersonsControls) {
			if(patientControl.getPerson().getID()==person.getID())
				personControl.setPersonControl(patientControl);
		}
		
		AmbulanceCarView carView=new AmbulanceCarView(person.getCity().getFieldSize(), car.getPositionCar(), personControl);
		AmbulanceCarControl carControl = new AmbulanceCarControl(car, carView,personControl);
		
		
		ambulanceView.getGroupCity().getChildren().add(carView.getGfx());
		
		carView.getGfx().setVisible(false);
		ambulance.goCar();
		carView.getGfx().setVisible(true);

		carControl.start();

		ambulance.backCar();
		
		if(person.getGender().equals(Types.MALE))
			ambulance.setInfectedMales(ambulance.getInfectedMales() + 1);
		else if(person.getGender().equals(Types.FEMALE))
			ambulance.setInfectedFemales(ambulance.getInfectedFemales() + 1);
		
		if(person.getAge()<18)
			ambulance.setInfectedKids(ambulance.getInfectedKids() + 1);
		else if(person.getAge()>64)
			ambulance.setInfectedRetiree(ambulance.getInfectedRetiree() + 1);
		else 
			ambulance.setInfectedPersons(ambulance.getInfectedPersons() + 1);
		
		ambulance.setCurrentStat(ambulance.getCurrentStat() + 1);
		person.setHealthCard(new HealthCard());
		person.getHealthCard().addTemperature(personControl.getTemperature());
		ambulance.getVirusActive().add(person);
		ambulance.setNumberOfInfected(ambulance.getNumberOfInfected() + 1);
		person.houseQuarantine(true);
	}
	
	public synchronized void leaveAmb(Person person) {
		Position position = person.getHousePosition();
		synchronized (this) {
			person.getHouse().setStatus(false);
			notify();
		}

		person.setPosition(position.getX(), position.getY());
		person.setIll(false);
		ambulance.getVirusActive().remove(person);
		ambulance.setCurrentStat(ambulance.getCurrentStat() - 1);

		ambulance.setNumberOfRecovered(ambulance.getNumberOfRecovered() + 1);
		ambulanceView.getControlerControlGUI().setRecovered();
	}



	public AmbulanceView getAmbulanceView()
	{
		return ambulanceView;
	}
	
	@Override
	public Integer setStartorStop(Integer move) {
          startOrStop=move;
          return startOrStop;
	}



	@Override
	public Integer setPause(Integer pause) {
		this.pause=pause;
		return pause;
	}
}
