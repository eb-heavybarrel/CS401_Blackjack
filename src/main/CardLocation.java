package main;


public enum CardLocation {
	OUT_OF_PLAY("out of play", -1), //card has been played since the last time the shoe was shuffled.
	DEALER("dealer", 0),  //Card is in dealers hand
	PLAYER1("player1", 1), //Card is in playe1's hand
	PLAYER2("player2", 2), //...
	PLAYER3("player3", 3),
	PLAYER4("player4", 4),
	PLAYER5("player5", 5),
	PLAYER6("player6", 6),
	UNPLAYED("shoe", 7);
	
    String locationName;
    int locationNumber;

    CardLocation(String locationName, int locationNumber) {
        this.locationName = locationName;
        this.locationNumber = locationNumber;
    }
    
    public String getLocationName() {
        return this.locationName;
    }
    
    public int getlocationNumber() {
        return this.locationNumber;
    }
}
