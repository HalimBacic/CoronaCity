package etf.pj2.cc.exceptions;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BadChildsNumber extends Exception implements Serializable{
	
	public BadChildsNumber() {
		super("Djeca ne mogu biti sama u gradu!");
	}
}
