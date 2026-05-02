package main;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;  //Eclipse suggests we need this
	//public static int count = 0;
	//private int ID;
	private String userName;
	private String password;
	private UserRole role;
	private float credits = 1000.0f;
	private boolean isLoggedIn = false;
	
	public User(String name, String pwd) {
		//this.ID = ++count;
		this.userName = name;
		this.password = pwd;
		this.role = UserRole.PLAYER;
	}
	
	public User(String name, String pwd, UserRole role) {
		//this.ID = ++count;
		this.userName = name;
		this.password = pwd;
		this.role = role;
	}

	//Setters
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	//Getters
//	public int getID() {
//		return ID;
//	}

	public String getUserName() {
		return userName;
	}

	public UserRole getRole() {
		return role;
	}

	public float getCredits() {
		return credits;
		//how do we need trigger a message to client here current Credits
	}
	
	public boolean getLoginStatus() {
		return isLoggedIn;
	}
	
	//Incrementers
	
	//need to check for positive float
	public void incrementCredits(float credits) {
		this.credits += credits;
	}

	//need to check for positive float
	public void decrementCredits(float credits) {
		this.credits -= credits;
	}
	
	//Others
	public boolean changePassword(String oldPwd, String newPwd) {
		if (oldPwd.equals(password)) {
			password = newPwd;
			return true;
		}
		return false;
	}
	
	public void loadUser(String userName) {
		//needs to be implemented
	}
		
	public void saveUser(String userName) {
		//needs to be implemented
	}
	
	public boolean login(String pwd) {
		System.out.println("User.login ran"); //troubleshooting
	    isLoggedIn = password.equals(pwd);
	    return isLoggedIn;
	}
		
	public boolean logout() {
			this.isLoggedIn = false;
			return isLoggedIn;
		}
	
}
