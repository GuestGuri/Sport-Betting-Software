package fr.uv1.bettingServices;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import fr.uv1.bettingServices.bet.BetOnPodium;
import fr.uv1.bettingServices.bet.BetOnPodiumDAO;
import fr.uv1.bettingServices.bet.BetOnWinner;
import fr.uv1.bettingServices.bet.BetOnWinnerDAO;
import fr.uv1.bettingServices.competition.Competition;
import fr.uv1.bettingServices.competitor.PersonDAO;
import fr.uv1.bettingServices.competitor.TeamDAO;
import fr.uv1.bettingServices.exceptions.AuthenticationException;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
import fr.uv1.bettingServices.exceptions.ExistingSubscriberException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.bettingServices.manager.CompetitionManager;
import fr.uv1.bettingServices.manager.PlayersManager;

public class Manager implements Betting{


	private static String Password_du_Manager="";

	public static void main(String[] args) {

		/*	String userName=null;

		while(!PlayersManager.hasValidUserName(userName)){
			Scanner sc=new Scanner(System.in);
			userName=sc.nextLine();
		}
		*/
	}

	public void authenticateMngr(String managerPwd) throws AuthenticationException {


		if (!(managerPwd==Password_du_Manager)){throw new AuthenticationException();}
		}


	@Override
	public String subscribe(String lastName, String firstName, String username, String borndate, String managerPwd)
			throws AuthenticationException, ExistingSubscriberException, SubscriberException, BadParametersException {


		authenticateMngr(managerPwd);

		try {
			return PlayersManager.subscribe(lastName, firstName, username, borndate);
		} catch (SQLException e) {

			e.printStackTrace();
		}
			return "error "+username+ " exist";
	}

	@Override
	public long unsubscribe(String username, String managerPwd)
			throws AuthenticationException, ExistingSubscriberException {

		/*

		 */
		authenticateMngr(managerPwd);




		try {
			return PlayersManager.unsubscribe(username);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public List<List<String>> listSubscribers(String managerPwd) throws AuthenticationException {

		/*

		 */
		authenticateMngr(managerPwd);

		try {
			return PlayersManager.listPlayers();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void addCompetition(String competition, Calendar closingDate, Collection<Competitor> competitors,
			String managerPwd)
			throws AuthenticationException, ExistingCompetitionException, CompetitionException, BadParametersException {
		/*

		 */
		authenticateMngr(managerPwd);


		try {
			CompetitionManager.addCompetition(competition, closingDate, competitors);
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (ExistingCompetitorException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void cancelCompetition(String competition, String managerPwd)
			throws AuthenticationException, ExistingCompetitionException, CompetitionException {

		/*

		 */
		authenticateMngr(managerPwd);

		try {
			CompetitionManager.cancelCompetition(competition);
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (BadParametersException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void deleteCompetition(String competition, String managerPwd)
			throws AuthenticationException, ExistingCompetitionException, CompetitionException {

		/*

		 */
		authenticateMngr(managerPwd);

		try {
			CompetitionManager.deleteCompetition(competition);
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (BadParametersException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void addCompetitor(String competition, Competitor competitor, String managerPwd)
			throws AuthenticationException, ExistingCompetitionException, CompetitionException,
			ExistingCompetitorException, BadParametersException {

		/*

		 */
		authenticateMngr(managerPwd);

		try {
			CompetitionManager.addCompetitor(competition, competitor);
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public Competitor createCompetitor(String lastName, String firstName, String borndate, String managerPwd)
			throws AuthenticationException, BadParametersException {

		/*

		 */
		authenticateMngr(managerPwd);

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		java.sql.Date startDate = null;
		try {
			startDate = (java.sql.Date) df.parse(borndate);
		} catch (ParseException e) {

			e.printStackTrace();
		}

		try {
			return PersonDAO.findOrCreateCompetitor(lastName, firstName, startDate);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Competitor createCompetitor(String name, String managerPwd)
			throws AuthenticationException, BadParametersException {
		/*

		 */
		authenticateMngr(managerPwd);
		try {
			return TeamDAO.findOrCreateTeam(name);
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void deleteCompetitor(String competition, Competitor competitor, String managerPwd)
			throws AuthenticationException, ExistingCompetitionException, CompetitionException,
			ExistingCompetitorException {
		/*

		 */
		authenticateMngr(managerPwd);

		try {
			CompetitionManager.deleteCompetitor(competition, competitor);
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (BadParametersException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void creditSubscriber(String username, long numberTokens, String managerPwd)
			throws AuthenticationException, ExistingSubscriberException, BadParametersException {

		/*

		 */
		authenticateMngr(managerPwd);


		try {
			 PlayersManager.creditSubscriberByUserName(username, numberTokens);
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void debitSubscriber(String username, long numberTokens, String managerPwd)
			throws AuthenticationException, ExistingSubscriberException, SubscriberException, BadParametersException {
		/*

		 */
		authenticateMngr(managerPwd);

		long l=PlayersManager.debitSubscriberByUserName(username, numberTokens);
		if(l==-1){
			System.out.println("error");
		}
		else{
	    System.out.println("updated tocken amount is "+l);
		}
	}

	@Override
	public void settleWinner(String competition, Competitor winner, String managerPwd)
			throws AuthenticationException, ExistingCompetitionException, CompetitionException {
		/*

		 */
		authenticateMngr(managerPwd);

		try {
			CompetitionManager.settleWinner(competition, winner);
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (BadParametersException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void settlePodium(String competition, Competitor winner, Competitor second, Competitor third,
			String managerPwd) throws AuthenticationException, ExistingCompetitionException, CompetitionException {
		/*

		 */
		authenticateMngr(managerPwd);

		try {
			CompetitionManager.settlePodium(competition, winner, second, third);
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (BadParametersException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void betOnWinner(long numberTokens, String competition, Competitor winner, String username, String pwdSubs)
			throws AuthenticationException, CompetitionException, ExistingCompetitionException, SubscriberException,
			BadParametersException {



		BetOnWinner bet=new BetOnWinner();
		bet.setTokens(numberTokens);
		try {
			Competition c=CompetitionManager.findByName1(competition);
			if (!c.isClosed()){
				bet.setCompetition(c);
				bet.setBetter(PlayersManager.findPlayerByUserName(username));
				bet.setCompetitor(winner);
				BetOnWinnerDAO.persist(bet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void betOnPodium(long numberTokens, String competition, Competitor winner, Competitor second,
			Competitor third, String username, String pwdSubs) throws AuthenticationException, CompetitionException,
			ExistingCompetitionException, SubscriberException, BadParametersException {

		BetOnPodium bet=new BetOnPodium();
		bet.setTokens(numberTokens);
		try {
			Competition c=CompetitionManager.findByName1(competition);
			if (!c.isClosed()){
				bet.setCompetition(c);
				bet.setBetter(PlayersManager.findPlayerByUserName(username));
				bet.setWinner(winner);
				bet.setSecond(second);
				bet.setThird(third);
				BetOnPodiumDAO.persist(bet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void changeSubsPwd(String username, String newPwd, String currentPwd)
			throws AuthenticationException, BadParametersException {


		try {
			PlayersManager.changeSubsPwd(username, newPwd, currentPwd);
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	@Override
	public ArrayList<String> infosSubscriber(String username, String pwdSubs) throws AuthenticationException {



		try {
			return PlayersManager.infosSubscriber(username, pwdSubs);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void deleteBetsCompetition(String competition, String username, String pwdSubs)
			throws AuthenticationException, CompetitionException, ExistingCompetitionException {

	try {
		PlayersManager.deleteBetsCompetition(competition, username, pwdSubs);
	} catch (SQLException e) {

		e.printStackTrace();
	} catch (BadParametersException e) {
		e.printStackTrace();
	}
	}

	@Override
	public List<List<String>> listCompetitions() {

		try {
			return CompetitionManager.listCompetitions();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (BadParametersException e) {
			e.printStackTrace();
		} catch (ExistingCompetitionException e) {
			e.printStackTrace();
		} catch (CompetitionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Collection<Competitor> listCompetitors(String competition)
			throws ExistingCompetitionException, CompetitionException {

		try {
			return CompetitionManager.listCompetitors(competition);
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (BadParametersException e) {

			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<String> consultBetsCompetition(String competition) throws ExistingCompetitionException {

		try {
			return CompetitionManager.consultBetsCompetition(competition);
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (BadParametersException e) {

			e.printStackTrace();
		} catch (CompetitionException e) {

			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<Competitor> consultResultsCompetition(String competition) throws ExistingCompetitionException {

		try {
			return CompetitionManager.consultResultsCompetition(competition);
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (BadParametersException e) {

			e.printStackTrace();
		} catch (CompetitionException e) {

			e.printStackTrace();
		}
		return null;
	}



}
