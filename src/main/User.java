package main;

public class User {
	public static int count = 0;
	private int ID;
	private String userName;
	private String password;
	private UserRole role;
	private float credits;
	
	public User(String name, String pwd, UserRole role) {
		this.ID = ++count;
		this.userName = name;
		this.password = pwd;
		this.role = role;
		this.credits = 0;
	}

	//Setters
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	//Getters
	public int getID() {
		return ID;
	}

	public String getUserName() {
		return userName;
	}

	public UserRole getRole() {
		return role;
	}

	public float getCredits() {
		return credits;
	}
	
	//Incrementers
	public void incrementCredits(float credits) {
		this.credits += credits;
	}

	public void decrementCredits(float credits) {
		this.credits -= credits;
	}
	
	//Others
	public void changePassword(String oldPwd, String newPwd) {
		if (oldPwd.equals(password)) {
			password = newPwd;
		}

	}
	
	public void loadUser(String userName) {
		//needs to be implemented
	}
		
	public void saveUser(String userName) {
		//needs to be implemented
	}
	
	public boolean authenicateUser(String userName, String pwd) {
		if (this.userName.equals(userName) && this.password.equals(pwd)) {
			return true;
		}
		
		return false;
	}
	
}
