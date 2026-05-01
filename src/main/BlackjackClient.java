package main;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
//import java.net.UnknownHostException;
import java.util.Scanner;

public class BlackjackClient {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Scanner sc = new Scanner(System.in);
		String host;
		int port = 2121;
		//String message;

		System.out.println("Enter host ip address: ");
		host = sc.nextLine();
		
		if (host.equals("")) return;
//		
//		System.out.println("Enter port: ");
//		port = sc.nextInt();
//		
//		if (port != 1234) return;
		
		Socket socket = new Socket(host, port);
		
		ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());
		
		MessageClass mClass = MessageClass.USER;
		MessageType mType = MessageType.USER_LOGIN;
		
		List<Object> mData = new ArrayList<>();
		System.out.println("Enter UserName: ");
		String userName = sc.nextLine();
		
		System.out.println("Enter Password: ");
		String password = sc.nextLine();

		mData.add(userName);
		mData.add(password);
		
		Message loginMessage = new Message(mClass, mType, mData);
		objOut.writeObject(loginMessage);
		objOut.flush();
		
		Message response = (Message) objIn.readObject();
		
		if (response != null) {
			System.out.println("Message reiceved");
		}

//		if (response.status != MessageStatus.SUCCESS) {
//			socket.close();
//			return;
//		} else {
//			printMessage(response);
//			while (true) {
//				System.out.println("Write text message. Enter \"logout\" to exit: ");
//				message = sc.nextLine();
//
//				if (message.equalsIgnoreCase("logout")) {
//					objOut.writeObject(new Message(MessageType.LOGOUT));
//					objOut.flush();
//					
//					Message logoutResponse = (Message) objIn.readObject();
//					printMessage(logoutResponse);
//					
//					socket.close();
//					break;
//				} else {
//					objOut.writeObject(new Message(MessageType.TEXT, MessageStatus.SENT, message));
//					objOut.flush();
//					
//					Message serverResponse = (Message) objIn.readObject();
//					printMessage(serverResponse);
//				}
//			}
//		}

	}

//	private static void printMessage(Message msg) {
//		System.out.println("Type: " + msg.type);
//		System.out.println("Status: " + msg.status);
//		System.out.println("Value: " + msg.value);
//		System.out.println();
//	}
}

