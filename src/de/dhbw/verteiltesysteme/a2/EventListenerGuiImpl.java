package de.dhbw.verteiltesysteme.a2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class EventListenerGuiImpl extends UnicastRemoteObject implements EventListener{

	private static final long serialVersionUID = 1L;
	private TimeServiceRMIClientGui clientGui;

	protected EventListenerGuiImpl(TimeServiceRMIClientGui clientGui) throws RemoteException {
		super();
		this.clientGui = clientGui;
	}

	@Override
	public void handleEvent(Event event) throws RemoteException {
		clientGui.showEvent(event);
	}

}
