package de.dhbw.verteiltesysteme.service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.function.Predicate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import de.dhbw.verteiltesysteme.event.Event;
import de.dhbw.verteiltesysteme.event.EventListener;

public class TimeServiceRMI extends UnicastRemoteObject implements TimeService{
	
	private static final long serialVersionUID = 1L;
	private SimpleDateFormat timeFormatter = new SimpleDateFormat("kk:mm:ss");
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
	
	private Vector<EventListener> eventlisteners;
	private Thread eventThread;
	private EntityManagerFactory entityManagerFactory;

	public TimeServiceRMI() throws RemoteException {
		super();
		
		entityManagerFactory = Persistence.createEntityManagerFactory("EventJPA");
		
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
	
	public void addEvent(Event event) throws RemoteException{
		if(null != event){
			EntityManager entityManager = null;
			EntityTransaction transaction = null;
			try {
				entityManager = entityManagerFactory.createEntityManager();
				transaction = entityManager.getTransaction();
				transaction.begin();
				entityManager.persist(event);
				transaction.commit();
			} finally {
				if(null != null && transaction.isActive()){ transaction.rollback(); }
				if(null != entityManager){ entityManager.close(); }
			}
			eventThread.interrupt();
		}
	}
	
	public Vector<Event> getAllEvents() {
		Vector<Event> events = new Vector<Event>();
		EntityManager entityManager = null;
		try {
			entityManager = entityManagerFactory.createEntityManager();
			Query query = entityManager.createQuery("SELECT e FROM Event e");
			for(Object e : query.getResultList()){ events.add((Event) e); }
			// cast warning bei der lambda expression
			//query.getResultList().forEach( e -> events.add((Event) e));
		} finally {
			if(null != entityManager){ entityManager.close(); }
		}
		
		events.sort(eventComparator());
		return events;
	}
	
	public Event getNextEvent(){
		try {
			return getFutureEvents().firstElement();
		} catch (NoSuchElementException | RemoteException e){
			return null;
		}
	}

	public Vector<Event> getFutureEvents() throws RemoteException{
		Vector<Event> futureEvents = new Vector<Event>();
		futureEvents.addAll(getAllEvents());
		futureEvents.removeIf(futureFilter());
		return futureEvents;
	}

	@Override
	public void addEventListener(EventListener listener){
		eventlisteners.add(listener);
		System.out.println("eventlistener has been registered");
	}
	
	@Override
	public void removeEventListener(EventListener listener){
		eventlisteners.remove(listener);
		System.out.println("eventlistener has been removed");
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
			public boolean test(Event event) {
				return new Date().after(event.getEventDate());
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
						if(null == nextEvent){ System.out.println("kein events vorhanden, warte"); wait();}
						long sleepTime = nextEvent.getEventDate().getTime() - new Date().getTime();
						Thread.sleep(sleepTime);
						System.out.println("event hat gezündet");
						for(EventListener listener : eventlisteners){
							try {
								listener.handleEvent(nextEvent);
							} catch (RemoteException e) {
								System.out.println("listener konnte nicht erreicht werden und wird entfernt");
								removeEventListener(listener);
							}
						}
					} catch (InterruptedException e) {
					}
				}
			}
		});
	}
}
