package etf.pj2.cc.exceptions;


import java.io.IOException;
import java.io.Serializable;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.Level;;

@SuppressWarnings("serial")
public class CCLogger implements Serializable {
	
	public static Handler handler;
	
	public static void cclogger(String place,Level level,Exception ex)
	{
		try {
			handler=new FileHandler("log.txt",true);
			Logger.getLogger(place).addHandler(handler);
			Logger.getLogger(place).log(level, ex.fillInStackTrace().toString());
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
