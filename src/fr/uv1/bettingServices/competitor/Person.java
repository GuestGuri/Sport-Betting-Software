package fr.uv1.bettingServices.competitor;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.competitor.Competitor;

import fr.uv1.bettingServices.competitor.PersonDAO;

/**Class Person represents a single Competitor
 * */


public class Person extends Competitor {
	//Regular expression to verify names
	private final static String VERIFYNAMES = "[A-Za-z\\-]{2,}";

	private int id;
	private String firstName;
	private String lastName;
	private Date bornDate;
	private String team;
	/**
	 * Default Constructor
	 */
	public Person(){

	}
	 /**
     * Constructor
     * @param lastName
     * 	<ul>Person's last name</ul>
     * @param firstName
     * <ul>Person's first name</ul>
     * @param borndate
     * <ul>Person's born date</ul>
     *
     * @throws BadParametersException
     * <ul>raised if born date is after current date or the person does not have a valid name</ul>
     */
	public Person(String firstName, String lastName, Date bornDate) throws BadParametersException {

		//checking if the competitor is not born in the future
		Date now = new Date(System.currentTimeMillis());
		if(bornDate.after(now))
			throw new BadParametersException("Competitor's borndate is invalid");

		this.firstName=firstName;
		this.lastName=lastName;
		this.bornDate=bornDate;

		//checking if the competitor s name is valid
		if (!(this.hasValidName()))
			throw new BadParametersException("Competitor's firstname and/or lastname are invalid");


	}

	 /**
     * First name setter
     * @param firstName
     * <ul>Person's first name</ul>
     *
     * @throws BadParametersException
     * <ul>raised if the person does not have a valid first name</ul>
     * @throws ClassNotFoundException
     * <ul>raised if the class is not found</ul>
	 * @throws SQLException
	 * <ul>raised if there is an error on a database access or other errors</ul>


     */

	public void setFirstName(String firstName) throws BadParametersException, ClassNotFoundException, SQLException {
		if (!firstName.matches(VERIFYNAMES))
			throw new BadParametersException();
		this.firstName=firstName;
		PersonDAO.update(this);
	}
	/**
	 * First Name getter
	 *@return firstName : the person's first name
	 *
	 */

	public String getFirstName() {
		return this.firstName;
	}

	 /**
     * Last name setter
     * @param lastName
     * <ul>Person's last name</ul>
     *
     * @throws BadParametersException
     * <ul>raised if the person does not have a valid last name</ul>
     * @throws ClassNotFoundException
     * <ul>raised if the class is not found</ul>
	 * @throws SQLException
	 * <ul>raised if there is an error on a database access or other errors</ul>
     */

	public void setLastName(String lastName) throws BadParametersException, ClassNotFoundException, SQLException {
		if (!lastName.matches(VERIFYNAMES))
			throw new BadParametersException();
		this.lastName=lastName;
		PersonDAO.update(this);


	}

	 /**
     * Last name getter
     * @return lastName : the person's last name
     *
     */

	public String getLastName() {
		return this.lastName;
	}


	 /**
     * born date setter
     * @param bornDate
     * <ul>the person's born date</ul>
     *
     * @throws BadParametersException
     * <ul>raised if the born date is after the current one</ul>
     * @throws ClassNotFoundException
     * <ul>raised if the class is not found</ul>
	 * @throws SQLException
	 * <ul>raised if there is an error on a database access or other errors</ul>
     */

	public void setBornDate(Date bornDate) throws ClassNotFoundException, BadParametersException, SQLException {
		Date now = new Date(System.currentTimeMillis());
		if(bornDate.after(now)){
			throw new BadParametersException("Competitor's borndate is invalid");
		}
		this.bornDate=bornDate;

		PersonDAO.update(this);

	}
	/**
	 * born date getter
	 * @return bornDate : the person's born date
	 *
	 */

	public Date getBornDate() {
		return this.bornDate;
	}
	 /**
     * id setter
     * @param id : the person's id
     *
     */

	public void setId(int id) {
		this.id = id;
		//Do not need to update id in DATABASE since it is auto-incremented

	}


	/**
	 * id getter
	 * @return id : the person's id
	 *
	 */
	public int getId() {
		return this.id;
	}
	/**
	 * Team name Setter
	 * @param team : the person's team name if he's a member of a team
	 */
	public void setTeam(String team){
		this.team=team;
	}
	/**
	 * Team name getter
	 * @return team : returns the person's team name if he's a member of a team
	 */
	public String getTeam(){
		return team;
	}


	 /**
     *
     * @return true if the person has a valid name
     */

	@Override
	public boolean hasValidName () {

		//the first name and last name must contain more than 2 characters
		//it can contain only letters ( upper or under case) and underscores (for complex names)
		return firstName.matches(VERIFYNAMES) && lastName.matches(VERIFYNAMES);
	}



	 /**
     * @param other: an other competitor
     *
     * @return true if it is the same person
     */



	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof Person))
			return false;

		Person competitor = (Person) other;
		return (this.firstName.equals(competitor.getFirstName()) &&
				this.lastName.equals(competitor.getLastName()) &&
				this.getBornDate().getTime() == competitor.getBornDate().getTime());
	}
	 /**
     * Describe the Person
     *
     * @return Person's description
     */

	public String toString(){
		String date = new SimpleDateFormat("dd-MM-yyyy").format(this.bornDate);
		return this.firstName + "," + this.lastName + "," + date;
	}

	@Override
	public void addMember(fr.uv1.bettingServices.Competitor member)
			throws ExistingCompetitorException, BadParametersException {
		throw new UnsupportedOperationException();

	}
	@Override
	public void deleteMember(fr.uv1.bettingServices.Competitor member)
			throws BadParametersException, ExistingCompetitorException {
		throw new UnsupportedOperationException();

	}
}
