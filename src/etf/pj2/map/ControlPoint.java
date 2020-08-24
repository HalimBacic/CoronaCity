package etf.pj2.map;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ControlPoint implements Serializable {

	String name;
	Position position;
	
	public ControlPoint(Position p)
	{
		name=p.getX().toString()+","+p.getY().toString();
		position=p;
	}
	
}
