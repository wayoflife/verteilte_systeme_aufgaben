package de.dhbw.verteiltesysteme;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import de.dhbw.verteiltesysteme.client.TimeServiceRMIClient;
import de.dhbw.verteiltesysteme.event.Event;
import de.dhbw.verteiltesysteme.service.TimeServiceRMI;

public class Application {
	
	public static void main(String[] args) throws Exception {
		Registry registry = LocateRegistry.createRegistry(1099);//1099 ist default
		TimeServiceRMI timeServiceRMI = new TimeServiceRMI();
		registry.bind("TimeService", timeServiceRMI);
		System.out.println("Time Service RMI started on port 1099");		
		
		//Das hier gehört alles in die Tests
		TimeServiceRMIClient client = new TimeServiceRMIClient();
		client.printTimeAndDate();
		
		timeServiceRMI.addEvent(new Event(2000, "erstes event"));
		timeServiceRMI.addEvent(new Event(3000, "zweites event"));
		timeServiceRMI.addEvent(new Event(5000, "drittes event"));
		timeServiceRMI.addEvent(new Event(6000, "viertes event"));
		timeServiceRMI.addEvent(new Event(8000, "fünftes event"));
		
		System.out.println("application wartet für 12 sekunden");
		Thread.sleep(12000);
		
		timeServiceRMI.addEvent(new Event(2000, "erstes event nach pause"));
		System.out.println("letztes event hinzugefügt");
	}

}
