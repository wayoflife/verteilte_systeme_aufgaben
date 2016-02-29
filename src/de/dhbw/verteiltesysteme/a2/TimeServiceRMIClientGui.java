package de.dhbw.verteiltesysteme.a2;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TimeServiceRMIClientGui extends JFrame {

	private static final long serialVersionUID = 1L;
	private SimpleDateFormat timeFormatter = new SimpleDateFormat("kk:mm:ss");
	private TimeService timeService;
	private Label serverTime;
	private JTextField txtEventStartTime;
	private JTextField txtDescription;
	
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		new TimeServiceRMIClientGui();
	}

	public TimeServiceRMIClientGui() throws MalformedURLException, RemoteException, NotBoundException {
		super("TimeService Client");
		timeService = (TimeService) Naming.lookup("rmi://localhost:1099/TimeService");
		timeService = (TimeService) Naming.lookup("TimeService");
		timeService.addEventListener(new EventListenerGuiImpl(this));
		
		init();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	private void init() {
		this.setSize(500, 300);
		this.setLayout(new BorderLayout(5, 5));
		
		serverTime = new Label("TimeServer nicht verbunden");
		this.add(serverTime, BorderLayout.NORTH);
		
		Button jButton = new Button("Neues Event hinzufügen");
		this.add(jButton, BorderLayout.SOUTH);
		jButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(TimeServiceRMIClientGui.this, 
						newEventPanel(), "Erstelle neues Event", JOptionPane.OK_CANCEL_OPTION);
				if(JOptionPane.OK_OPTION == result){
					
				}
			}
		});
	}

	public void showEvent(Event event) {
		//aktualisiere alle eventanzeigen
		// TODO Auto-generated method stub
		
	}
	
	public void addEvent(){
		
	}
	
	private JPanel newEventPanel(){
		
		JPanel pnlNewEvent = new JPanel(new GridLayout(2, 1, 5, 5));
		
		JPanel pnlTime = new JPanel();
		pnlNewEvent.add(pnlTime);
		txtEventStartTime = new JTextField(timeFormatter.format(new Date()));
		pnlTime.add(new JLabel("Startzeit:"));
		pnlTime.add(txtEventStartTime);
		
		JPanel pnlDescription = new JPanel();
		pnlNewEvent.add(pnlDescription);
		txtDescription = new JTextField("Das Event ist...");
		pnlDescription.add(new JLabel("Beschreibung:"));
		pnlDescription.add(txtDescription);
		
		return pnlNewEvent;
	}
}
