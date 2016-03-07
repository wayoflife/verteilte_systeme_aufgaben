package de.dhbw.verteiltesysteme.a3_2;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name="Event")
public class Event implements Serializable{

	private static final long serialVersionUID = -4084919683176155680L;
	
	@Id  @GeneratedValue
	private int id;
	
	private Date eventDate;
	private String beschreibung;
	
	public Event() {
		super();
	}
	
	public Event(String beschreibung) throws RemoteException{
		setEventDate(new Date());
		setBeschreibung(beschreibung);
	}
	
	public Event(Date date, String beschreibung) throws RemoteException{
		setEventDate(date);
		setBeschreibung(beschreibung);
	}
	
	public Event(long timeFromNow, String beschreibung) throws RemoteException{
		setEventDate(new Date(new Date().getTime() + timeFromNow));
		setBeschreibung(beschreibung);
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
	
	public void printInfo(){
		System.out.println("Zeitpunkt: " + eventDate.toString() + "\n"
				+ "Beschreibung: " + beschreibung);
	}
}