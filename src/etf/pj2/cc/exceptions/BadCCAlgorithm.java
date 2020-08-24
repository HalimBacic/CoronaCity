package etf.pj2.cc.exceptions;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BadCCAlgorithm extends Exception implements Serializable{
	
	public BadCCAlgorithm()
	{
		super("Problem sa algoritmom za raspored osoba. Pokusaj ponovo");
	}
	
	public BadCCAlgorithm(String msg)
	{
		super(msg);
	}

}
