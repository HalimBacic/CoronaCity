package etf.pj2.cc.entity;

import java.io.Serializable;
import java.util.ArrayList;


@SuppressWarnings("serial")
public class HealthCard implements Serializable {

	
	private ArrayList<Float> temperatures=new ArrayList<>();
	
	public HealthCard()
	{}
	
	public ArrayList<Float> getTemperatures()
	{
		return temperatures;
	}
	
	public Float lastThree()
	{	   
	   Integer size=temperatures.size();
	   Float last3=temperatures.get(size-1);
	   Float last2=temperatures.get(size-2);
	   Float last1=temperatures.get(size-3);
	   
	   return (last3+last2+last1)/3;
	}
    
	public void addTemperature(Float currentTemperature)
	{
		temperatures.add(currentTemperature);
	}
	
	public Integer healthCardSize()
	{
		return temperatures.size();
	}
    
	
}
