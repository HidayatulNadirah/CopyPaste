package project02;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketException;

public class SessionHandler {
	private Socket client1, client2;
	private int sessionId;
	private boolean waitingForClient2;
	private DataInputStream fromClient1, fromClient2;
	private DataOutputStream toClient1, toClient2;
	private boolean successfullySentToClient1 = true;
	private boolean successfullySentToClient2 = true;

	public SessionHandler(int sessionId, Socket client1) {
		this.sessionId = sessionId;
		this.client1 = client1;
		waitingForClient2 = true;
	}
	public void connectClient2(Socket client) {
		this.client2 = client2;
		waitingForClient2 = false;
	}


	public void startWaitingOtherClient() {
		try {
			fromClient1 = new DataInputStream(client1.getInputStream());
			toClient1 = new DataOutputStream(client1.getOutputStream());

			while(waitingForClient2) {
				try {
					Thread.sleep(500);
					toClient1.writeBoolean(waitingForClient2);
					toClient1.writeUTF("Waiting for other client to connect");
				} catch (InterruptedException e) {
					System.out.println(getSessionName() + " was waiting, but interrupted.");
				}
			}
			startChat();
		} catch (IOException e) {
			System.out.println("Communication problem in " + getSessionName());
		}
	}
	public void startChat()throws IOException{
		toClient1.writeUTF(client2.getInetAddress().getHostName());
		toClient1.writeUTF(client2.getInetAddress().getHostAddress());

		fromClient2 = new DataInputStream(client2.getInputStream());
		toClient2 = new DataOutputStream(client2.getOutputStream());

		toClient2.writeUTF(client1.getInetAddress().getHostName());
		toClient2.writeUTF(client1.getInetAddress().getHostAddress());

		while(true) {
			if(fromClient1.available() > 0) {
				forwardMessageToClient2();
				break;
			}
			if(fromClient2.available() > 0) {
				forwardMessageToClient1();
				break;
			}
			if(fromClient1.available()>0) {

			}
		}
	}
	public void notifyProblemToClient1() throws IOException {
		toClient1.writeUTF("Other Client has disconnected.");
		toClient1.writeBoolean(successfullySentToClient2);
	}
	public void notifyProblemToClient2() throws IOException {
		toClient2.writeUTF("Other Client has disconnected.");
		toClient2.writeBoolean(successfullySentToClient1);
	}
	public void forwardMessageToClient1() throws IOException {
		String message = fromClient2.readUTF();
		try {
			toClient1.writeUTF(message);
		} catch(SocketException e) {
			successfullySentToClient1 = false;
		}
	}
	public void forwardMessageToClient2() throws IOException {
		String message = fromClient1.readUTF();
		try {
			toClient2.writeUTF(message);
		} catch(SocketException e) {
			successfullySentToClient2 = false;
		}
	}

	public String getSessionName() {
		return sessionId + " " + client1.getInetAddress().getHostName()+" " + 
				client1.getInetAddress().getHostAddress();
	}
}
