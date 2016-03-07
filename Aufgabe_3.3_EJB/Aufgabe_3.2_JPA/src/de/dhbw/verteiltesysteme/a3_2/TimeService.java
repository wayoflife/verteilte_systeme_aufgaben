package de.dhbw.verteiltesysteme.a3_2;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.Vector;

public interface TimeService extends Remote{
	
	public abstract String getDate() throws RemoteException;
	public abstract String getTime() throws RemoteException;
	public abstract Date getTimeAndDate() throws RemoteException;
	public abstract void addEventListener(EventListener eventListener) throws RemoteException;
	public abstract void removeEventListener(EventListener eventListener) throws RemoteException;
	public void addEvent(Event event) throws RemoteException;
	public abstract Vector<Event> getFutureEvents() throws RemoteException;
}
