package de.dhbw.verteiltesysteme.a3_2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EventListener extends Remote{

	public abstract void handleEvent(Event event) throws RemoteException;
}