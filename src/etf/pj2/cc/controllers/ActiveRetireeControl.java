package etf.pj2.cc.controllers;

import etf.pj2.cc.entity.Person;
import etf.pj2.cc.view.ActiveRetireeView;


public class ActiveRetireeControl extends PersonControl  {

    
	public ActiveRetireeControl(ActiveRetireeView retireeView,Person person) {
		super(retireeView, person);
		setRadius(4);
		setSpeed(1.9);
	}
}
