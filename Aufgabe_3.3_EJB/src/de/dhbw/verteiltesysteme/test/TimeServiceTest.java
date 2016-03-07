package de.dhbw.verteiltesysteme.test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import de.dhbw.verteiltesysteme.ejbs.TimeServiceImpl;
import de.dhbw.verteiltesysteme.entities.Event;

public class TimeServiceTest {
	
	private SimpleDateFormat timeFormatter = new SimpleDateFormat("kk:mm:ss");
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");
	
	private TimeServiceImpl timeServiceRMI;

	@Before
	public void setUp() throws Exception {
		timeServiceRMI = new TimeServiceImpl();
	}
	
	@Test
	public void getCurrentTime() throws Exception {
		assertThat(timeServiceRMI.getTime(), is(timeFormatter.format(new Date())));
	}
	
	@Test
	public void getCurrentDate() throws Exception {
		assertThat(timeServiceRMI.getDate(), is(dateFormatter.format(new Date())));
	}
	
	@Test
	public void addEvent() throws Exception {
		timeServiceRMI.addEvent(new Event("Aktuelles Event"));
		assertThat(timeServiceRMI.getAllEvents().size(), is(1));
	}
	
	@Test
	public void addPastEvent() throws Exception {
		timeServiceRMI.addEvent(new Event(-5000, "Event in der Vergangenheit"));
		assertThat(timeServiceRMI.getAllEvents().size(), is(1));
		assertThat(timeServiceRMI.getFutureEvents().size(), is(0));
	}
	
	@Test
	public void addFutureEvent() throws Exception {
		timeServiceRMI.addEvent(new Event(5000, "Event in der Zukunft"));
		assertThat(timeServiceRMI.getAllEvents().size(), is(1));
		assertThat(timeServiceRMI.getFutureEvents().size(), is(1));
	}
	
}
