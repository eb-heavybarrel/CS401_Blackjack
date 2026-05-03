package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.Message;
import main.MessageClass;
import main.MessageType;
import main.User;
import main.MessageStatus;
import main.MessageHandler;



class MessageTest {

	private Message message1;
	private Message message2;
	String userName;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		List<Object> data = new ArrayList<>();
		userName = "Pippin";
		data.add(userName);
		message1 = new Message(MessageClass.USER,MessageType.USER_LOGIN, data);
		message2 = new Message(MessageClass.USER,MessageType.USER_LOGIN, MessageStatus.REQUEST , data);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetmClass() {
		MessageClass mClass = message1.getmClass();
		assertSame(MessageClass.USER, mClass);
	}

	@Test
	void testGetmType() {
		MessageType mType = message1.getmType();
		assertSame(MessageType.USER_LOGIN, mType);
	}

	@Test
	void testGetmStatus() {
		MessageStatus mStatus1 = message1.getmStatus();
		MessageStatus mStatus2 = message2.getmStatus();
		
		assertAll("Message Status",
				() -> assertSame(MessageStatus.UNDEFINED, mStatus1),
				() -> assertSame(MessageStatus.REQUEST, mStatus2));
	}

	@Test
	void testGetmData() {
		List<Object> testData = new ArrayList<>();
		testData = message1.getmData();
		String testUserName = (String) testData.get(0);
		assertSame(userName, testUserName);
	}

}
