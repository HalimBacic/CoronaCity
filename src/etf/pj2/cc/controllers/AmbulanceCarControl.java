package etf.pj2.cc.controllers;

import etf.pj2.cc.entity.AmbulanceCar;
import etf.pj2.cc.view.AmbulanceCarView;

public class AmbulanceCarControl extends Thread {
	
	AmbulanceCarView carView;
	AmbulanceCar car;
	PersonControl personControl;
	
	public AmbulanceCarControl(AmbulanceCar car, AmbulanceCarView carView,PersonControl person) {
             this.car=car;
             this.carView=carView;
             this.personControl=person;
	}
	
	@Override
	public void run() {
             if(car.getPositionPatient().getX()>car.getPositionCar().getX())
            	 carView.moveCarX(+(car.getPositionPatient().getX()-car.getPositionCar().getX())*carView.getFieldsize());
             else if(car.getPositionPatient().getX()<car.getPositionCar().getX())
            	 carView.moveCarX(-(car.getPositionCar().getX()-car.getPositionPatient().getX())*carView.getFieldsize());
             
             if(car.getPositionPatient().getY()>car.getPositionCar().getY())
            	 carView.moveCarY(+(car.getPositionPatient().getY()-car.getPositionCar().getY())*carView.getFieldsize());
             else if(car.getPositionPatient().getY()<car.getPositionCar().getY())
            	 carView.moveCarY(-(car.getPositionCar().getY()-car.getPositionPatient().getY())*carView.getFieldsize()); 
             
             

            carView.getPersonView().getGfxGroup().setVisible(false);
            personControl.goHome(1.0);
            
             if(car.getPositionPatient().getY()>car.getPositionCar().getY())
            	 carView.moveCarY(-(car.getPositionPatient().getY()-car.getPositionCar().getY())*carView.getFieldsize());
             else if(car.getPositionPatient().getY()<car.getPositionCar().getY())
            	 carView.moveCarY(+(car.getPositionCar().getY()-car.getPositionPatient().getY())*carView.getFieldsize());
             
             if(car.getPositionPatient().getX()>car.getPositionCar().getX())
            	 carView.moveCarX(-(car.getPositionPatient().getX()-car.getPositionCar().getX())*carView.getFieldsize());
             else if(car.getPositionPatient().getX()<car.getPositionCar().getX())
            	 carView.moveCarX(+(car.getPositionCar().getX()-car.getPositionPatient().getX())*carView.getFieldsize()); 
             
             carView.getGfx().setVisible(false);
	}
	
	

}
