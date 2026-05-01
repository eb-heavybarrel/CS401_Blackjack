package main;

import java.util.List;

public class MessageHandler {
	//These variables may move to server later.
	List<User> users;
	//List<Table> tables;
	

	//begin--------------- Verify Messages ---------------//
	
	//verify message length and that each object is of the correct type.
    private String verifyMessage(Message message, int size, List<Class<?>> types) {

        String sizeResult = verifyMessageSize(message, size);
        String typeResult = verifyMessageTypes(message, size, types);
        
        if (sizeResult.equals("true") && typeResult.equals("true")){
        	return "true";
        }
        if (sizeResult.equals("true")) {
        	return typeResult;
        }
        if (typeResult.equals("true")) {
        	return sizeResult;
        }
        
        String combinedResults = sizeResult + typeResult;
        return combinedResults;
    }
    
    private String verifyMessageSize(Message message, int size) {
        List<Object> data = message.getmData();
        
        if (data == null) {
            return "Error: Message data is null.\n";
        }

        if (data.size() != size) {
            return "Error: Expected " + size + " arguments.\n";
        }
        return "true";
    }
    
    private String verifyMessageTypes(Message message, int size, List<Class<?>> types) {
        List<Object> data = message.getmData();
        
        if (data == null) {
            return "Error: Message data is null.\n";
        }
        
        for (int i = 0; i < size; i++) {
            if (!types.get(i).isInstance(data.get(i))) {
               return  "Error: Index " + i + " must be of type "
                	+ types.get(i).getSimpleName() + ".\n";
            }
        }
        return "true";
    }
    
	//end--------------- Verify Messages ---------------//

    
    //Handle Class -----
    
	public void handleClass(Message message) {
        switch (message.getmClass()) {
            case MessageClass.USER:
                handleUser(message);
                break;
//            case MessageClass.TABLE:
//                handleTable(message);
//                break;
//            case MessageClass.GAME:
//                handleGame(message);
//                break;
//            case MessageClass.SHOE:
//                handleShoe(message);
//                break;
//            case MessageClass.HAND:
//                handleHand(message);
//                break;
//            case MessageClass.CARD:
//                handleCard(message);
//                break;
            default:
                System.out.println("No handler for message Class: " + message.getmClass());
                break;
        }
    }
    
    
    //begin--------------- Handle by Type ---------------//
    
    //For each handle<Type>;
    //verify message, extract data from message, perform expected actions
    
    //Handle User -----
	private void handleUser(Message message) {
		List<Object> data = message.getmData();
		List<Class<?>> expectedTypes;
		int expectedSize;
		String resultsOfVerify;
		
		User user;
		String username;
		String password;
		UserRole role;
		float credits;

		switch (message.getmType()) {
		case MessageType.USER_CREDITS:
			//verify
			expectedSize = 2;
			expectedTypes = List.of(String.class, float.class);
			resultsOfVerify = verifyMessage(message, expectedSize, expectedTypes);
			if (!resultsOfVerify.equals("true")) {
				//send to logger once complete
				System.out.println("User Credits failed: -----\n" + resultsOfVerify);
			}
			
			//extract data
			username = (String) data.get(0);
			credits = (float) data.get(1);
			
			//actions
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).getUserName().equals(username)) {
					user = users.get(i);
					credits = user.getCredits();
					
					//how do we need trigger a message to client here current Credits
				}
				//is else needed? isloggedIn should remain false.
			}
			break;
		case MessageType.USER_LOGIN:
			//verify
			expectedSize = 2;
			expectedTypes = List.of(String.class, String.class);
			resultsOfVerify = verifyMessage(message, expectedSize, expectedTypes);
			if (!resultsOfVerify.equals("true")) {
				//send to logger once complete
				System.out.println("User Login failed: -----\n" + resultsOfVerify);
			}
			
			//extract data
			username = (String) data.get(0);
			password = (String) data.get(1);
			
			//actions
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).getUserName().equals(username)) {
					user = users.get(i);
					user.login(password);
					//do we need trigger a message to client here  with userRole,Credits
				}
				//is else needed? isloggedIn should remain false.
			}
			break;
		case MessageType.USER_lOGOUT:
			//verify
			expectedSize = 1;
			expectedTypes = List.of(String.class);
			resultsOfVerify = verifyMessage(message, expectedSize, expectedTypes);
			if (!resultsOfVerify.equals("true")) {
				//send to logger once complete
				System.out.println("User Logout failed: -----\n" + resultsOfVerify);
			}
			
			//extract data
			username = (String) data.get(0);
			
			//actions
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).getUserName().equals(username)) {
					user = users.get(i);
					user.logout();
				}
			}
			break;
		 case MessageType.USER_INC_CREDIT:
			expectedSize = 2;
			expectedTypes = List.of(String.class, float.class);
			resultsOfVerify = verifyMessage(message, expectedSize, expectedTypes);
			if (!resultsOfVerify.equals("true")) {
				//send to logger once complete
				System.out.println("User Logout failed: -----\n" + resultsOfVerify);
			}
			
			//extract data
			username = (String) data.get(0);
			credits = (float) data.get(0);
			
			//actions
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).getUserName().equals(username)) {
					user = users.get(i);
					user.incrementCredits(credits);
				}
			}
			break;
		 case MessageType.USER_DEC_CREDIT:
			expectedSize = 2;
			expectedTypes = List.of(String.class, float.class);
			resultsOfVerify = verifyMessage(message, expectedSize, expectedTypes);
			if (!resultsOfVerify.equals("true")) {
				//send to logger once complete
				System.out.println("User Logout failed: -----\n" + resultsOfVerify);
			}
			
			//extract data
			username = (String) data.get(0);
			credits = (float) data.get(0);
			
			//actions
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).getUserName().equals(username)) {
					user = users.get(i);
					user.decrementCredits(credits);
				}
			}
			break;
		default:
			//send to logger once complete
			System.out.println("Handle User Failed: -----\n Unknown message type: "
					+ message.getmType());
			throw new IllegalArgumentException("Handle User Failed: -----\n"
					+ "Unknown message type: " + message.getmType());
		}
	}
	
	//Handle Table -----
//	private void handleTable(Message message) {
//		
//	}
//	
//	//Handle Game -----
//	private void handleGame(Message message) {
//		
//	}
//	
//	//Handle Shoe -----
//	private void handleShoe(Message message) {
//		
//	}
//	
//	//Handle Hand -----
//	private void handleHand(Message message) {
//		
//	}
//	
//	//Handle Card -----
//	private void handleCard(Message message) {
//		
//	}
}
