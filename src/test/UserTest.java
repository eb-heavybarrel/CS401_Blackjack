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
		dealer = new User("dealer", "password123", UserRole.DEALER, 1000f);
		//dealer.incrementCredits(50.3f);
		developer = new User("developer", "luggagecobination", UserRole.DEVELOPER, 1000f);
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
		float expectedCredits = 1000f;
		assertEquals(expectedCredits, player.getCredits());
	}
	
	@Test
	void getLoginStatus() {
		assertFalse(player.getLoginStatus());
	}

	@Test
	void testIncrementCredits() {
		float startingCredits = 1000.f;
		float addCredits = 21.3f;
		player.incrementCredits(addCredits);
		assertEquals(startingCredits + addCredits, player.getCredits());
	}

	@Test
	void testDecrementCredits() {
		float startingCredits = 1000.f;
		float creditsremoved = 21.3f;
		float finalCredits = startingCredits - creditsremoved;
		//dealer.incrementCredits(startingCredits);
		dealer.decrementCredits(creditsremoved);
		assertEquals(finalCredits, dealer.getCredits(), 0.1f);
	}

	@Test
	void testChangePassword() {
		String oldPassword = "luggagecobination";
		assertAll("Change Password",
			() -> assertTrue(developer.changePassword(oldPassword, "NewPassword")),
			() -> assertFalse(dealer.changePassword(oldPassword, "NewPassword")));
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
		assertFalse(player.getLoginStatus());
	}

}
