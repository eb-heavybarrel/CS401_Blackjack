package main;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
//import java.util.ArrayList;
import java.util.List;

class ClientHandler implements Runnable {
	private Socket clientSocket;

	// Constructor
	public ClientHandler(Socket socket) {
		this.clientSocket = socket;
	}

	public void run() {
		BlackjackServer instance = BlackjackServer.getInstance();
		User user = null;

		// Input Steams
		ObjectInputStream objIn = null;
		InputStream in = null;

		// Output Streams
		ObjectOutputStream objOut = null;
		OutputStream out = null;

		try {			
			in = clientSocket.getInputStream();
			out = clientSocket.getOutputStream();

			objOut = new ObjectOutputStream(out);
			objOut.flush();
			objIn = new ObjectInputStream(in);
			
			while (true) {
			    Message inMsg;
			    try {
			        inMsg = (Message) objIn.readObject();
			    } catch (EOFException e) {
			        System.out.println("Client disconnected: " + clientSocket.getRemoteSocketAddress());
			        break;
			    }
				Message outMsg = null;
			
				if (user == null) {
					if (inMsg.mClass == MessageClass.USER && inMsg.mType == MessageType.USER_LOGIN) {
			
						String username = (String) inMsg.mData.get(0);
						String password = (String) inMsg.mData.get(1);
			
						user = instance.userAccount(username, password);
			
						if (user != null) {
							//user.saveUser();
							outMsg = new Message(MessageClass.USER, MessageType.USER_LOGIN,
									MessageStatus.SUCCESS, List.of(user));
						} else {
							outMsg = new Message(MessageClass.USER, MessageType.USER_LOGIN,
									MessageStatus.FAILED, null);
						}
			
						objOut.writeObject(outMsg);
						objOut.flush();
					}
				}
				
				//planned for use later, adding multiple input and output messages
//				else {
//					// handle other messages
//					MessageHandler messageHandler = new MessageHandler();
//					// Send all pending outbound messages to client before managing inbound messages;
//					if (outMsg != null) {
//						messageHandler.handleClass(outMsg);
//					}
//			
//					// handle one pending inbound message from client before and return to outbound messages;
//					if (inMsg != null) {
//						messageHandler.handleClass(inMsg);
//						break;
//					}
//				}
			}		
		} catch (IOException e) {
			System.out.println("Error while trying to read object");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Error while trying to find type of object");
			e.printStackTrace();
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
