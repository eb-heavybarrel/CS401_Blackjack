package main;

public class User {
	//public static int count = 0;
	//private int ID;
	private String userName;
	private String password;
	private UserRole role;
	private float credits;
	private boolean isLoggedIn = false;
	
	public User(String name, String pwd) {
		//this.ID = ++count;
		this.userName = name;
		this.password = pwd;
		this.role = UserRole.PLAYER;
		this.credits = 0;
		this.isLoggedIn = false;
	}
	
	public User(String name, String pwd, UserRole role) {
		//this.ID = ++count;
		this.userName = name;
		this.password = pwd;
		this.role = role;
		this.credits = 0;
		this.isLoggedIn = false;
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
		if (password.equals(pwd)) {
			isLoggedIn = true;
			//do we need trigger a message to client here  with userRole,Credits
		}
		return isLoggedIn;
	}
		
	public boolean logout() {
			this.isLoggedIn = false;
			return isLoggedIn;
		}
	
}
