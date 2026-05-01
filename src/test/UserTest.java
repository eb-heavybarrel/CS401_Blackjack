package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.User;
import main.UserRole;

class UserTest {
	
	private User player;
	private User dealer;
	private User developer;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		player = new User("player", "superSecret");
		dealer = new User("dealer", "password123", UserRole.DEALER);
		dealer.incrementCredits(50.3f);
		developer = new User("developer", "luggagecobination", UserRole.DEVELOPER);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testSetUserName() {
		String username = "player2";
		player.setUserName(username);
		assertSame(username, player.getUserName());
	}

	@Test
	void testSetRole() {
		UserRole userRole = UserRole.DEVELOPER;
		player.setRole(userRole);
		assertSame(userRole, player.getRole());
	}

	@Test
	void testGetUserName() {
		String username = "developer";
		assertSame(username, developer.getUserName());
	}

	@Test
	void testGetRole() {
	    UserRole expectedRole = UserRole.DEALER;
	    assertSame(expectedRole, dealer.getRole());
	}

	@Test
	void testGetCredits() {
		float expectedCredits = 0;
		assertEquals(expectedCredits, player.getCredits());
	}
	
	@Test
	void getLoginStatus() {
		assertFalse(player.getLoginStatus());
	}

	@Test
	void testIncrementCredits() {
		float startingCredits = 0;
		float addCredits = 21.3f;
		player.incrementCredits(addCredits);
		assertEquals(startingCredits + addCredits, player.getCredits());
	}

	@Test
	void testDecrementCredits() {
		float startingCredits = 50.3f;
		float removeCredits = 21.3f;
		dealer.incrementCredits(removeCredits);
		assertEquals(startingCredits + removeCredits, player.getCredits());
	}

	@Test
	void testChangePassword() {
		String oldPassword = "luggagecobination";
		assertAll("Change Password",
			() -> assertTrue(developer.changePassword(oldPassword, "NewPassword")),
			() -> assertFalse(dealer.changePassword(oldPassword, "NewPassword")));
	}

	@Test
	void testLoadUser() {
	}

	@Test
	void testSaveUser() {
	}

	@Test
	void testLogin() {
		String password = "superSecret";
		player.login(password);
		assertTrue(player.getLoginStatus());
	}

	@Test
	void testLogout() {
		String password = "superSecret";
		player.login(password);
		if (player.getLoginStatus()) {
			player.logout();
		}
		assertTrue(player.getLoginStatus());
	}

}
