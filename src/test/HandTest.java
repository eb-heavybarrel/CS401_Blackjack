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
	
	@Test
	void testBlackJack() {
		Hand a = new Hand();
		Hand b = new Hand();
		Hand c = new Hand();
		Hand d = new Hand();
		
		Card ace = new Card(CardSuit.CLUBS, CardValue.ACE);
		Card king = new Card(CardSuit.DIAMONDS, CardValue.KING);
		Card queen = new Card(CardSuit.HEARTS, CardValue.QUEEN);
		Card jack = new Card(CardSuit.SPADES, CardValue.JACK);
		Card ten = new Card(CardSuit.HEARTS, CardValue.TEN);
		
		a.addCard(ace);
		a.addCard(ten);
		
		b.addCard(ace);
		b.addCard(king);
		
		c.addCard(ace);
		c.addCard(queen);
		
		d.addCard(ace);
		d.addCard(jack);
		
		assertTrue(a.hasBlackJack());
		assertTrue(b.hasBlackJack());
		assertTrue(c.hasBlackJack());
		assertTrue(d.hasBlackJack());
	}

}
