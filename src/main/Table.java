package main;

public class Table {
	private int dealerID;
	private User[] players = new User[7];
	private int shoeSize;
	private int playerCount = 0;
	private int initialTime;
	private int minBet;
	private int maxBet;
	
	public Table(int dealerID, int shoeSize, int intialTime, int minBet, int maxBet) {
		this.dealerID = dealerID;
		this.shoeSize = shoeSize;
		this.initialTime = initialTime;
		this.minBet = minBet;
		this.maxBet = maxBet;
	}
	
	public void AddUser(User player) {
		players[playerCount] = player;
		playerCount++;
	}
	public boolean IsFull() {
		return playerCount == 7;
	}
	
	public int getPlayerCount() {
		return this.playerCount;
	}
	public int getInitialTime() {
		return this.initialTime;
	}
	public int getMinBet() {
		return this.minBet;
	}
	public int getMaxBet() {
		return this.maxBet;
	}
}
