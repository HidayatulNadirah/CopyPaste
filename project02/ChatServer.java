package project02;
import java.net.ServerSocket;
import java.net.BindException;
import java.io.IOException;
public class ChatServer {
	public static void main(String[]args) {
		ServerSocket serversocket = null;
		try {
			serversocket = new ServerSocket(9101);
		} catch (BindException e) {
			System.out.println("Unable to start server due to port is already in used.");
		} catch (IOException e) {
			System.out.println("Unexpected problem. Unable to start server.");
		}
	}
}
