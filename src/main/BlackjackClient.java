package main;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BlackjackClient {

	public static void main(String[] args) {
		User user = null;
		Scanner sc = new Scanner(System.in);
		String host;
		int port = 2121;

		// System.out.println("Enter host IP address: ");
		// host = sc.nextLine();
		host = "192.168.1.101"; // hardcoding IP address while testing
		if (host.equals(""))
			return;

		try (Socket socket = new Socket(host, port)) {
			ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
			objOut.flush();
			ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());

			// user login/create account
			while (user == null) {
				MessageClass mClass = MessageClass.USER;
				MessageType mType = MessageType.USER_LOGIN;

				List<Object> mData = new ArrayList<>();
				System.out.println("Enter UserName: ");
				String userName = sc.nextLine();
				mData.add(userName);

				System.out.println("Enter Password: ");
				String password = sc.nextLine();
				mData.add(password);

				Message loginMessage = new Message(mClass, mType, mData);
				objOut.writeObject(loginMessage);
				objOut.flush();

				Message response = (Message) objIn.readObject();

				if (response != null) {
					System.out.println("Message received"); // troubleshooting
					if (response.mType == MessageType.USER_LOGIN
							&& response.mStatus == MessageStatus.SUCCESS) {
						user = (User) response.mData.get(0);
						userName = user.getUserName();
						System.out.println("User " + userName + " Login successful:"); // troubleshooting
					} else {
						System.out.println("User Login: failed - incorrect password"); // troubleshooting
					}
				}
			}

			//System.out.println("passed login stage"); // troubleshooting
			
			while(true) {
						
	            try {
					Thread.sleep(30000);
		            System.out.println("Waiting for 30 seconds...");
				} catch (InterruptedException e) {
					System.out.println("Timer interupted");
					e.printStackTrace();
				}

//				objOut.writeObject();
//				objOut.flush();
			}
			

		} catch (IOException e) {
			System.out.println("Error while trying to read object");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Error while trying to find type of object");
			e.printStackTrace();
		}
	}
}
