package de.dhbw.verteiltesysteme.ejbs;

import java.rmi.Remote;
import java.util.Vector;

import de.dhbw.verteiltesysteme.entities.Event;

public interface TimeService extends Remote{
	
	public abstract String getDate();
	public abstract String getTime();
	public abstract void addEvent(Event event);
	public abstract Vector<Event> getFutureEvents();
}
