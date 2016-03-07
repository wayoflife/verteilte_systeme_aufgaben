package de.dhbw.verteiltesysteme.a2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EventListener extends Remote{

	public abstract void handleEvent(Event event) throws RemoteException;
}
