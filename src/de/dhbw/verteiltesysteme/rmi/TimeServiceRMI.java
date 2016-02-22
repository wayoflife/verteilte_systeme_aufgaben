package de.dhbw.verteiltesysteme.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServiceRMI extends UnicastRemoteObject implements TimeService{
	
	private static final long serialVersionUID = 1L;
	private SimpleDateFormat timeFormatter = new SimpleDateFormat("kk:mm:ss");
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

	public TimeServiceRMI() throws RemoteException {
		super();
	}

	@Override
	public synchronized String getDate() throws RemoteException{
		return dateFormatter.format(new Date());
	}

	@Override
	public synchronized String getTime() throws RemoteException{
		return timeFormatter.format(new Date());
	}

}
