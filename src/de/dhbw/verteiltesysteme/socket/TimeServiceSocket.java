package de.dhbw.verteiltesysteme.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TimeServiceSocket extends Thread{

	private ServerSocket serverSocket;
	private ExecutorService fixedThreadPool;

	public static void main(String[] args) {
		try {
			new TimeServiceSocket();
		} catch (IOException e) {
			System.out.println("cannot create TimeService: " + e.getMessage());
		} 
	}
	
	public TimeServiceSocket() throws IOException {
		serverSocket = new ServerSocket(75);
		fixedThreadPool = Executors.newFixedThreadPool(5);
		this.run();
	}
	
	private Runnable createRunnable(final Socket socket){
		return new Runnable() {

			private PrintWriter printWriter;
			private BufferedReader bufferedReader;
			
			private SimpleDateFormat timeFormatter = new SimpleDateFormat("kk:mm:ss");
			private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

			@Override
			public void run() {
				System.out.println(timeFormatter.format(new Date()) 
						+ " connection accepted from: " + socket.getInetAddress() + ":" + socket.getPort());
				try {
					bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					printWriter = new PrintWriter(socket.getOutputStream());
					printWriter.println("time service");
					printWriter.flush();

					while(true){
						String line = bufferedReader.readLine();
						if(null != line && line.matches("time")) {
							printWriter.println(timeFormatter.format(new Date()));
							printWriter.flush();
						} else if(null != line && line.matches("date")){
							printWriter.println(dateFormatter.format(new Date()));
							printWriter.flush();
						} else {
							socket.close();
						}
					}
				} catch (IOException e) {
					System.out.println(timeFormatter.format(new Date()) 
							+ " Socket has been closed: " + socket.getInetAddress() + ":" + socket.getPort());
				} 
			}
		};
	}
	
	@Override
	public void run() {
		System.out.println("Waiting for connections");
		while(true){
			try {
				fixedThreadPool.execute(createRunnable(serverSocket.accept()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
