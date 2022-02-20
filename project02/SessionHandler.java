package project02;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SessionHandler {
	private Socket client1;
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
			} catch (IOException e) {
				System.out.println("Communication problem in " + getSessionName());
			}
		}
	
		public String getSessionName() {
			return sessionId + " " + client1.getInetAddress().getHostName()+" " + 
		client1.getInetAddress().getHostAddress();
		}
		}
