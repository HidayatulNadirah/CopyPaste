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
	public void startANewSession() {

	}

	public void listDownAvailableSession() {

	}

}
