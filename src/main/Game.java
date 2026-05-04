package main;


public class Game {
	private Table table;
	
	private Hand dealerHand;
	
	private Shoe shoe;
	public boolean canBet;
	
	public Game(Table table) {
		this.table = table;
		this.shoe = table.getShoe();
		
		dealerHand = new Hand();
	}
	
	public Hand DealerHand() {
		return dealerHand;
	}
	
	public void hit(int seatIndex) {
		Card card = shoe.deal();
		if (seatIndex == -1) {
			dealerHand.addCard(card);
			return;
		}
		table.getPlayer(seatIndex).getHand().addCard(card);	
	}
	
	public void stand() {
		
	}
	
	public void bet() {
		
	}
}
