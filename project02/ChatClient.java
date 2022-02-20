package project02;
import java.util.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.net.ConnectException;

public class ChatClient {
	private static String ipAddress = "localhost";
	private static int portNumber = 9101;
	private static Socket socket;
	private static DataInputStream fromServer;
	private static DataOutputStream toServer;
	private static Scanner scanner;
	private static boolean chatActive = true;

	public static void main(String[]args) {
		try {
			socket = new Socket(ipAddress, portNumber);

			fromServer = new DataInputStream(socket.getInputStream());

			toServer = new DataOutputStream(socket.getOutputStream());

			scanner = new Scanner(System.in);

			System.out.println(fromServer.readUTF());
			mainMenuLogic();

		} catch (ConnectException | UnknownHostException e) {
			System.out.println("Unable to connect to server " + ipAddress + ":" +
					portNumber);
		} catch (IOException e) {
			System.out.println("Problem communicating with the server.");
		} finally {
			try {
				if(socket != null) {
					socket.close();
				}

				if(fromServer != null) {
					fromServer.close();
				}
				if(toServer != null) {
					toServer.close();
				}}
			catch (IOException e) {}
		}}

	private static void mainMenuLogic() throws IOException{
		readMainMenu();
		selectMainMenuOption();
	}

	private static void readMainMenu() throws IOException {
		System.out.println(fromServer.readUTF());
	}

	public static void selectMainMenuOption() throws IOException{
		String option = scanner.nextLine();
		toServer.writeUTF(option);

		boolean validOptionSelected = fromServer.readBoolean();
		if(validOptionSelected) {
			if(option.equals("1")) {
				waitingForOtherClient();
			}
			if(option.equals("2")) {
				listDownAvailableSession();
			}
		}else {
			System.out.println(fromServer.readUTF());
			mainMenuLogic();
		}
	}
	public static void waitingForOtherClient() throws IOException{
		System.out.println(fromServer.readUTF());
		boolean waiting = true;
		while(waiting) {
			try {
				Thread.sleep(500);
				waiting = fromServer.readBoolean();
				System.out.println(fromServer.readUTF());	
			} catch (InterruptedException e) {
				System.out.println("Waiting was interupted.");
			}
		}
	}
	public static void listDownAvailableSession() {
	}
}

