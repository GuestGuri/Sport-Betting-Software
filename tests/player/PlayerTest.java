/**
 *
 */
package player;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import fr.uv1.bettingServices.exceptions.AuthenticationException;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.bettingServices.player.Player;

/**
 *
 */
public class PlayerTest {

	private Player defaultPlayer;

	/**
	 * This part runs before each test case
	 * Initializing defaultPlayer
	 * @throws BadParametersException
	 */
	@Before
	public void before() throws BadParametersException {
		defaultPlayer = new Player("Last", "First", "lastFirst", new Date(), 100L);
	}

	/**
	 * Case 1: Object is not defined correctly
	 * 4 argument Constructor
	 * Expecting a BadParametersException to be thrown
	 * Date parameter is passed as null
	 * @throws BadParametersException
	 */
	@Test(expected=BadParametersException.class)
	public void test1() throws BadParametersException {
		new Player("Last", "First", "lastFirst", null);
	}

	/**
	 * Case 2.1: Object is not defined correctly
	 * 5 argument Constructor
	 * Expecting a BadParametersException to be thrown
	 * Date parameter is passed as null
	 * @throws BadParametersException
	 */
	@Test(expected=BadParametersException.class)
	public void test2_1() throws BadParametersException {
		new Player("Last", "First", "lastFirst", null, 5L);
	}

	/**
	 * Case 2.2: Object is not defined correctly
	 * 5 argument Constructor
	 * Expecting a BadParametersException to be thrown
	 * Date parameter is passed as null
	 * @throws BadParametersException
	 */
	@Test(expected=BadParametersException.class)
	public void test2_2() throws BadParametersException {
		new Player("Last", "First", "lastFirst", null, -10L);
	}

	/**
	 * Case 3: Changing user password
	 * changeSubsPwd
	 * Expecting an AuthenticationException to be thrown
	 * Entered password doesn't match the user password
	 * @throws BadParametersException
	 * @throws AuthenticationException
	 */
	@Test(expected=AuthenticationException.class)
	public void test3() throws BadParametersException, AuthenticationException {
		defaultPlayer.changeSubsPwd("1234", "4567");
	}

	/**
	 * Case 4: Credit user
	 * Checks if player credit is updated
	 * @throws BadParametersException
	 */
	@Test
	public void test4() throws BadParametersException {
		long currentCredit = defaultPlayer.getNbTokens();
		long newCredit = defaultPlayer.creditSubscriber(10L);

		assertEquals(newCredit, currentCredit + 10L);

		// Checking if the value of player was really updated
		assertEquals(newCredit, defaultPlayer.getNbTokens());
	}

	/**
	 * Case 5: check test on null value passed as current password parameter to change password
	 * @throws BadParametersException
	 */
	@Test(expected=BadParametersException.class)
	public void test5() throws AuthenticationException, BadParametersException {
		boolean flag = defaultPlayer.changeSubsPwd("newpassword", null);
		assertFalse(flag);
	}

	/**
	 * Case 6: check test on null value passed as new password parameter to change password
	 * @throws BadParametersException
	 */
	@Test(expected=BadParametersException.class)
	public void test6() throws AuthenticationException, BadParametersException {
		boolean flag = defaultPlayer.changeSubsPwd(null, "currentpassword");
		assertFalse(flag);
	}

	/**
	 * Case 7.1: Debit user
	 *
	 * @throws BadParametersException
	 * @throws SubscriberException
	 */
	@Test(expected=SubscriberException.class)
	public void test7_1() throws BadParametersException, SubscriberException {
		defaultPlayer.setNbTokens(0L);
		defaultPlayer.debitSubscriber(150L);
	}

	/**
	 * Case 7.2: Debit user
	 *
	 * @throws BadParametersException
	 * @throws SubscriberException
	 */
	@Test(expected=BadParametersException.class)
	public void test7_2() throws BadParametersException, SubscriberException {
		defaultPlayer.debitSubscriber(-10L);
	}

	/**
	 * Case 7.3: Debit user
	 *
	 * @throws BadParametersException
	 * @throws SubscriberException
	 */
	@Test
	public void test7_3() throws BadParametersException, SubscriberException {
		long newNbOfTokens = defaultPlayer.debitSubscriber(15L);

		assertEquals(85L, newNbOfTokens);

		assertEquals(newNbOfTokens, defaultPlayer.getNbTokens());
	}

	/**
	 * Case 8: Check that password was changed
	 * @throws BadParametersException
	 */
	@Test
	public void test8() throws AuthenticationException, BadParametersException {
		boolean flag = defaultPlayer.changeSubsPwd("1234", "0000");

		assertTrue(flag);
	}

	/**
	 * Case 9.1: authenticate user
	 * @throws BadParametersException
	 * @throws AuthenticationException
	 */
	@Test(expected=BadParametersException.class)
	public void test9_1() throws AuthenticationException, BadParametersException {
		boolean flag = defaultPlayer.authentificatePlayer(null, null);
		assertFalse(flag);
	}

	/**
	 * Case 9.2: authenticate user
	 * @throws BadParametersException
	 * @throws AuthenticationException
	 */
	@Test(expected=BadParametersException.class)
	public void test9_2() throws AuthenticationException, BadParametersException {
		boolean flag = defaultPlayer.authentificatePlayer(null, "0000");
		assertFalse(flag);
	}

	/**
	 * Case 9.3: authenticate user
	 * @throws BadParametersException
	 * @throws AuthenticationException
	 */
	@Test(expected=BadParametersException.class)
	public void test9_3() throws AuthenticationException, BadParametersException {
		boolean flag = defaultPlayer.authentificatePlayer("username", null);
		assertFalse(flag);
	}

	/**
	 * Case 9.4: authenticate user
	 * Bad username, good password
	 * @throws BadParametersException
	 * @throws AuthenticationException
	 */
	@Test(expected=AuthenticationException.class)
	public void test9_4() throws AuthenticationException, BadParametersException {
		boolean flag = defaultPlayer.authentificatePlayer("badUsername", defaultPlayer.getPwdPlayer());
		assertFalse(flag);
	}

	/**
	 * Case 9.5: authenticate user
	 * Bad password, good username
	 * @throws BadParametersException
	 * @throws AuthenticationException
	 */
	@Test(expected=AuthenticationException.class)
	public void test9_5() throws AuthenticationException, BadParametersException {
		boolean flag = defaultPlayer.authentificatePlayer(defaultPlayer.getUserName(), "badPassword");
		assertFalse(flag);
	}

	/**
	 * Case 9.6: authenticate user
	 * good password, good username
	 * @throws BadParametersException
	 * @throws AuthenticationException
	 */
	@Test
	public void test9_6() throws AuthenticationException, BadParametersException {
		boolean flag = defaultPlayer.authentificatePlayer(defaultPlayer.getUserName(), defaultPlayer.getPwdPlayer());

		assertTrue(flag);
	}

	/**
	 * Case 10.1: info plauer
	 */
	@Test
	public void test10_1() {
		ArrayList<String> playerInfo = defaultPlayer.infosSubscriber();

		assertEquals(playerInfo.get(0), defaultPlayer.getLastName());
		assertEquals(playerInfo.get(1), defaultPlayer.getFirstName());
		assertEquals(playerInfo.get(2), defaultPlayer.getUserName());
		assertEquals(playerInfo.get(3), defaultPlayer.getBornDate().toString());
		assertEquals(playerInfo.get(4), new Long(defaultPlayer.getNbTokens()).toString());
	}

	/**
	 * Case 10.2: info player
	 */
	@Test(expected=IndexOutOfBoundsException.class)
	public void test10_2() {
		ArrayList<String> playerInfo = defaultPlayer.infosSubscriber();
		playerInfo.get(5);
	}

	/**
	 * Case 11.1: object equals
	 * @throws BadParametersException
	 */
	@Test
	public void test11_1() throws BadParametersException {
		Player player = new Player(defaultPlayer.getLastName(), defaultPlayer.getLastName(), defaultPlayer.getUserName(), defaultPlayer.getBornDate());

		assertEquals(player, defaultPlayer);
	}

	/**
	 * Case 11.2: object not equals
	 * @throws BadParametersException
	 */
	@Test
	public void test11_2() throws BadParametersException {
		Player player = new Player("ln", "fn", "un", new Date());

		assertNotEquals(player, defaultPlayer);
	}

}
