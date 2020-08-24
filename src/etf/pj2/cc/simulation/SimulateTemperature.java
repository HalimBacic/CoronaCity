package etf.pj2.cc.simulation;

import java.io.Serializable;
import java.util.Random;
import java.util.TimerTask;

@SuppressWarnings("serial")
public class SimulateTemperature extends TimerTask implements Serializable {

	Float temperature=(float) 30;
	Random random=new Random();
	@Override
	public void run() {
		temperature=10*random.nextFloat()+30;
	}
	
	public Float getTemp()
	{
		return temperature;
	}

}
