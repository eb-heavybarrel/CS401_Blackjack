package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.Game;
import main.Table;
import main.Card;
import main.CardSuit;
import main.CardValue;
import main.Hand;
import main.HandState;

class GameTest {
	private String[] players = { "Manuel", "Paul", "Nick" };
	private Table table = new Table();
	private Game game = table.getGame();
	
	private Hand manuel;
	private Hand paul;
	private Hand nick;
	private Hand dealer;
	
	void SetupTable() {
		for (String player : players) {
			table.PlayerJoin(player);
		}
		
		manuel = table.getPlayer(players[0]).getHand();
		paul = table.getPlayer(players[1]).getHand();
		nick = table.getPlayer(players[2]).getHand();
		dealer = game.DealerHand();
	}
	
	@Test
	void testEndConditionPush() {
		SetupTable();

		manuel.addCard(new Card(CardSuit.HEARTS, CardValue.ACE));
		manuel.addCard(new Card(CardSuit.DIAMONDS, CardValue.TEN));

		dealer.addCard(new Card(CardSuit.SPADES, CardValue.ACE));
		dealer.addCard(new Card(CardSuit.CLUBS, CardValue.KING));

		paul.addCard(new Card(CardSuit.HEARTS, CardValue.EIGHT));
		paul.addCard(new Card(CardSuit.DIAMONDS, CardValue.NINE));

		nick.addCard(new Card(CardSuit.HEARTS, CardValue.FIVE));
		nick.addCard(new Card(CardSuit.DIAMONDS, CardValue.TEN));

		game.testDetermineWinner();

		assertEquals(dealer.getHandState(), HandState.PUSH);
	}

	@Test
	void testEndConditionDealerBlackjack() {
		SetupTable();

		manuel.addCard(new Card(CardSuit.HEARTS, CardValue.THREE));
		manuel.addCard(new Card(CardSuit.DIAMONDS, CardValue.FOUR));

		dealer.addCard(new Card(CardSuit.SPADES, CardValue.ACE));
		dealer.addCard(new Card(CardSuit.CLUBS, CardValue.KING));

		paul.addCard(new Card(CardSuit.HEARTS, CardValue.EIGHT));
		paul.addCard(new Card(CardSuit.DIAMONDS, CardValue.NINE));

		nick.addCard(new Card(CardSuit.HEARTS, CardValue.FIVE));
		nick.addCard(new Card(CardSuit.DIAMONDS, CardValue.TEN));

		game.testDetermineWinner();

		assertEquals(dealer.getHandState(), HandState.WINNER);
	}

	@Test
	void testEndConditionPlayerBlackjack() {
		SetupTable();

		manuel.addCard(new Card(CardSuit.HEARTS, CardValue.ACE));
		manuel.addCard(new Card(CardSuit.DIAMONDS, CardValue.QUEEN));
		
		paul.addCard(new Card(CardSuit.CLUBS, CardValue.ACE));
		paul.addCard(new Card(CardSuit.DIAMONDS, CardValue.KING));

		dealer.addCard(new Card(CardSuit.SPADES, CardValue.FOUR));
		dealer.addCard(new Card(CardSuit.CLUBS, CardValue.NINE));

		game.testDetermineWinner();

		assertEquals(manuel.getHandState(), HandState.WINNER);
		assertEquals(paul.getHandState(), HandState.WINNER);
	}
	@Test
	void testPlayerHandGreaterThanDealer() {
		SetupTable();
		
		manuel.addCard(new Card(CardSuit.DIAMONDS, CardValue.NINE));
		nick.addCard(new Card(CardSuit.HEARTS, CardValue.EIGHT));
		
		dealer.addCard(new Card(CardSuit.SPADES, CardValue.THREE));
		
		game.testDetermineWinner();
		
		assertEquals(manuel.getHandState(), HandState.WINNER);
		assertEquals(nick.getHandState(), HandState.WINNER);
	}
	
	@Test
	void testDealerHandGreaterThanPlayer() {
		SetupTable();
		
		manuel.addCard(new Card(CardSuit.DIAMONDS, CardValue.THREE));
		nick.addCard(new Card(CardSuit.HEARTS, CardValue.EIGHT));
		
		dealer.addCard(new Card(CardSuit.SPADES, CardValue.NINE));
		
		game.testDetermineWinner();
		
		assertEquals(dealer.getHandState(), HandState.WINNER);
	}
	
	@Test
	void testDealerEqualToPlayer() {
		SetupTable();
		
		manuel.addCard(new Card(CardSuit.DIAMONDS, CardValue.NINE));
		nick.addCard(new Card(CardSuit.HEARTS, CardValue.NINE));
		paul.addCard(new Card(CardSuit.CLUBS, CardValue.NINE));
		
		dealer.addCard(new Card(CardSuit.SPADES, CardValue.NINE));
		
		game.testDetermineWinner();
		
		assertEquals(dealer.getHandState(), HandState.PUSH);
		assertEquals(manuel.getHandState(), HandState.PUSH);
		assertEquals(nick.getHandState(), HandState.PUSH);
		assertEquals(paul.getHandState(), HandState.PUSH);
	}
	
	@Test
	void testDealer21ToPlayer21() {
		SetupTable();
		
		manuel.addCard(new Card(CardSuit.DIAMONDS, CardValue.TEN));
		manuel.addCard(new Card(CardSuit.HEARTS, CardValue.ACE));
		
		dealer.addCard(new Card(CardSuit.CLUBS, CardValue.TEN));
		dealer.addCard(new Card(CardSuit.SPADES, CardValue.FIVE));
		dealer.addCard(new Card(CardSuit.HEARTS, CardValue.SIX));
		
		game.testDetermineWinner();
		
		assertEquals(dealer.getHandState(), HandState.BUST);
		assertEquals(manuel.getHandState(), HandState.WINNER);
	}
	
	@Test
	void testBust() {
		SetupTable();
		
		manuel.addCard(new Card(CardSuit.DIAMONDS, CardValue.TEN));
		manuel.addCard(new Card(CardSuit.HEARTS, CardValue.ACE));
		
		nick.addCard(new Card(CardSuit.CLUBS, CardValue.SIX));
		nick.addCard(new Card(CardSuit.SPADES, CardValue.NINE));
		nick.addCard(new Card(CardSuit.HEARTS, CardValue.SEVEN));
		
		paul.addCard(new Card(CardSuit.CLUBS, CardValue.EIGHT));
		paul.addCard(new Card(CardSuit.SPADES, CardValue.SEVEN));
		paul.addCard(new Card(CardSuit.HEARTS, CardValue.SEVEN));
		
		dealer.addCard(new Card(CardSuit.CLUBS, CardValue.TEN));
		dealer.addCard(new Card(CardSuit.HEARTS, CardValue.SIX));
		dealer.addCard(new Card(CardSuit.SPADES, CardValue.NINE));
		
		game.testDetermineWinner();
		
		assertEquals(dealer.getHandState(), HandState.BUST);
		assertEquals(nick.getHandState(), HandState.BUST);
		assertEquals(paul.getHandState(), HandState.BUST);
		assertEquals(manuel.getHandState(), HandState.WINNER);
	}
	
	

}
