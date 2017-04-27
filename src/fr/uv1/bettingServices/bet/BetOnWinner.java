/**
 *
 */
package fr.uv1.bettingServices.bet;

import fr.uv1.bettingServices.Competitor;
import fr.uv1.bettingServices.competition.Competition;
import fr.uv1.bettingServices.competitor.Person;
import fr.uv1.bettingServices.competitor.Team;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.player.Player;


import java.util.ArrayList;


/**
 *
 */


public class BetOnWinner extends Bet {


	private ArrayList<Competitor> competitionCompetitors = new ArrayList<Competitor>();
	private Competitor competitor;

	/*
	 * Constructor without parameters
	 */

	public BetOnWinner(){

	}
	public BetOnWinner(int id){
		this.id=id;
	}



	/**
	 * Constructor
	*
	* @param tokens
	* @param player
	* @param competition
	* @param competitor
	*
	*/


	public BetOnWinner ( long tokens, Player player, Competition competition, Competitor competitor) throws BadParametersException, CompetitionException{

		super(tokens, player, competition);
		competitionCompetitors = (ArrayList<Competitor>) competition.getCompetitors();


		if (!(competitionCompetitors.contains(competitor)))
			throw new CompetitionException("competitor is not participating in this competition");

		/*
		 * the second part consists in doing that:
		 *generate id
		 *checks id. if there exists a bet with the id generated, create a new id
		 *then gives the id
		 *and constructs
		 */

		this.competitor = competitor;
		if (competition.isClosed())
			throw new CompetitionException();


	}





	//Getter methods :
	public Competition getCompetition() {
		return competition;
	}

	public long getTokens() {
		return tokens;
		}

	public  int getId() {
		return id;
		}

	public Competitor getCompetitor() {
		return competitor;
		}



	//Setter methods
	/**
	 *
	 * @param competition
	 */
	public void setCompetition(Competition competition)  {
		this.competition = competition;

	}
	/**
	 * @param tokens
	 */
	public void setTokens(long tokens)  {
		this.tokens = tokens;
	}
	/**
	 *
	 * @param competitor
	 */

	public void setCompetitor(Competitor competitor)  {
		this.competitor =  competitor;
	}



	//------------------------------------------------
	@Override
	/**
	 *
	 */
	public boolean equals(Object obj){
		if( obj instanceof BetOnWinner){

			return this.id == ((BetOnWinner) obj).getId();
		}
		return false;
	}
	@Override
	/**
	 *
	 */
	public String toString(){
		if ( competitor  instanceof Person)
			return "Bet[" +id + "," + tokens +","+ id +","+ ((Person)competitor).getFirstName() +","+((Person)competitor).getLastName()+"on "+competition.getName()+"]"+" done by "+player.getUserName();
		else if(competitor instanceof Team)
			return "Bet[" +id + "," + tokens +","+ id +","+ ((Team)competitor).getName() +","+"on "+competition.getName()+"]"+" done by "+player.getUserName();
		return " none bet ";
	}

}
