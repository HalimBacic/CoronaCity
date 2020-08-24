package etf.pj2.map;

import java.io.Serializable;

import interfaces.Types;

@SuppressWarnings("serial")
public class Field implements Serializable{

	private String type;
	private Position position;
	private Integer owner=0;
	private boolean isControlPoint=false;
	
	public Field(String type,Position pos)
	{
		this.type=type;
		this.setPosition(pos);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
	public Integer getX()
	{
		return position.getX();
	}
	
	public Integer getY()
	{
		return position.getY();
	}

	public synchronized boolean isAccessible(Integer owner) {
		if(this.owner==owner || (this.owner==0 && !this.type.equals(Types.HOUSE)))
		return true;
		else
			return false;
	}

	public synchronized void setAccess(Integer owner) {
		this.owner=owner;
	}
	
	public synchronized void setOwner(Integer owner)
	{
		this.owner=owner;
	}

	public boolean isControlPoint() {
		return isControlPoint;
	}

	public void setControlPoint(boolean isControlPoint) {
		this.isControlPoint = isControlPoint;
	}
	
}
