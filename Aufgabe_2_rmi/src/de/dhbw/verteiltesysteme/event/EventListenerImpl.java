package de.dhbw.verteiltesysteme.event;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class EventListenerImpl extends UnicastRemoteObject implements EventListener{

	private static final long serialVersionUID = 533407711526839667L;

	public EventListenerImpl() throws RemoteException {
		super();
	}

	@Override
	public void handleEvent(Event event) throws RemoteException {
		System.out.println("Event eingetreten: " + event.getBeschreibung() +"\n"
				+ "Enddatum: " + event.getEventDate().toString());
	}

}
