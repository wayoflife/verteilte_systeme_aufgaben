package de.dhbw.verteiltesysteme.a2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.function.Predicate;

public class TimeServiceRMI extends UnicastRemoteObject implements TimeService{
	
	private static final long serialVersionUID = 1L;
	private SimpleDateFormat timeFormatter = new SimpleDateFormat("kk:mm:ss");
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
	
	private volatile Vector<Event> events;
	private Vector<EventListener> eventlisteners;
	private Thread eventThread;

	public TimeServiceRMI() throws RemoteException {
		super();
		events = new Vector<Event>();
		eventlisteners = new Vector<EventListener>();
		
		eventThread = getEventThread();
		eventThread.start();
	}

	@Override
	public synchronized String getDate() throws RemoteException{
		return dateFormatter.format(new Date());
	}

	@Override
	public synchronized String getTime() throws RemoteException{
		return timeFormatter.format(new Date());
	}

	@Override
	public synchronized Date getTimeAndDate() throws RemoteException {
		return new Date();
	}
	
	public void addEvent(Event event) {
		events.add(event);
		events.sort(eventComparator());
		eventThread.interrupt();
	}
	
	public Vector<Event> getAllEvents() {
		return events;
	}
	
	public Event getNextEvent(){
		try {
			return getFutureEvents().firstElement();
		} catch (NoSuchElementException e){
			return null;
		}
	}

	public Vector<Event> getFutureEvents() {
		Vector<Event> futureEvents = new Vector<Event>();
		futureEvents.addAll(events);
		futureEvents.removeIf(futureFilter());
		return futureEvents;
	}

	@Override
	public void addEventListener(EventListener listener){
		eventlisteners.add(listener);
	}
	
	@Override
	public void removeEventListener(EventListener listener){
		eventlisteners.remove(listener);
	}

	private Comparator<Event> eventComparator() {
		return new Comparator<Event>() {
			
			@Override
			public int compare(Event e1, Event e2) {
				return e1.getEventDate().compareTo(e2.getEventDate());
			}
		};
	}
	
	private Predicate<Event> futureFilter() {
		Predicate<Event> filter = new Predicate<Event>() {

			@Override
			public boolean test(Event t) {
				if(t.getEventDate().after(new Date())){
					return false;
				} 
				return true;
			}
		};
		return filter;
	}
	
	private Thread getEventThread() {
		return new Thread(new Runnable() {
			
			@Override
			public synchronized void run() {
				while(true){
					try {
						Event nextEvent = getNextEvent();
						if(null == nextEvent){ wait();}
						long sleepTime = nextEvent.getEventDate().getTime() - new Date().getTime();
						Thread.sleep(sleepTime);
						
						for(EventListener listener : eventlisteners){
							try {
								listener.handleEvent(nextEvent);
							} catch (RemoteException e) {
								System.out.println("listener konnte nicht erreicht werden");
								e.printStackTrace();
							}
						}
					} catch (InterruptedException e) {
					}
				}
			}
		});
	}
}
