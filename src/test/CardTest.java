package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.Card;
import main.CardSuit;
import main.CardValue;

class CardTest {

	@Test
	void TestCardSuit() {
		Card clubs = new Card(CardSuit.CLUBS, null, null);
		Card spades = new Card(CardSuit.SPADES, null, null);
		Card diamonds = new Card(CardSuit.DIAMONDS, null, null);
		Card hearts = new Card(CardSuit.HEARTS, null, null);
		
		assertEquals(clubs.getSuit(), CardSuit.CLUBS);
		assertEquals(spades.getSuit(), CardSuit.SPADES);
		assertEquals(diamonds.getSuit(), CardSuit.DIAMONDS);
		assertEquals(hearts.getSuit(), CardSuit.HEARTS);
	}
	
	@Test
	void TestCardValue() {
		Card two = new Card(null, CardValue.TWO, null);
		Card three = new Card(null, CardValue.THREE, null);
		Card four = new Card(null, CardValue.FOUR, null);
		Card five = new Card(null, CardValue.FIVE, null);
		Card six = new Card(null, CardValue.SIX, null);
		Card seven = new Card(null, CardValue.SEVEN, null);
		Card eight = new Card(null, CardValue.EIGHT, null);
		Card nine = new Card(null, CardValue.NINE, null);
		Card ten = new Card(null, CardValue.TEN, null);
		Card jack = new Card(null, CardValue.JACK, null);
		Card queen = new Card(null, CardValue.QUEEN, null);
		Card king = new Card(null, CardValue.KING, null);
		Card ace = new Card(null, CardValue.ACE, null);
		
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
