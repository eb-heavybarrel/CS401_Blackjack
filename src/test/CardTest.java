package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.Card;
import main.CardSuit;
import main.CardValue;

class CardTest {

	@Test
	void TestCardSuit() {
		Card clubs = new Card(CardSuit.CLUBS, null);
		Card spades = new Card(CardSuit.SPADES, null);
		Card diamonds = new Card(CardSuit.DIAMONDS, null);
		Card hearts = new Card(CardSuit.HEARTS, null);
		
		assertEquals(clubs.getSuit(), CardSuit.CLUBS);
		assertEquals(spades.getSuit(), CardSuit.SPADES);
		assertEquals(diamonds.getSuit(), CardSuit.DIAMONDS);
		assertEquals(hearts.getSuit(), CardSuit.HEARTS);
	}
	
	@Test
	void TestCardValue() {
		Card two = new Card(null, CardValue.TWO);
		Card three = new Card(null, CardValue.THREE);
		Card four = new Card(null, CardValue.FOUR);
		Card five = new Card(null, CardValue.FIVE);
		Card six = new Card(null, CardValue.SIX);
		Card seven = new Card(null, CardValue.SEVEN);
		Card eight = new Card(null, CardValue.EIGHT);
		Card nine = new Card(null, CardValue.NINE);
		Card ten = new Card(null, CardValue.TEN);
		Card jack = new Card(null, CardValue.JACK);
		Card queen = new Card(null, CardValue.QUEEN);
		Card king = new Card(null, CardValue.KING);
		Card ace = new Card(null, CardValue.ACE);
		
		assertEquals(two.getValue(), CardValue.TWO);
		assertEquals(three.getValue(), CardValue.THREE);
		assertEquals(four.getValue(), CardValue.FOUR);
		assertEquals(five.getValue(), CardValue.FIVE);
		assertEquals(six.getValue(), CardValue.SIX);
		assertEquals(seven.getValue(), CardValue.SEVEN);
		assertEquals(eight.getValue(), CardValue.EIGHT);
		assertEquals(nine.getValue(), CardValue.NINE);
		assertEquals(ten.getValue(), CardValue.TEN);
		assertEquals(jack.getValue(), CardValue.JACK);
		assertEquals(queen.getValue(), CardValue.QUEEN);
		assertEquals(king.getValue(), CardValue.KING);
		assertEquals(ace.getValue(), CardValue.ACE);
	}

}
