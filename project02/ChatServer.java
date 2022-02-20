package project02;
import java.net.ServerSocket;
import java.net.BindException;
import java.io.IOException;
import java.util.Date;
import java.net.Socket;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
public class ChatServer {
	static List<SessionHandler> availableSessions;
	static int numberOfSessionCreated = 0;

	public static void main(String[]args) {
		availableSessions = Collections.synchronizedList(new
				ArrayList<SessionHandler>());
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(9101);
			System.out.println("ServerSocket at " + new Date());
			while(true) {
				Socket client = serverSocket.accept();
				new Thread(new SessionInitial(client)).start();
			}
		} catch (BindException e) {
			System.out.println("Unable to start server due to port is already in used.");
		} catch (IOException e) {
			System.out.println("Unexpected problem. Unable to start server.");
		}finally {
			try {
				if (serverSocket !=null) {
					serverSocket.close();
				}
			}catch (IOException e) {
				System.out.println("Unexpected problem. Unable to close server socket.");
			}
		}
	}
}
