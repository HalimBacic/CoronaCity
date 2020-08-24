package etf.pj2.cc.controllers;

import etf.pj2.cc.entity.Person;
import etf.pj2.cc.view.ActivePersonView;


public class ActivePersonControl extends PersonControl {

	Person person;
	ActivePersonView activePersonView;

	public ActivePersonControl(ActivePersonView personview, Person person) {
		super(personview, person);
		setRadius(person.countRadius());
		setSpeed(1.7);
	}
}
