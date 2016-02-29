package de.dhbw.verteiltesysteme.a2;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Date;

public class Event implements Serializable{

	private static final long serialVersionUID = -4084919683176155680L;
	private Date eventDate;
	private String beschreibung;
	
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
}