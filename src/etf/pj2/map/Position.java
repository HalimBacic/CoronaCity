package etf.pj2.map;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Position implements Serializable {

	Integer x;
	Integer y;
	
	public Position(Integer x, Integer y)
	{
		this.x=x;
		this.y=y;
	}

	public Position() {
		
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}
	
	public void setPosition(Position position)
	{
		this.x=position.getX();
		this.y=position.getY();
	}

	public void setY(Integer y) {
		this.y = y;
	}
	
	@Override
	public String toString()
	{
		return getX()+","+getY();
	}
}
