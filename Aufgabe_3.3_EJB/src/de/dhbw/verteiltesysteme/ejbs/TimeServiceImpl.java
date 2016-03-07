package de.dhbw.verteiltesysteme.ejbs;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.function.Predicate;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import de.dhbw.verteiltesysteme.entities.Event;

@Stateless
@Remote(TimeService.class)
public class TimeServiceImpl implements TimeService{
	
	private SimpleDateFormat timeFormatter = new SimpleDateFormat("kk:mm:ss");
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
	
	private Thread eventThread;
	
	@PersistenceContext(unitName="eventEJBDB")
	private EntityManager em;

	public TimeServiceImpl() {
		super();
		
		eventThread = getEventThread();
		eventThread.start();
	}

	@Override
	public synchronized String getDate() {
		return dateFormatter.format(new Date());
	}

	@Override
	public synchronized String getTime(){
		return timeFormatter.format(new Date());
	}
	
	public void addEvent(Event event){
		if(null != event){
			
			//TODO implement

			eventThread.interrupt();
		}
	}
	
	public Vector<Event> getAllEvents() {
		Vector<Event> events = new Vector<Event>();

		//TODO implement
		
		events.sort(eventComparator());
		return events;
	}
	
	public Event getNextEvent(){
		try {
			return getFutureEvents().firstElement();
		} catch (NoSuchElementException e){
			return null;
		}
	}

	public Vector<Event> getFutureEvents(){
		Vector<Event> futureEvents = new Vector<Event>();
		futureEvents.addAll(getAllEvents());
		futureEvents.removeIf(futureFilter());
		return futureEvents;
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
						if(null == nextEvent){ wait();}
						long sleepTime = nextEvent.getEventDate().getTime() - new Date().getTime();
						Thread.sleep(sleepTime);

						//TODO feuere/behandle events
						// am besten mit irgendeiner push operation auf die clients, jms?
					
					} catch (InterruptedException e) {
					}
				}
			}
		});
	}
}
