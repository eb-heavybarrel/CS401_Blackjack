package main;

public enum MessageType {

// Only Objects and methods that need to be passed back and forth between server
// and client need to be added to MessageType.
	
// Enum naming convention
// client to server: CLASS_METHOD
// server to client: CLASS_OBJECT
	
	
// USER Objects:
	//getters
//	USER_ID,
//	USER_NAME,
//	USER_PASSWORD,
	USER_ROLE,
	USER_CREDITS,
// USER Methods;:
	USER_lOGOUT,
	USER_LOGIN,
	USER_INC_CREDIT,
	USER_DEC_CREDIT,
	
//	TABLE_DEALERID,
//	TABLE_PLAYERID,
//	TABLE_SHOE_SIZE,
//	TABLE_PLAYER_COUNT,
//	TABLE_IS_FULL,
//	TABLE_TIME_LIMIT,
//	TABLE_MIN_BET,
//	TABLE_MAX_BET,
//	GAME_TIMER_START,
//	GAME_TIMER_FINISH,
//	GAME_PHASE,
//	GAME_CAN_BET,
//	HAND_PLAYERID,
//	HAND_BET,
//	HAND_CARD,
//	SHOE,
	//remove these once message.java has been updated
	LOGIN,
	TEXT,
	LOGOUT
}