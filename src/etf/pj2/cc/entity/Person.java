package etf.pj2.cc.entity;

import java.io.Serializable;
import etf.pj2.map.City;
import etf.pj2.map.House;
import etf.pj2.map.Position;
import interfaces.Types;


@SuppressWarnings("serial")
public class Person implements Serializable{

	private Integer ID;
	private String name;
	private String surname;
	private Integer age;
	private Integer houseID;
	private Position position = new Position();
	private Position housePosition = new Position();
	private String gender;
	private City city;
	private House house;
    private HealthCard healthCard;
    private boolean ill=false;


	public Person(Integer ID, String name, String surname, Integer age, String gender) {
		this.ID = ID;
		this.name = name;
		this.surname = surname;
		this.setAge(age);
		this.gender = gender;
	}
	
	public void setCity(City city) {
		this.city = city;
	}
	
	
	
	public void setHouseID(House house) {
		this.houseID = house.getID();
        setHouse(house);
	}
	
	public void setHouse(House house)
	{
		this.house=house;
		setPosition(house.getPosition().getX(),house.getPosition().getY());
		setHousePosition(house.getPosition().getX(),house.getPosition().getY());
		System.out.print("");
	} 
	
	private void setHousePosition(Integer x, Integer y) {
		housePosition.setX(x);
		housePosition.setY(y);
	}

	public Position getHousePosition() {
		return housePosition;
	}
	
	public void setPosition(Integer x, Integer y) {
		position.setX(x);
		position.setY(y);
	}
	
	public Position getPosition() {
		return position;
	}
	
	public void setHousePosition(Position position)
	{
		housePosition.setX(position.getX());
		housePosition.setY(position.getY());
	}

	public Integer getHouseID() {
		return houseID;
	}

	public Integer getID() {
		return ID;
	}

	public String getSurname() {
		return surname;
	}

	
	
	public String getNamePerson() {
		return name;
	}
	
	public void setIll(boolean ill)
	{
		this.ill=ill;
	}
	
	public boolean getIll()
	{
		return ill;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGender() {
		return this.gender;
	}
		
	public synchronized void lockCity(Integer x, Integer y, Integer step1, Integer step2) {
		for (int i = 0; i < step1; i++)
			for (int j = 0; j < step2; j++)
				if (!city.getField(x + i, y + j).getType().equals(Types.HOUSE))
					city.getField(x + i, y + j).setAccess(getHouseID());
	}
	
	public synchronized void unlockCity(Integer x, Integer y) {
		
		Integer step1 = 3;
		Integer step2 = 3;
		
		x = position.getX() - 1;
		y = position.getY() - 1;
		
		if (x < 0) {
			step1 += x;
			x = 0;
		}
		if (y < 0) {
			step2 += y;
			y = 0;
		}
		if (x + step1 > city.getDimension() - 1) {
			step1 = x+step1-city.getDimension();
		}
		if (y + step2 > city.getDimension() - 1) {
			step2 = y+step2-city.getDimension();
		}
		
		for (int i = 0; i < step1; i++)
			for (int j = 0; j < step2; j++)
				if (!city.getField(x + i, y + j).getType().equals(Types.HOUSE))
					city.getField(x + i, y + j).setAccess(0);
	}
     	
	public void houseQuarantine(boolean status)
	{
		house.setStatus(status);
	}
	
	public House getHouse()
	{
		return house;
	}
	
	public HealthCard getHealthCard()
	{
		return healthCard;
	}
	

	public void setHealthCard(HealthCard healthCard)
	{
		this.healthCard=healthCard;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public City getCity() {
		return city;
	}
	
	public Integer countRadius() {
		Double radius1 = 25.0 * getCity().getDimension() / 100.0;
		Integer radius = 25 * getCity().getDimension() / 100;

		if (radius1 - radius > 0.5)
			radius += 1;
		return radius;
	}
	
}
