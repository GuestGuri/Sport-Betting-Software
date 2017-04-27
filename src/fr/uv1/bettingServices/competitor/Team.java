package fr.uv1.bettingServices.competitor;

import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;

import java.sql.SQLException;
import java.util.ArrayList;
/**
 *
 * */
public final class Team extends Competitor {
	//regular expression to verify the team name
	private final static String NAMEPATTERN = "[A-Z][\\w\\-\\s]{3,}";

	private String name;
	private ArrayList<Person> members;
	private int id;

	public Team(){}
	/**
	 *
	 * @param name
	 *
	 * @throws BadParametersException
	 */
	public Team(String name) throws  BadParametersException{
		this.name=name;
		this.members=new ArrayList<Person>();
		if (!this.hasValidName())
			throw new BadParametersException();


	}
	/**
	 *
	 */
	@Override
	public void addMember(fr.uv1.bettingServices.Competitor member)
			throws ExistingCompetitorException, BadParametersException {

		for (Person oldMember : members) {
			if(oldMember.equals(member))
				throw new ExistingCompetitorException();
			}
		members.add((Person)member);
		try {
			TeamDAO.addCompetitorToTeam(this,(Person)member);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	/**
	 *
	 */
	@Override
	public void deleteMember(fr.uv1.bettingServices.Competitor member)
			throws BadParametersException, ExistingCompetitorException {

			members.remove(member);
			try {
				TeamDAO.deleteCompetitor(this, (Person) member);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}




	}
	/**
	 *
	 */
	@Override
	public boolean hasValidName() {

		return this.name.matches(NAMEPATTERN);
	}
	/**
	 *
	 * @return id
	 */
	public int getId() {
		return id;
	}
	/**
	 *
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 *
	 * @return name
	 */
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}
	@Override
	public boolean equals(Object other){
		if (other == null)
			return false;
		if (!(other instanceof Team))
			return false;

		Team competitor = (Team) other;
		return (name==competitor.name);
	}
}
