package etf.pj2.map;

import java.io.Serializable;
import java.util.Random;

import etf.pj2.cc.entity.Person;;

@SuppressWarnings("serial")
public class House implements Serializable {
	
	private Integer ID;
	private boolean isQuarantine=false;
	private boolean isFull=false;
	private boolean isEmpty=true;
	private Position position=new Position(0, 0);
	private Person owner;
	private Person coOwner;
	private boolean canHaveChild=false;
	
	public House(Integer x,Integer y)
	{
		Random random=new Random();
		setID(1000+random.nextInt(3000));
		setPosition(new Position(x, y));
	}
	
	public boolean isFull()
	{
		return isFull;
	}
	
	public void setFull(boolean full)
	{
		this.isFull=full;
	}
	
	public synchronized boolean getStatus()
	{
		return isQuarantine;
	}
	
	public void setStatus(boolean status)
	{
		this.isQuarantine=status;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position.x = position.x;
		this.position.y=position.y;
	}

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	public Person getOwnerSurname() {
		return owner;
	}

	public void setOwnerSurname(Person owner) {
		this.owner = owner;
	}

	public boolean isCanHaveChild() {
		return canHaveChild;
	}

	public void setCanHaveChild(boolean canHaveChild) {
		this.canHaveChild = canHaveChild;
	}

	public Person getCoOwner() {
		return coOwner;
	}

	public void setCoOwner(Person coOwner) {
		this.coOwner = coOwner;
	}

}
