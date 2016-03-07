package de.dhbw.verteiltesysteme.event;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import de.dhbw.verteiltesysteme.client.TimeServiceRMIClientGui;

public class EventListenerGuiImpl extends UnicastRemoteObject implements EventListener{

	private static final long serialVersionUID = 1L;
	private TimeServiceRMIClientGui clientGui;

	public EventListenerGuiImpl(TimeServiceRMIClientGui clientGui) throws RemoteException {
		super();
		this.clientGui = clientGui;
	}

	@Override
	public void handleEvent(Event event) throws RemoteException {
		clientGui.showEvent(event);
	}

}
