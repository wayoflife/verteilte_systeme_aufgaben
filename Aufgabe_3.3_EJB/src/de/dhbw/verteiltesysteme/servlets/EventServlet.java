package de.dhbw.verteiltesysteme.servlets;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.dhbw.verteiltesysteme.ejbs.TimeService;
import de.dhbw.verteiltesysteme.entities.Event;

@WebServlet(value="/event")
public class EventServlet extends HttpServlet{
	
	private static final long serialVersionUID = -6992309923099033662L;
	
	@EJB
	private TimeService timeService;

	/**
	 * POST erstellt neues Event
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		//TODO empfangene Daten erst auf korrektheit testen
		timeService.addEvent(
				new Event(Long.parseLong(req.getParameter("time")), req.getParameter("description")));
		
		super.doPost(req, resp);
	}

}
