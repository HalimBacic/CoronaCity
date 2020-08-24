package etf.pj2.cc.entity;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import etf.pj2.map.Position;


@SuppressWarnings("serial")
public class Ambulance implements Serializable{

	private Integer capacity;
	private CopyOnWriteArrayList<Person> virusActive = new CopyOnWriteArrayList<Person>();
	private Integer currentStat = 0;
	private Position position;
	private Integer cars;
	private AmbulanceCar car;
	private Integer InfectedPersons=0;
	private Integer InfectedRetiree=0;
	private Integer infectedKids=0;
	private Integer infectedMales=0;
	private Integer infectedFemales=0;
	private Integer numberOfInfected=0;
	private Integer numberOfRecovered=0;
	
	public Integer getInfectedPersons() {
		return InfectedPersons;
	}
	
	public Integer getInfectedRetiree() {
		return InfectedRetiree;
	}


	public Integer getInfectedKids() {
		return infectedKids;
	}


	public Integer getInfectedMales() {
		return infectedMales;
	}


	public Integer getInfectedFemales() {
		return infectedFemales;
	}
	

	public Ambulance(Integer population, Position pos, Integer dimension, Integer cars, Integer fieldsize) {
		
		Random random=new Random();
		Integer percentage=random.nextInt(6)+10;    //Slučajni postotak
		double percentageOfPopulation= (percentage*population)/100.0;  //4.3
		Integer capacity = percentage*population/100;    //4
		if (percentageOfPopulation - capacity > 0.5)
			capacity += 1;
		if(capacity==0)
			capacity++;
		
		this.cars = cars;
		this.capacity = capacity;
		position = pos;
	}
	
	public Integer getCapacity() {
		return capacity;
	}

	public Integer getCurrent() {
		return getCurrentStat();
	}

	public void goCar() {
		cars--;
	}

	public void backCar() {
		cars++;
	}



	public Position getPosition() {
		return position;
	}

	public Integer getCurrentStat() {
		return currentStat;
	}

	public Integer getCarsNum() {
		return cars;
	}

	public AmbulanceCar getAmbulanceCar() {
		return car;
	}




	public Integer getNumberOfInfected() {
		return numberOfInfected;
	}


	public Integer getNumberOfRecovered() {
		return numberOfRecovered;
	}

	public CopyOnWriteArrayList<Person> getVirusActive() {
		return virusActive;
	}

	public void setInfectedMales(Integer infectedMales) {
		this.infectedMales = infectedMales;
	}

	public void setInfectedFemales(Integer infectedFemales) {
		this.infectedFemales = infectedFemales;
	}

	public void setInfectedKids(Integer infectedKids) {
		this.infectedKids = infectedKids;
	}

	public void setInfectedRetiree(Integer infectedRetiree) {
		InfectedRetiree = infectedRetiree;
	}

	public void setInfectedPersons(Integer infectedPersons) {
		InfectedPersons = infectedPersons;
	}

	public void setCurrentStat(Integer currentStat) {
		this.currentStat = currentStat;
	}

	public void setNumberOfInfected(Integer numberOfInfected) {
		this.numberOfInfected = numberOfInfected;
	}

	public void setNumberOfRecovered(Integer numberOfRecovered) {
		this.numberOfRecovered = numberOfRecovered;
	}
}
