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
 */


public class BetOnPodium extends Bet {


	private Competitor winner;
	private Competitor second;
	private Competitor third;
	/*
	 * Constructor without parameters
	 */

	public BetOnPodium(){

	}
	public BetOnPodium(int id){
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


	public BetOnPodium ( long tokens, Player player, Competition competition, Competitor winner,Competitor second,Competitor third) throws BadParametersException, CompetitionException{

		super(tokens, player, competition);




		this.winner = winner;
		this.second=second;
		this.third=third;


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

	public Competitor getWinner() {
		return winner;
		}
	public Competitor getSecond() {
		return second;
		}
	public Competitor getThird() {
		return third;
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

	public void setWinner(Competitor winner)  {
		this.winner =  winner;
	}

	public void setSecond(Competitor second)  {
		this.second =  second;
	}

	public void setThird(Competitor third)  {
		this.third =  third;
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
		if ( winner  instanceof Person)
			return "Bet[" +id + "," + tokens +","+ id +","+ ((Person)winner).getFirstName() +","+((Person)winner).getLastName()+"on "+competition.getName()+"]"+" done by "+player.getUserName();
		else if(winner instanceof Team)
			return "Bet[" +id + "," + tokens +","+ id +","+ ((Team)winner).getName() +","+"on "+competition.getName()+"]"+" done by "+player.getUserName();
		return " none bet ";
	}

}
