package etf.pj2.cc.entity;

import java.io.Serializable;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Level;
import etf.pj2.cc.controllers.AmbulanceControl;
import etf.pj2.cc.controllers.PersonControl;
import etf.pj2.cc.exceptions.CCLogger;
import etf.pj2.cc.simulation.ControlerControlGUI;
import etf.pj2.map.City;
import etf.pj2.map.Position;
import interfaces.WorkEntityInterface;
import javafx.scene.Group;
import javafx.stage.Stage;

//Ne vrati boju osobi, ne pošalje ga kući, ne nađe dobru ambulantu(ili se vozila loše postave)
@SuppressWarnings("serial")
public class Watchman extends Thread implements Serializable,WorkEntityInterface {
	
	
	private static Stack<Alarm> stack= new Stack<>();
	private ControlerControlGUI controlerControlGUI;
	private Integer startOrStop=1;
	private Integer pause=0;
	private Vector<AmbulanceControl> ambulanceControls;
	
	public void setControler(ControlerControlGUI controlGUI,City city,Vector<PersonControl> persons, Vector<AmbulanceControl> ambulances,
			                 Stage stage,Group groupCity)
	{
		this.ambulanceControls=ambulances;
		controlerControlGUI=controlGUI;
		controlerControlGUI.setElements(ambulances, persons, city);
		controlerControlGUI.setStage(stage);
		controlerControlGUI.setGroupCity(groupCity);
	}
	
	
	@Override
	public void run()
	{
		while(startOrStop!=0)
		{
			if(pause==0) {
			try {
			if(!stack.isEmpty())
				{ 
				  Alarm currentAlarm=stack.pop();
				  controlerControlGUI.settfAI(currentAlarm.toString());
				  AmbulanceControl nearestAmbulance=findNearestAmbulance(currentAlarm.getPosition());

				  controlerControlGUI.setInfectedInfo(currentAlarm.getPerson(), nearestAmbulance);
				  controlerControlGUI.setInfected();
				  
				}
				sleep(3000);
			} catch (InterruptedException e) {
				CCLogger.cclogger(Watchman.class.getName(), Level.INFO, e);
			}	
			}
			else {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
                    CCLogger.cclogger(getClass().getName(), Level.WARNING, e);
			}
		}
		}
	}
	
	public Alarm popAlarm()
	{
		return stack.pop();
	}
	
	public static void addAlarm(Alarm al)
	{
		stack.add(al);
	}
	
	public AmbulanceControl findNearestAmbulance(Position position) {

		AmbulanceControl returnAmbulance=null;
		Double distance = 1000.0;

		Integer testValue=0;
		
		while(testValue==0)
		{
			for (AmbulanceControl ambulance : this.ambulanceControls) {
				Integer xAmbulance=ambulance.getAmbulance().getPosition().getX();
				Integer yAmbulance=ambulance.getAmbulance().getPosition().getY();
				
				Double ambulanceDistance=Math.sqrt(Math.pow(xAmbulance-position.getX(), 2)+Math.pow(yAmbulance-position.getY(), 2));
				if(distance>ambulanceDistance)
				{
					distance = ambulanceDistance;
					returnAmbulance = ambulance;
				}
			}
			
		    if(returnAmbulance.getAmbulance().getCapacity()>returnAmbulance.getAmbulance().getCurrentStat() && returnAmbulance.getAmbulance().getCarsNum()>0)
				testValue=1;
		}
		return returnAmbulance;
		
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
