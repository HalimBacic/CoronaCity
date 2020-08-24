package interfaces;

import etf.pj2.cc.entity.Person;

public interface Movements {
	
    public void animateTopDown(Integer dir,Double speed, Person person);  // -1 Down +1 Up
    
    public void animateLeftRight(Integer dir,Double speed, Person person); // -1 Left +1 Right
    
	public void animateHomeY(Integer direction,Double speed);
	
	public void animateHomeX(Integer direction,Double speed);
}
