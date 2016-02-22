package de.dhbw.verteiltesysteme.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Application {
	
	public static void main(String[] args) throws Exception {
		Registry registry = LocateRegistry.createRegistry(1099);//1099 ist default
		TimeServiceRMI timeServiceRMI = new TimeServiceRMI();
		registry.bind("TimeService", timeServiceRMI);
		System.out.println("RMI started on port 1099");		
		
		new TimeServiceRMIClient();
	}

}
