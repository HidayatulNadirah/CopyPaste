package project02;
import java.util.*;
import java.io.*;
import java.net.Socket;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class SessionInitial implements Runnable{
	private Socket client;
	private DataInputStream fromClient;
	private DataOutputStream toClient;

	public void sendMainMenu() throws IOException {
		String menu = "";
		menu = menu + "Please choose an option:\n";
		menu = menu + "(1) Start a new Chatting session\n";
		menu = menu + "(2) Connect to available session\n";
		toClient.writeUTF(menu);
	}

	public SessionInitial(Socket client) {
		this.client = client;
		System.out.println("Client connected: " +
				client.getInetAddress().getHostAddress() + " " 
				+ client.getInetAddress().getHostName() + " .");
	}

	public void run() {
		try {
			fromClient = new DataInputStream(client.getInputStream());
			toClient = new DataOutputStream(client.getOutputStream());

			toClient.writeUTF("Welcome to PB Chatting");
			mainMenuLogic();

		} catch (IOException e) {
			System.out.println("Communication problem with " +
					client.getInetAddress().getHostAddress()
					+ " " + client.getInetAddress().getHostName() + " .");
		}

	}
	public void mainMenuLogic() throws IOException{
		sendMainMenu();
		readOptionFromClient();
	}
	public void readOptionFromClient() throws IOException{
		String option = fromClient.readUTF();

		boolean validOptionSelected = false;
		if (option.equals("1") || option.equals("2")) {
			validOptionSelected = true;
			toClient.writeBoolean(validOptionSelected);
			if(option.equals("1")) {
				startANewSession();
			}if(option.equals("2")) {
				listDownAvailableSession();
			}
		}else {
			validOptionSelected = false;
			toClient.writeBoolean(validOptionSelected);
			toClient.writeUTF("Wrong option. Please select correct option.");
			mainMenuLogic();
		}

	}
	public void startANewSession() throws IOException{
		ChatServer.numberOfSessionCreated++;
		SessionHandler chatSession = new SessionHandler(ChatServer.numberOfSessionCreated, client);
		System.out.println("New session started: " + chatSession.getSessionName());
		toClient.writeUTF("New session started: " + chatSession.getSessionName());
		ChatServer.availableSessions.add(chatSession);
		chatSession.startWaitingOtherClient();
	}

	public void listDownAvailableSession() throws IOException{
		boolean noAvailableSession = ChatServer.availableSessions.isEmpty();
		toClient.writeBoolean(noAvailableSession);
		if(!noAvailableSession) {
			int numberOfAvailableSessions = ChatServer.availableSessions.size();
			toClient.writeInt(ChatServer.availableSessions.size());
			toClient.writeUTF("Please Choose an option");
			for(int i = 0; i < ChatServer.availableSessions.size(); i++) {
				SessionHandler chatSession = ChatServer.availableSessions.get(i);
				toClient.writeUTF("(" + i + ") " + chatSession.getSessionName());
			}
			toClient.writeUTF("(" + ChatServer.availableSessions.size()
			+ ") Back to Main Menu");

			clientSelectedSession();
		}else {
			toClient.writeUTF("No available session to choose.");
			 toClient.writeUTF("Do you want to start new session? (y/n)");
			 
			 String decision = fromClient.readUTF();
			 if(decision.equalsIgnoreCase("y")) {
			 startANewSession();
			 } else {
			 toClient.writeUTF("Go to main menu? (y/n)");
			 decision = fromClient.readUTF();
			 if(decision.equalsIgnoreCase("y")) {
			 mainMenuLogic();
			 } else {
			 listDownAvailableSession();
			 }
			 }
		}
	}
	public void clientSelectedSession()throws IOException {
		boolean validSelectedSession = false;
		int selectedSession = fromClient.readInt();
		if(selectedSession >= 0 && selectedSession <
				ChatServer.availableSessions.size()) {
			SessionHandler chatSession = ChatServer.availableSessions.remove(selectedSession);
			validSelectedSession = true;
			toClient.writeBoolean(validSelectedSession);
			toClient.writeUTF("Connected to: " + chatSession.getSessionName());
			chatSession.connectClient2(client);
		} else {
			if(selectedSession == ChatServer.availableSessions.size()) {
				validSelectedSession = true;
				toClient.writeBoolean(validSelectedSession);
				toClient.writeUTF("Going back to main menu.");
				mainMenuLogic();
			} else {
				validSelectedSession = false;
				toClient.writeBoolean(validSelectedSession);
				toClient.writeUTF("Selected session does not exists.");
				listDownAvailableSession(); 
			}
		}
	}
}
