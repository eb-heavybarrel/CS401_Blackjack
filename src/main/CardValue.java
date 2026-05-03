package main;

public enum CardValue {
	TWO ("2", 2),
	THREE ("3", 3),
	FOUR ("4", 4),
	FIVE ("5", 5),
	SIX ("6", 6),
	SEVEN ("7", 7),
	EIGHT ("8", 8),
	NINE ("9", 9),
	TEN ("10", 10),
	JACK ("Jack", 10),
	QUEEN ("Queen", 10),
	KING ("King", 10), 
	ACE ("Ace", 11);
	
	String valueString;
	int valueInt;
	
	CardValue(String valueString, int valueInt) {
		this.valueString = valueString;
		this.valueInt = valueInt;
	}
	
	public String getValueString() {
		return valueString;
	}
	
	public int getValueInt() {
		return valueInt;
	}
}
