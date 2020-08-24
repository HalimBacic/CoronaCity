package etf.pj2.cc.exceptions;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BadCCNumber extends Exception implements Serializable {

	public BadCCNumber()
	{
		super("Pogresan unos brojeva!");
	}
	
	public BadCCNumber(String msg)
	{
		super(msg);
	}
}
