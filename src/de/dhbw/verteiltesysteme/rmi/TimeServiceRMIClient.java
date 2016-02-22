package de.dhbw.verteiltesysteme.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class TimeServiceRMIClient {
	
	public TimeServiceRMIClient() throws MalformedURLException, RemoteException, NotBoundException {
		//TimeService timeService = (TimeService) Naming.lookup("rmi://localhost:1099/TimeService");
		TimeService timeService = (TimeService) Naming.lookup("TimeService");
		
		System.out.println(timeService.getDate());
		System.out.println(timeService.getTime());
	}
	
}
