package main;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
//import java.util.*;
//import java.util.List;

public class BlackjackServer {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		ServerSocket server = null;
		int port = 2121;
		server = new ServerSocket(port);
		server.setReuseAddress(true);
		System.out.println("ServerSocket awaiting connections on port " + port + " ...");

		while (true) {
			Socket client = server.accept();
			System.out.println("Connection from " + client.getInetAddress().getHostAddress() 
					+ ":" + client.getPort());

			ClientHandler clientSocket = new ClientHandler(client);
			new Thread(clientSocket).start();
		}
	}
}

class ClientHandler implements Runnable {
	private final Socket clientSocket;

	//private boolean login = false;

	// Constructor
	public ClientHandler(Socket socket) {
		this.clientSocket = socket;
	}

	public void run() {
		ArrayList<User> users = new ArrayList<>();
		//ArrayList<Table> tables = new ArrayList<>();
		
		User Paul = new User("Paul","password123");
		users.add(Paul);
		User Nick = new User("Nick", "qwerty123", UserRole.DEALER);
		users.add(Nick);
		User Manny = new User("Manny", "admin123", UserRole.DEVELOPER);
		users.add(Manny);
		

		// Input Steams
		ObjectInputStream objIn = null;
		InputStream in = null;

		// Output Streams
		ObjectOutputStream objOut = null;
		OutputStream out = null;
		
		MessageHandler messageHandler = new MessageHandler(users);

		try {
			in = clientSocket.getInputStream();
			out = clientSocket.getOutputStream();

			objIn = new ObjectInputStream(in);
			objOut = new ObjectOutputStream(out);
			while (true) {
				Message msg = (Message) objIn.readObject();
				messageHandler.handleClass(msg);
				
//				if (!login) {
//					if (msg.type == MessageType.LOGIN) {
//						login = true;
//						objOut.writeObject(new Message(msg.type, MessageStatus.SUCCESS, "LOGIN SUCCESSFUL"));
//						objOut.flush();
//					} else {
//						objOut.writeObject(new Message(msg.type, MessageStatus.FAILED, "LOGIN BEFORE SENDING ANY MESSAGES"));
//					}
//				} else {
//					if (msg.type == MessageType.TEXT) {
//						objOut.writeObject(new Message(msg.type, MessageStatus.RECEIVED, msg.value.toUpperCase()));
//						objOut.flush();
//						printMessage(msg);
//					} else if (msg.type == MessageType.LOGOUT) {
//						objOut.writeObject(new Message(msg.type, MessageStatus.SUCCESS, "LOGOUT SUCCESSFUL"));
//						objOut.flush();
//						break;
//					}
//				}
			}
			//clientSocket.close();
		} catch (IOException e) {
			System.out.println("Error while trying to read object");
		} catch (ClassNotFoundException e) {
			System.out.println("Error while trying to find type of object");
		}
	}
	
//	public void printMessage(Message msg){
//		System.out.println("Type: " + msg.type);
//		System.out.println("Status: " + msg.status);
//		System.out.println("Value: " + msg.value);
//		System.out.println();
//	}
}
