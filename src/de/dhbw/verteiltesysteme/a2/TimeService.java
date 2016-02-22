package de.dhbw.verteiltesysteme.a2;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

public interface TimeService extends Remote{
	
	public abstract String getDate() throws RemoteException;
	public abstract String getTime() throws RemoteException;
	public abstract Date getTimeAndDate() throws RemoteException;

}
