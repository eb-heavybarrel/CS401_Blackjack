package main;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
//import java.util.*;
//import java.util.List;

public class BlackjackServer {

	private static final BlackjackServer instance = new BlackjackServer();
	static ArrayList<User> users = new ArrayList<>();
	// ArrayList<Table> tables = new ArrayList<>();

	private BlackjackServer() {
	}

	public static BlackjackServer getInstance() {
		return instance;
	}
	
	public User userAccount(String username, String password) {
	    for (User user : users) {
	        // login for existing account
	        if (user.getUserName().equals(username)) {
	            if (user.login(password)) {
	                System.out.println("User Login: Successful"); // troubleshooting
	                return user;
	            } else {
	                System.out.println("User Login: failed - incorrect password."); //troubleshooting
	                return null;
	            }
	        }
	    }
	    // create account if it doesn't exist
	    User newUser = new User(username, password);
	    users.add(newUser);
	    System.out.println("User Account Created"); // troubleshooting
	    return newUser;
	}

	public static void main(String[] args) {
		BlackjackServer.getInstance();

		User Paul = new User("paul", "player123");
		users.add(Paul);
		User Nick = new User("nick", "dealer123", UserRole.DEALER);
		users.add(Nick);
		User Manny = new User("manny", "admin123", UserRole.DEVELOPER);
		users.add(Manny);
		
//		//troubleshooting
//		for (User user : users) {
//			System.out.println(user.getUserName());
//		}
		
		int port = 2121;

		try (ServerSocket server = new ServerSocket(port)) {
			server.setReuseAddress(true);
			System.out.println("ServerSocket awaiting connections on port " + port + " ...");

			while (true) {
				Socket client = server.accept();
				System.out.println(
						"Connection from " + client.getInetAddress().getHostAddress() + ":" + client.getPort());

				ClientHandler clientSocket = new ClientHandler(client);
				new Thread(clientSocket).start();
			}
		} catch (IOException e) {
			System.out.println("ServerSocket error in BlackjackServer/main ");
			e.printStackTrace();
		}
	}
}