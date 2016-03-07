package de.dhbw.verteiltesysteme.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TimeServiceSocketClient {
	
	private Socket timeServer;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	
	public static void main(String[] args) {
		try {
			TimeServiceSocketClient timeServiceClient = new TimeServiceSocketClient();
			System.out.println(timeServiceClient.dateFromServer());
			System.out.println(timeServiceClient.timeFromServer());
			timeServiceClient.closeConnection();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public TimeServiceSocketClient() throws UnknownHostException, IOException {
			this("localhost");
	}
	
	public TimeServiceSocketClient(String host) throws UnknownHostException, IOException {
		timeServer = new Socket(host, 75);
		setUp();
	}
	
	public TimeServiceSocketClient(InetAddress address) throws UnknownHostException, IOException {
		timeServer = new Socket(address, 75);
		setUp();
	}
	
	private void setUp() throws IOException {
		bufferedReader = new BufferedReader(new InputStreamReader(timeServer.getInputStream()));
		printWriter = new PrintWriter(timeServer.getOutputStream());
		System.out.println(bufferedReader.readLine());
	}
	
	public String dateFromServer() throws IOException{
		System.out.print("request date: ");
		printWriter.println("date");
		printWriter.flush();
		return bufferedReader.readLine();
	}
	
	public String timeFromServer() throws IOException{
		System.out.print("request time: ");
		printWriter.println("time");
		printWriter.flush();
		return bufferedReader.readLine();
	}
	
	public void closeConnection() throws IOException {
		System.out.println("closing connection");
		printWriter.println("quit");
		printWriter.flush();
		timeServer.close();
	}

}
