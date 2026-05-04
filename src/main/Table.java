package main;

public class Table {

	private Player[] players = new Player[6];
	private int playerCount;

	private int dealerID;
	private int shoeSize;
	private int initialTime;
	private int minBet;
	private int maxBet;
	private Game game;
	private Shoe shoe;

	// default constructor
	public Table() {
		this.dealerID = 0;
		this.shoeSize = 3;
		this.initialTime = 30;
		this.minBet = 50;
		this.maxBet = 1000;
		this.playerCount = 0;

		this.shoe = new Shoe(shoeSize);
		game = new Game(this);
	}

	public Table(int dealerID, int shoeSize, int intialTime, int minBet, int maxBet) {
		this.dealerID = dealerID;
		this.shoeSize = shoeSize;
		this.initialTime = initialTime;
		this.minBet = minBet;
		this.maxBet = maxBet;
		this.playerCount = 0;

		this.shoe = new Shoe(shoeSize);
		game = new Game(this);
	}
	
	public Shoe getShoe() {
		return this.shoe;
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

	public int getPlayerCount() {
		return playerCount;
	}

	public boolean isFull() {
		return playerCount == players.length;
	}

	public Player getPlayer(int seatIndex) {
		return players[seatIndex];
	}
	
	public Game getGame() {
		return game;
	}

	public Player getPlayer(String userName) {
		for (Player p : players) {
			if (p != null && p.UserName().equals(userName)) {
				return p;
			}
		}
		return null;
	}

	public void PlayerJoin(String userName) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] == null) {
				Player player = new Player(userName, i);
				players[i] = player;
				playerCount++;
				return;
			}
		}
	}

	public void PlayerLeave(String userName) {
		for (int i = 0; i < players.length; i++) {
			if (players[i] != null && players[i].UserName().equals(userName)) {
				players[i] = null;
				playerCount--;
				return;
			}
		}
	}
}
