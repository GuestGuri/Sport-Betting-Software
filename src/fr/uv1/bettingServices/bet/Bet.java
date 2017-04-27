/**
 *
 */
package fr.uv1.bettingServices.bet;

import fr.uv1.bettingServices.competition.Competition;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.player.Player;


/**
 *
 */
public abstract class Bet {

	protected int id;
	protected long tokens;
	protected Player player;
	protected Competition competition;


	// we must to generate a random id, check in the data base, if it already exist to either attribute or look for another one, but it will be handleded in the inherited classes

	/*
	 *  Constructors to use in subclasses
	 */
	public Bet(){

	}


	 /**
	  * Constructor
     *
     * @param tokens
     * @param player
     * @param competition
     *
     */

	public Bet( long tokens, Player player,Competition competition) throws  BadParametersException, CompetitionException{
		if (tokens < 0)
			throw new BadParametersException("Tokens number inferior than 0");
		if(player.getNbTokens() < tokens)
			throw new BadParametersException("Bet impossible because : no enough tokens ");
		if(competition.isClosed())
			throw new CompetitionException("bet impossible because the competition is closed");


		this.tokens = tokens;
		//this.id = id; (for data base)
		this.player = player;
		this.competition = competition;

	}

	//Getter methods


	public long getTokens(){
		return tokens;
	}

	public Player getBetter(){
		return player;
	}

	//Setter methods

	public void setTokens(long tokens) {
		this.tokens = tokens;

	}
	public void setBetter(Player player){
		this.player = player;
	}



}
