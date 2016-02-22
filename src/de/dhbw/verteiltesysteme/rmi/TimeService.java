package de.dhbw.verteiltesysteme.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TimeService extends Remote{
	
	public abstract String getDate() throws RemoteException;
	public abstract String getTime() throws RemoteException;

}
