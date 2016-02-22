package de.dhbw.verteiltesysteme.a2;

import java.util.Date;

public class Event {
	
	private Date eventDate;
	private String beschreibung;
	
	public Event(String beschreibung) {
		this(new Date(), beschreibung);
	}
	
	public Event(Date date, String beschreibung) {
		setEventDate(date);
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
