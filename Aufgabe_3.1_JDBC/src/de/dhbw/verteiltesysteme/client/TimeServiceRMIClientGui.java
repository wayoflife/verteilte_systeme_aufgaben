package de.dhbw.verteiltesysteme.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import de.dhbw.verteiltesysteme.event.Event;
import de.dhbw.verteiltesysteme.event.EventListenerGuiImpl;
import de.dhbw.verteiltesysteme.service.TimeService;

public class TimeServiceRMIClientGui extends JFrame {

	private static final long serialVersionUID = 1L;
	private SimpleDateFormat timeFormatter = new SimpleDateFormat("kk:mm:ss");
	private TimeService timeService;
	private JLabel lblServerTime;
	private JTextArea txtDescription;
	private JTextArea allEvents;
	private JTextField txtTime;
	
	public static void main(String[] args) {
		new TimeServiceRMIClientGui();
	}

	public TimeServiceRMIClientGui() {
		super("TimeService Client");
		try{
			timeService = (TimeService) Naming.lookup("rmi://localhost:1099/TimeService");
			timeService = (TimeService) Naming.lookup("TimeService");
			EventListenerGuiImpl eventListener = new EventListenerGuiImpl(this);
			timeService.addEventListener(eventListener);

			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setVisible(true);
			this.setSize(400, 250);
			this.setLayout(new BorderLayout(5, 5));
			
			init();
			
			this.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					try {
						timeService.removeEventListener(eventListener);
					} catch (RemoteException e1) {
						// remote service not reachable, remoteservice will delete listener
					}
					super.windowClosing(e);
				}
			});

			Executors.newSingleThreadExecutor().execute(pollTimeServer());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "TimeService nicht verfügbar. Anwendung beendet sich jetzt.");
		}
	}

	private void init() {
		lblServerTime = new JLabel("TimeServer nicht verbunden");
		this.add(lblServerTime, BorderLayout.NORTH);
		
		JButton btnAddEvent = new JButton("Neues Event hinzufügen");
		this.add(btnAddEvent, BorderLayout.SOUTH);
		btnAddEvent.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(TimeServiceRMIClientGui.this, 
						newEventPanel(), "Erstelle neues Event", JOptionPane.OK_CANCEL_OPTION);
				if(JOptionPane.OK_OPTION == result){
					addEvent();
				}
			}
		});
		
		allEvents = new JTextArea(5, 10);
		this.add(allEvents, BorderLayout.CENTER);
	}

	public void showEvent(Event event) {
		JOptionPane.showMessageDialog(this, 
				"Ausgelöst um: " + timeFormatter.format(event.getEventDate()) + "\n"
				+ "Beschreibung: " + event.getBeschreibung());
	}
	
	private void addEvent(){
		try {
			Date startDate = new Date(System.currentTimeMillis() + 1000 * Integer.parseInt(txtTime.getText()));
			timeService.addEvent(new Event(startDate, txtDescription.getText()));
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NumberFormatException e){
			JOptionPane.showMessageDialog(this, "Nur gültige Ganzzahl zulässig");
		}
	}
	
	private JPanel newEventPanel(){
		
		JPanel pnlNewEvent = new JPanel(new BorderLayout(5, 5));
		
		JPanel pnlnorthernPanel = new JPanel();
		pnlNewEvent.add(pnlnorthernPanel, BorderLayout.NORTH);
		txtTime = new JTextField("0", 10);
		pnlnorthernPanel.add(new JLabel("Startzeit von jetzt in Sekunden:"));
		pnlnorthernPanel.add(txtTime);
		
		txtDescription = new JTextArea("Hier eine Eventbeschreibung",5 ,10);
		pnlNewEvent.add(new JLabel("Beschreibung:"), BorderLayout.WEST);
		pnlNewEvent.add(txtDescription, BorderLayout.CENTER);
		
		return pnlNewEvent;
	}

	private Runnable pollTimeServer() {
		return () -> {
			while(true){
				try {
					lblServerTime.setText("ServerTime: " + timeService.getTime());
					allEvents.setText("");
					for(Event e : timeService.getFutureEvents()){
						allEvents.append(
								timeFormatter.format(e.getEventDate()) + ": " + e.getBeschreibung() + "\n");
					}
					Thread.sleep(1000);
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}};
	}
}
