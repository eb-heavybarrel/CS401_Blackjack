package main;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
//import java.util.*;
//import java.util.List;
import java.util.Scanner;

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
	
	public void loadUsers() {
		try {
			Scanner scanner = new Scanner("");
			File folder = new File(System.getProperty("user.dir"));
			File[] listOfFiles = folder.listFiles();

			if (listOfFiles != null) {
				for (File file : listOfFiles) {
					if (file.isFile() && file.getName().startsWith("User.")) {
						// String filename = "User." + userName + ".txt";
						// String fullPath = folder + "\\" + filename;
						// File file = new File(fullPath);
						scanner = new Scanner(file);

						while (scanner.hasNextLine()) {
							String data = scanner.nextLine();
							// System.out.println(data); //troubleshooting
							String[] dataArray = data.split(","); // Split using
																	// a comma

							// skip empty lines. If invalid data from file is
							// detected, stop processing.
							if (dataArray[0] == "") {
								continue;
							} else if (dataArray.length != 4) {
								System.out.println("Corrupted file: skipping " + file);
								continue;
							}

							String userNameString = dataArray[0];
							String passwordString = dataArray[1];
							String roleString = dataArray[2];
							UserRole roleRole = UserRole.valueOf(roleString);
							String creditsString = dataArray[3];
							float creditsFloat = Float.parseFloat(creditsString);

							User user = new User(userNameString, passwordString, roleRole,
									creditsFloat);

							for (int i = 0; i < users.size(); i++) {
								if (users.get(i).equals(user)) {
									continue;
								} else {
									users.add(user);
								}
							}
						}
					}
				}
			}
			scanner.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) {
		BlackjackServer blackjackServer = BlackjackServer.getInstance();
		blackjackServer.loadUsers();
		
//		User Paul = new User("paul", "player123");
//		users.add(Paul);
//		User Nick = new User("nick", "dealer123", UserRole.DEALER, 1000f);
//		users.add(Nick);
//		User Manny = new User("manny", "admin123", UserRole.DEVELOPER, 1000f);
//		users.add(Manny);
		
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