package etf.pj2.cc.entity;

import java.io.Serializable;

import etf.pj2.map.Position;


@SuppressWarnings("serial")
public class AmbulanceCar implements Serializable {

	private Position positionCar;
	private Position positionPatient;
	private Person patientPerson;
	
	public AmbulanceCar(Position posCar) {
		setPositionCar(posCar);
	}
	
	public void sendCar(Person patientPerson)  
	{
		this.setPatientPerson(patientPerson);
		setPositionPatient(patientPerson.getPosition());
	}

	public Position getPositionPatient() {
		return positionPatient;
	}

	public void setPositionPatient(Position positionPatient) {
		this.positionPatient = positionPatient;
	}

	public Person getPatientPerson() {
		return patientPerson;
	}

	public void setPatientPerson(Person patientPerson) {
		this.patientPerson = patientPerson;
	}

	public Position getPositionCar() {
		return positionCar;
	}

	public void setPositionCar(Position positionCar) {
		this.positionCar = positionCar;
	}
}
