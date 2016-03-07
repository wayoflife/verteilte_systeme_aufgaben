package de.dhbw.verteiltesysteme.a3_1;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.function.Predicate;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class TimeServiceRMI extends UnicastRemoteObject implements TimeService{
	
	private static final long serialVersionUID = 1L;
	private SimpleDateFormat timeFormatter = new SimpleDateFormat("kk:mm:ss");
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
	
	private Vector<EventListener> eventlisteners;
	private Thread eventThread;
	private DataSource dataSource;

	public TimeServiceRMI() throws RemoteException {
		super();
		
		MysqlDataSource mysqlDataSource = new MysqlDataSource();
		mysqlDataSource.setURL("jdbc:mysql://localhost:3306/event");
		mysqlDataSource.setUser("root");
		mysqlDataSource.setPassword("passwort");
		dataSource = (DataSource) mysqlDataSource;
		
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
		Connection connection = null;
		try {
			if(null != event){
				connection = dataSource.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO event (time, description) VALUES (?,?)");
				Timestamp x = new Timestamp(event.getEventDate().getTime());
				preparedStatement.setTimestamp(1, x);
				preparedStatement.setString(2, event.getBeschreibung());
				preparedStatement.execute();
				eventThread.interrupt();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(null != connection) try { connection.close(); } catch (SQLException e) {}
		}
	}
	
	public Vector<Event> getAllEvents() {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			Statement statement = connection.createStatement();
			statement.execute("SELECT time,description from event");
			ResultSet resultSet = statement.getResultSet();
			Vector<Event> events = new Vector<Event>();
			while(resultSet.next()){
				events.add(new Event(resultSet.getTimestamp("time"), resultSet.getString("description")));
			}
			events.sort(eventComparator());
			return events;
		} catch (SQLException e) {
		} catch (RemoteException e) {
		} finally {
			if(null != connection) try { connection.close(); } catch (SQLException e) {}
		}
		return null;
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
