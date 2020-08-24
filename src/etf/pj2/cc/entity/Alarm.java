package etf.pj2.cc.entity;

import java.io.Serializable;

import etf.pj2.cc.controllers.PersonControl;
import etf.pj2.map.Position;


@SuppressWarnings("serial")
public class Alarm implements Serializable {
	
	private Position position;
	private Integer houseID;
	private Person person;
	
	public Alarm(Position pos, PersonControl personControl)
	{
		setPosition(pos);
		setHouseID(personControl.getPerson().getHouseID());
		setPerson(personControl.getPerson());
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Integer getHouseID() {
		return houseID;
	}

	public void setHouseID(Integer houseID) {
		this.houseID = houseID;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	@Override
	public String toString()
	{
		String toReturn="POSITION: "+position.toString()+" HOUSE ID: "+person.getHouseID();
		return new String(toReturn);
	}
	
	
}
