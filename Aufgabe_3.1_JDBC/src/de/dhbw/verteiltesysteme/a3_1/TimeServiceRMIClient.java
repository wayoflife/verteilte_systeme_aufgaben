package de.dhbw.verteiltesysteme.a3_1;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class TimeServiceRMIClient {
	
	private TimeService timeService;

	public TimeServiceRMIClient() throws MalformedURLException, RemoteException, NotBoundException {
		timeService = (TimeService) Naming.lookup("rmi://localhost:1099/TimeService");
		timeService = (TimeService) Naming.lookup("TimeService");
		timeService.addEventListener(new EventListenerImpl());
	}
	
	public void printTimeAndDate() throws RemoteException{
		System.out.println(timeService.getTimeAndDate().toString());
	}
}
