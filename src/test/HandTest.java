package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.Hand;
import main.Card;
import main.CardSuit;
import main.CardValue;

class HandTest {

	@Test
	void testTwoAces() {
		Hand hand = new Hand();
		Card ace = new Card(CardSuit.CLUBS, CardValue.ACE);
		Card ace2 = new Card(CardSuit.DIAMONDS, CardValue.ACE);
		
		hand.addCard(ace);
		hand.addCard(ace2);
		
		assertEquals(hand.getHandValue(), 12);
	}

}
