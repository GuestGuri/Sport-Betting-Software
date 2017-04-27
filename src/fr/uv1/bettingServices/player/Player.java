package fr.uv1.bettingServices.player;
import java.util.ArrayList;
import java.util.Date;

import fr.uv1.bettingServices.competitor.Person;
import fr.uv1.bettingServices.exceptions.AuthenticationException;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.ExistingSubscriberException;
import fr.uv1.bettingServices.exceptions.SubscriberException;



/**
 * Classe Player
 * <br>
 *         This class declares all attributes and methods that should be provided by a
 *         player. <br>
 */

public class Player {

	/***********************************************************************
	 * DEFAULT PARAMETERS
	 ***********************************************************************/
	private static final String DEFAULT_PASSWORD = "0000";
	private static final long DEFAULT_TOKENS = 0L;

	/***********************************************************************
	 * Player ATTRIBUTES
	 ***********************************************************************/
	private int id;
	private String lastName;
    private String firstName;
    private String userName;
    private Date bornDate;
    private String pwdPlayer;
    private long nbTokens;

    /**
     * <br> Player Constructor with 4 arguments </br>
     * @param lastName
     * 			player's lastname
     * @param firstName
     * 			player's firstname
     * @param username
     * 			player's username
     * @param borndate
     * 			player's borndate
     * @throws BadParametersException
     * 			 raised if just one parameter above is null
     */
    public Player(String lastName, String firstName, String username, Date borndate) throws BadParametersException {
    	if(lastName == null || firstName == null || username == null || borndate == null) {
    		throw new BadParametersException();
    	}

    	this.lastName = lastName;
        this.firstName = firstName;
        this.userName = username;
        this.bornDate = borndate;
        this.pwdPlayer= DEFAULT_PASSWORD;
        this.nbTokens= DEFAULT_TOKENS;
    }


    /**
     * <br> Player Constructor with 5 arguments </br>
     * @param lastName
     * 			player's lastname
     * @param firstName
     * 			player's firstname
     * @param username
     * 			player's username
     * @param borndate
     * 			player's borndate
     * @param initialCredit
     * 			player's credit
     * @throws BadParametersException
     * 			raised if just one parameter above is null or initialCredit has a negative value
     */
    public Player(String lastName, String firstName, String username, Date borndate,long initialCredit) throws BadParametersException {
    	this(lastName, firstName, username, borndate);
        if(initialCredit < 0L){
        	throw new BadParametersException();
        }
        else{
        	this.nbTokens= initialCredit;
        }
    }


    /**
     * returns player's id
     * @return id
     */

	public int getId() {
		return id;
	}

	/**
	 * Sets player's id
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	 /**
     * returns player's lastName
     * @return
     */

	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets player's lastName
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	/**
	 * returns player's firstName
	 * @return firstName
	 */
	public String getFirstName() {
		return firstName;
	}


	/**
	 * Sets player's firstName
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	/**
	 * Returns player's userName
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}


	/**
	 * Sets player's userName
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}


	/**
	 * Returns player's bornDate
	 * @return bornDate
	 */
	public Date getBornDate() {
		return bornDate;
	}


	/**
	 * Sets player's bornDate
	 * @param bornDate
	 */
	public void setBornDate(Date bornDate) {
		this.bornDate = bornDate;
	}


	/**
	 * Returns player's password
	 * @return pwdPlayer
	 */
	public String getPwdPlayer() {
		return pwdPlayer;
	}


	/**
	 * Sets player's password
	 * @param pwdPlayer
	 */
	public void setPwdPlayer(String pwdPlayer) {
		this.pwdPlayer = pwdPlayer;
	}


	/**
	 * Returns number of tokens
	 * @return nbTokens
	 */
	public long getNbTokens() {
		return nbTokens;
	}

	/**
	 * Sets number of tokens
	 * @param nbTokens
	 */
	public void setNbTokens(long nbTokens) {
		this.nbTokens = nbTokens;
	}


    /**
     * changes current user password
     *
     * @param newPwd
     *			new user password
     * @param currentPwd
     *			current user password
     * @throws AuthenticationException
     * 			raised if the player's password and the new password passed as a parameters are not equals.
     * @throws BadParametersException
     * 			raised if the current password or the new password is null.
     *
     *
     */
    public boolean changeSubsPwd(String newPwd, String currentPwd) throws AuthenticationException, BadParametersException {
        if (newPwd == null || currentPwd == null) {
			throw new BadParametersException();
		}

        if(passwordVerified(currentPwd)){
            pwdPlayer = newPwd;
            return true;
        }

        return false;
    }

    /**
     * Checks if the entered userName and password are correct for a player
     * @param userNameEntered
     * @param pwdPlayerEntered
     * @return
     * 			<br>true if player has the same userName and password</br>
     * 			<br>false if userName and password does not match with the player</br>
     * @throws AuthenticationException
     * 			raised if the player's userName and the password passed as a parameters does not match.
     * @throws BadParametersException
     * 			raised if the userName or the password is null.
     */
	public boolean authentificatePlayer(String userNameEntered,String pwdPlayerEntered) throws AuthenticationException, BadParametersException{
    	if (userNameEntered == null || pwdPlayerEntered == null) {
			throw new BadParametersException();
		}
		return (usernameVerified(userNameEntered) && passwordVerified(pwdPlayerEntered)) ? true : false;
    }

	/**
	 * credit number of tokens of a subscriber.
	 *
	 * @param numberTokens
	 *            number of tokens to credit.
	 * @return nbTokens
	 * 			  the new value of the player's credit.
	 * @throws BadParametersException
	 *             raised if number of tokens is less than (or equals to) 0.
	 **/
    public long creditSubscriber(long numberTokens) throws BadParametersException{
    	if (numberTokens <= 0L) {
			throw new BadParametersException();
		}
    	return nbTokens+=numberTokens;
    }

    /**
     * debit a subscriber account with a number of tokens.
     * @param numberTokens
	 *            number of tokens to debit.
     * @return nbTokens
     * 			the new value of the player's credit
     * @throws SubscriberException
	 *             raised if number of tokens not enough.
	 * @throws BadParametersException
	 *             raised if number of tokens is less than (or equals to) 0.
	 *
     */
    public long debitSubscriber(long numberTokens) throws SubscriberException,BadParametersException{
    	if (numberTokens <= 0L) {
			throw new BadParametersException();
		}
    	if(nbTokens<numberTokens){
    		throw new SubscriberException("invalid tockens");
    	}
    	return nbTokens -= numberTokens;
    }


    /**
	 * consult informations about a subscriber
	 *
	 * @return list of String with:
	 *         <ul>
	 *         <li>subscriber's lastname</li>
	 *         <li>subscriber's firstname</li>
	 *         <li>subscriber's borndate</li>
	 *         <li>subscriber's username</li>
	 *         <li>number of tokens</li>
	 *         </ul>
	 * <br>
	 */
	public ArrayList<String> infosSubscriber(){
		ArrayList<String> list = new ArrayList<String>();
		list.add(lastName);
		list.add(firstName);
		list.add(userName);
		list.add(bornDate.toString());
		list.add(new Long(nbTokens).toString());
		return list;
	}

	/**
	 * Verify that the user password matches the entered password
	 * @param enteredPassword
	 * 			the password of the player passed as a parameter
	 * @return
	 * 			<br>true if passwords are equals.</br>
	 *
	 * @throws AuthenticationException
	 *      	raised if the password of the player and the password passed as a parameter does not match.
	 */
	private boolean passwordVerified(String enteredPassword) throws AuthenticationException {
		if(pwdPlayer.equals(enteredPassword)) {
			return true;
		} else {
			throw new AuthenticationException();
		}
	}

	/**
	 * Verify that the user userName matches the entered userName
	 * @param enteredUsername
	 * 			the userName of the player passed as a parameter
	 * @return
	 * <br>true if usernames are equals.</br>
	 *
	 * @throws AuthenticationException
	 * 	 		raised if the username of the player and the username passed as a parameter does not match.
	 */
	private boolean usernameVerified(String enteredUsername) throws AuthenticationException {
		if (userName.equals(enteredUsername)) {
			return true;
		} else {
			throw new AuthenticationException();
		}
	}

	/**
	 * Prints User full name, username, borndate and current number of tokens
	 * @return	description:
	 * 			a string containing all searched for elements
	 */
	@Override
	public String toString() {
		String description=new String( "Full name: " + lastName + " " + firstName + "\nusername: " + userName +
				"\nbirthdate: " + bornDate.toString() + "\nNumbers of Tokens: " + new Long(nbTokens).toString());
		 return description;
	}

	/**
	 * test if two players are equals using the "username" as criteria.
	 * @return
	 * <br>true if usernames are equals.</br>
	 * <br>false if usernames are not equals.</br>

	 */
	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof Player))
			return false;

		Player player = (Player) other;
		return (this.userName.equals(player.getUserName()));
	}
}
