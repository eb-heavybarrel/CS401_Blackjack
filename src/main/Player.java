package main;

import java.util.ArrayList;

public class Player {
	private String userName;
	// private ArrayList<Hand> hands = new ArrayList();
	private Hand hand = new Hand();
	private int seat;

	public Player(String userName, int seat) {
		this.userName = userName;
		this.seat = seat;

		// Hand hand = new Hand();
		// hands.add(hand);
	}

	public String UserName() {
		return userName;
	}

	public int Seat() {
		return seat;
	}

	public Hand getHand() {
		return hand;
	}
//	public void AddNewHand(Hand hand) {
//		hands.add(hand);
//	}
//	public void ResetHands() {
//		hands.clear();
//	}
}
