package project02;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SessionInitial implements Runnable{
		private Socket client;
		 private DataInputStream fromClient;
		 private DataOutputStream toClient;
		
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
			} catch (IOException e) {
				System.out.println("Communication problem with " +
		client.getInetAddress().getHostAddress()
				 + " " + client.getInetAddress().getHostName() + " .");
		}
	}
}
