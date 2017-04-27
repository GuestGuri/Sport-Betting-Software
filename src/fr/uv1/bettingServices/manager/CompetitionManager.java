package fr.uv1.bettingServices.manager;

import java.sql.Connection;
import java.sql.Date;

import fr.uv1.bettingServices.competitor.*;
import fr.uv1.bettingServices.exceptions.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import fr.uv1.bettingServices.Competitor;
import fr.uv1.bettingServices.competition.*;
import fr.uv1.bettingServices.db.*;


/**
 */


public class CompetitionManager {



	/**
	 * @param c
	 * @param competition
	 * @return integer
	 * @throws SQLException
	 */
	public static int chercherIdCompetition(Connection c,String competition) throws SQLException{
		int idCompetition=0;
	PreparedStatement chercherIdCompetition = c.prepareStatement("Select id from competition where name=?");
	chercherIdCompetition.setString(1, competition);
	ResultSet id_compet= chercherIdCompetition.executeQuery();
	while(id_compet.next()){
		idCompetition=id_compet.getInt("idcompetition");
	}
	chercherIdCompetition.close();
	return idCompetition;
	}




	/**
	 * @param date
	 * @return calendar
	 */
	public static Calendar toCalendar(Date date){
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(date);
		  return cal;
		}

	/**
	 * @param c
	 * @param e1
	 * @throws SQLException
	 */
	public static void verifierBonneMAJ(Connection c,SQLException e1) throws SQLException{

			try {

				c.rollback();

			} catch (SQLException e2) {
				e1.printStackTrace();
			}
		}

	/**
	 * @param competitionName
	 * @return Boolean
	 * @throws SQLException
	 * @throws BadParametersException
	 * @throws CompetitionException
	 */
	public static boolean findByName(String competitionName) throws SQLException, BadParametersException, CompetitionException {
		Competition competition = null;
		Connection c = DBConnection.newConnection();
		try{
			PreparedStatement psSelect = c.prepareStatement("select * from competition where name = ?");
			psSelect.setString(1, competitionName);
			ResultSet resultSet = psSelect.executeQuery();
			if(resultSet.next()){
				competition = new Competition(resultSet.getString("name"),resultSet.getDate("closingdate"));
				}
			resultSet.close();
			psSelect.close();

		} catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();

		if (competition==null){
			return false;
		}
		else {
			return true;
		}
		}
	/**
	 * @param competitionName
	 * @return Competition
	 * @throws SQLException
	 * @throws BadParametersException
	 * @throws CompetitionException
	 */
	public static Competition findByName1(String competitionName) throws SQLException, BadParametersException, CompetitionException {
		Competition competition = null;
		Connection c = DBConnection.newConnection();
		try{
			PreparedStatement psSelect = c.prepareStatement("select * from competition where name = ?");
			psSelect.setString(1, competitionName);
			ResultSet resultSet = psSelect.executeQuery();
			if(resultSet.next()){
				competition = new Competition(resultSet.getString("name"),resultSet.getDate("closingdate"));
				}
			resultSet.close();
			psSelect.close();

		} catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();

		return competition;

		}


	/**
	 * @param competitionName
	 * @return
	 * @throws SQLException
	 * @throws BadParametersException
	 * @throws ExistingCompetitionException
	 * @throws CompetitionException
	 */
	public static Competition findById(int ID) throws SQLException, BadParametersException, ExistingCompetitionException, CompetitionException {
		Competition competition = null;
		Connection c = DBConnection.newConnection();
		try{
			PreparedStatement psSelect = c.prepareStatement("select * from competition where idcompetition = ?");
			psSelect.setInt(1,ID);
			ResultSet resultSet = psSelect.executeQuery();
			if(resultSet.next()){
				competition = new Competition(resultSet.getString("name"),resultSet.getDate("closingdate"));
				competition.setId(ID);
				}
			resultSet.close();
			psSelect.close();

		} catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();
		if(competition==null){throw new ExistingCompetitionException("invalid competition");}
		return competition;
		}
	/**
	 *
	 * @param competition
	 * @throws SQLException
	 * @throws ExistingCompetitionException
	 * @throws BadParametersException
	 * @throws ExistingCompetitorException
	 */

	public static void addCompetition(String competition, Calendar closingDate,
			Collection<Competitor> competitors )
			throws  ExistingCompetitionException,
			CompetitionException, BadParametersException, SQLException, ExistingCompetitorException {
		/*
		 */

		Connection c = DBConnection.newConnection();
		/**
		 */
		Competition compet=new Competition(competition,closingDate.getTime());
		/*
		 */
		if(findByName(competition)==true){throw new ExistingCompetitionException("invalid competition");}
		/*
		 */
		if(competitors.size()<2){throw new CompetitionException("2 competitors at least are required");}
		/*
		 */
		for (Competitor i: competitors){
			if(Collections.frequency(competitors,i)>1){
				throw new CompetitionException("invalid competitor");}
		}
		/*
		 */
		try {

			PreparedStatement preparedStatement1 = c.prepareStatement("INSERT INTO competition (name, closingdate,"
					+ "values (?, ?)");

				/*
				 */
			preparedStatement1.setString(1, competition);
			preparedStatement1.setDate(2, java.sql.Date.valueOf(closingDate.toString()));



			preparedStatement1.executeUpdate();
			preparedStatement1.close();

			for(Competitor i:competitors)
			{ addCompetitor(competition,i);}


			PreparedStatement psIdValue = c.prepareStatement("select idperson_seq.currval from dual");

			ResultSet resultSet = psIdValue.executeQuery();
            resultSet.next();
			int id =  Integer.parseInt(resultSet.getString("idperson"));

			c.commit();
			//the auto-incremented iD will be settled in person object

			compet.setId(id);
		} catch (SQLException e1) {
			verifierBonneMAJ(c,e1);
		}
		c.close();
	}

	/**
	 * @throws BadParametersException
	 * @throws SQLException
	 */
	public static void cancelCompetition(String competition )
			throws  ExistingCompetitionException,
			CompetitionException, SQLException, BadParametersException{


		if ((findByName(competition))==false){throw new ExistingCompetitionException("invalid competition");}

		if (findByName1(competition).isClosed()==true){
			throw new CompetitionException("competition terminated");
		}
		Connection connection1 = DBConnection.newConnection();

		try{

		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("Update  competition set date=? where name LIKE ?");
		preparedStatement1.setDate(1,(Date) new java.util.Date());
		preparedStatement1.setString(2, competition);
		preparedStatement1.executeUpdate();
		preparedStatement1.close();
		connection1.commit();
	    connection1.close();


		}
		catch (SQLException e1) {
			verifierBonneMAJ(connection1,e1);

		}
		connection1.close();

	}

	/**
	 * @param nom_competition
	 * @throws SQLException
	 * @throws BadParametersException
	 */

	public static void deleteCompetition(String competition )
			throws  ExistingCompetitionException,
			CompetitionException, SQLException, BadParametersException{



	if ((findByName(competition))==false){throw new ExistingCompetitionException("invalid competition");}

	if (findByName1(competition).isClosed()==false){
		throw new CompetitionException("competition already started");
	}
	Connection connection1 = DBConnection.newConnection();

	try{

	PreparedStatement preparedStatement1 = connection1
			.prepareStatement("DELETE FROM competition WHERE name LIKE ?");
	preparedStatement1.setString(1,competition );
	preparedStatement1.executeUpdate();
	preparedStatement1.close();
	connection1.commit();
    connection1.close();


	}
	catch (SQLException e1) {
		verifierBonneMAJ(connection1,e1);

	}
	connection1.close();
	}




	/**
	 *
	 * @param name
	 * @return ArrayList
	 * @throws SQLException
	 * @throws BadParametersException
	 * @throws CompetitionException
	 */
	public static LinkedList<Competition> lister_Competition() throws SQLException, BadParametersException, CompetitionException {

		LinkedList<Competition> liste_competition= new LinkedList<Competition>();

		Connection c = DBConnection.newConnection();

		try{


		PreparedStatement preparedStatement1 = c.prepareStatement("SELECT * FROM competition");

		ResultSet resultSet1 = preparedStatement1.executeQuery();

		while (resultSet1.next()) {
			Competition compet = new Competition(resultSet1.getString("name"),resultSet1.getDate("closingdate"));
			compet.setId(resultSet1.getInt("idcompetition"));
			liste_competition.add(compet);
		}

		resultSet1.close();
		preparedStatement1.close();



		}
		catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();
		return liste_competition;
		}

	/**
	 * @throws SQLException
	 * @throws BadParametersException
	 */
	public static Collection<Competitor> fetchPersons(String competition)
			throws ExistingCompetitionException, CompetitionException, SQLException, BadParametersException {

		if (findByName(competition)==false){throw new ExistingCompetitionException("La comp�tition n'existe pas");}

		ArrayList<Competitor> listePersons=new ArrayList<Competitor>();

		int idCompetition=findByName1(competition).getId();

		Connection c = DBConnection.newConnection();

		try{


		PreparedStatement preparedStatement1 = c.prepareStatement(
				"Select * From person Where idperson="
				+ "(Select person from competitor where idcompetitor="
				+ "(Select competitor from competitionandcompetitor where id="+Integer.toString(idCompetition)+"))");

		ResultSet resultSet1 = preparedStatement1.executeQuery();

		while (resultSet1.next()) {

			Person competiteur=new Person(resultSet1.getString("firstname"),resultSet1.getString("lastname"),resultSet1.getDate("borndate"));



			listePersons.add(competiteur);
		}

		resultSet1.close();
		preparedStatement1.close();



		}
		catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();
		return listePersons;
		}

	/**
	 * @throws ClassNotFoundException
	 */
	public static Collection<Team> fetchTeams(String competition)
			throws ExistingCompetitionException, CompetitionException, SQLException, BadParametersException, ClassNotFoundException {

		if (findByName(competition)==false){throw new ExistingCompetitionException("La comp�tition n'existe pas");}

		ArrayList<Team> listeTeams=new ArrayList<Team>();

		int idCompetition=findByName1(competition).getId();

		Connection c = DBConnection.newConnection();

		try{


		/* on prepare une prepared statemnet */
		PreparedStatement preparedStatement1 = c.prepareStatement(
				"Select * From team Where idteam="
				+ "(Select team from competitor where idcompetitor="
				+ "(Select competitor from competitionandcompetitor where id="+Integer.toString(idCompetition)+"))");

		ResultSet resultSet1 = preparedStatement1.executeQuery();

		while (resultSet1.next()) {

			Team competiteur=new Team(resultSet1.getString("teamname"));



			listeTeams.add(competiteur);
		}

		resultSet1.close();
		preparedStatement1.close();



		}
		catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();
		return listeTeams;
		}

	/**
	 */

	public static Collection<Competitor> fetchTeamCompetitors(String teamName)
			throws ExistingCompetitionException, CompetitionException, SQLException, BadParametersException, ClassNotFoundException{



		ArrayList<Competitor> teamCompetitors = new ArrayList<Competitor>();



		Connection c = DBConnection.newConnection();

		try{


		PreparedStatement preparedStatement1 = c.prepareStatement(
				"Select * From person Where team="+teamName);

		ResultSet resultSet1 = preparedStatement1.executeQuery();

		while (resultSet1.next()) {

			Person competiteur=new Person(resultSet1.getString("firstname"),resultSet1.getString("lastname"),resultSet1.getDate("borndate"));
			competiteur.setTeam(resultSet1.getString("team"));



			teamCompetitors.add(competiteur);
		}

		resultSet1.close();
		preparedStatement1.close();



		}
		catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();
		return teamCompetitors;
		}

	/**
	 */
	public static ArrayList<String> listCompetitorsAsString(String competition)
			throws ExistingCompetitionException, CompetitionException, SQLException, BadParametersException, ClassNotFoundException{

		if (findByName(competition)==false){throw new ExistingCompetitionException("invalid competition");}

		ArrayList<String> competitorsList=new ArrayList<String>();



		int idCompetition=findByName1(competition).getId();

		Connection c = DBConnection.newConnection();

		try{


		/* on prepare un statemnet */
		PreparedStatement preparedStatement1 = c.prepareStatement(
				"Select competitortype From competitor Where idcompetitor="
				+ "(Select competitor from competitionandcompetitor where competition="+
						Integer.toString(idCompetition)+")");

		ResultSet resultSet1 = preparedStatement1.executeQuery();



		if (resultSet1.getString("team")=="team"){

			ArrayList<Team> teamCompetitors=  (ArrayList<Team>)fetchTeams(competition);
			int i=0;
			while(i<teamCompetitors.size()){

				Team competitor=teamCompetitors.remove(i);
				competitorsList.add(competitor.getName()+",");
			}

		}
		if (resultSet1.getString("team")=="person"){
			ArrayList<Competitor> personCompetitors= (ArrayList<Competitor>)fetchPersons(competition);
			int i=0;
			while(i<personCompetitors.size()){
				Person competitor=(Person) personCompetitors.remove(i);
				competitorsList.add(competitor.getFirstName()+" "+competitor.getLastName()+",");
			}

		}

		resultSet1.close();
		preparedStatement1.close();



		}
		catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();
		return competitorsList;
	}
	/**
	 * @throws BadParametersException
	 * @throws SQLException
	 * @throws CompetitionException
	 */
	public static ArrayList<String> consultBetsCompetition(String competition)
			throws ExistingCompetitionException, SQLException, BadParametersException, CompetitionException{
		if(findByName(competition)==false){throw new ExistingCompetitionException("invalid competition");}
		ArrayList<String> bets = new ArrayList<String>();
		try(Connection c = DBConnection.newConnection()){
			PreparedStatement psSelect = c
					.prepareStatement("SELECT id FROM bet WHERE competition=?");

			ResultSet resultSet = psSelect.executeQuery();

			while (resultSet.next()) {
				bets.add(Integer.toString(resultSet.getInt("idbet")));
			}
			resultSet.close();
			psSelect.close();
			return bets;
		} catch(SQLException e) {
			e.printStackTrace();
		}
        return bets;
	}

	/**
	 * @throws BadParametersException
	 * @throws SQLException
	 * @throws ExistingCompetitionException
	 * @throws CompetitionException
	 */

	public static String consultBetsCompetitionAsString(String competition) throws ExistingCompetitionException, SQLException, BadParametersException, CompetitionException{
		ArrayList<String> bets = consultBetsCompetition(competition);
		int i=0;
		String s="";
		while(i<bets.size()){
			 s+=bets.remove(i)+",";
		}
		return s;

	}
	/**
	 * @throws CompetitionException
	 * @throws ExistingCompetitionException
	 * @throws ClassNotFoundException
	**/
	public static List<List<String>> listCompetitions() throws SQLException, BadParametersException, ClassNotFoundException, ExistingCompetitionException, CompetitionException{

		List<List<String>> listeCompetitions = new ArrayList<>();

		LinkedList<Competition> laListeDesCompetitions = (LinkedList<Competition>)lister_Competition();

		while(!(laListeDesCompetitions==null)){

			List<String> string = (List<String>) new LinkedList<String>();
			Competition competition=laListeDesCompetitions.removeFirst();
			string.add(competition.getName()+": ");
			Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String s = formatter.format("sa date de cloture est : "+competition.getClosingDate()+",");
			string.add(s);
			string.add("Les Bets en cours sont : "+consultBetsCompetitionAsString(competition.getName())+",");
			string.add("Les competiteurs sont"+listCompetitorsAsString(competition.getName()));


			listeCompetitions.add(string);

		}
		return listeCompetitions;


	}

	/**
	 * @throws SQLException
	 * @throws BadParametersException
	 * @throws ClassNotFoundException
	 */

	public static Collection<Competitor> listCompetitors(String competition)
			throws ExistingCompetitionException, CompetitionException, SQLException, BadParametersException, ClassNotFoundException{
		if(findByName(competition)==false){throw new ExistingCompetitionException("invalid competition");}
		if(findByName1(competition).isClosed()==true){throw new CompetitionException("closed competition");}

		int idCompetition=findByName1(competition).getId();

		Connection c = DBConnection.newConnection();

		Collection<Competitor> competitors= new ArrayList<Competitor>();

		try{


		PreparedStatement preparedStatement1 = c.prepareStatement(
				"Select competitortype From competitor Where idcompetitor="
				+ "(Select competitor from competitionandcompetitor where competition="+
						Integer.toString(idCompetition)+")");

		ResultSet resultSet1 = preparedStatement1.executeQuery();




		if (resultSet1.getString("team")=="team"){
			competitors=fetchTeamCompetitors(competition);




			}


		else {
			competitors= fetchPersons(competition);

			}



		resultSet1.close();
		preparedStatement1.close();



		}
		catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}

		c.close();

		return competitors;



	}

	/**
	 * @param competition
	 * @param competitor
	 * @throws SQLException
	 */
	public static void addCompetitor(String competition, Competitor competitor) throws
			ExistingCompetitionException, CompetitionException,
			ExistingCompetitorException, BadParametersException, SQLException{
		/*
		 */

		/*
		 */
		if (findByName(competition)==false){throw new ExistingCompetitionException("invalid competition");}
		/*
		 */
		if (findByName1(competition).isClosed()==true){throw new ExistingCompetitionException("invalid competition");};
		/*
		 */
		Connection c = DBConnection.newConnection();
		/*
		 */

		try{

            PreparedStatement psInsert = c.prepareStatement("INSERT INTO competitionandcompetitor VALUES (?,?)");
            psInsert.setInt(1, ((fr.uv1.bettingServices.competitor.Competitor) competitor).getIdCompetitor());
            psInsert.setInt(2, chercherIdCompetition(c,competition));
            psInsert.executeUpdate();
            psInsert.close();
            c.commit();
		}
		catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();

    }


	/**
	 * @param competition
	 * @param competitor
	 * @throws BadParametersException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void deleteCompetitor(String competition, Competitor competitor
			) throws
			ExistingCompetitionException, CompetitionException,
			ExistingCompetitorException, SQLException, BadParametersException, ClassNotFoundException{
		/*
		 */

		/*
		 */
		if(findByName(competition)==false){throw new ExistingCompetitionException("invalid competition");};
		/*
		 */
		if(findByName1(competition).isClosed()==true){throw new CompetitionException("closed competition");}
		/*
		 */
		if(listCompetitors(competition).size()<2){throw new CompetitionException("2 competitors at least are required");}

		Connection c = DBConnection.newConnection();
		try {
            PreparedStatement psDelete = c.prepareStatement("DELETE FROM competitionandcompetitor WHERE competitor_id = ? AND competition_name = ?");
            psDelete.setInt(1, ((fr.uv1.bettingServices.competitor.Competitor) competitor).getIdCompetitor());
            psDelete.setInt(2, chercherIdCompetition(c,competition));
            psDelete.executeQuery();
            psDelete.close();
            c.commit();
		}
		catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();
		}




	/**
	 *
	 * @param competitor
	 * @param rank
	 * @param competition
	 * @throws SQLException
	 */
	private  static void rankCompetitor(Competitor competitor,String rank,String competition) throws SQLException{

    	Connection c = DBConnection.newConnection();
        try{
            PreparedStatement psUpdate = c.prepareStatement("UPDATE competition SET competitor? = ? WHERE  idcompetition = ?");
            psUpdate.setString(1, rank);
            psUpdate.setInt(2, ((fr.uv1.bettingServices.competitor.Competitor) competitor).getIdCompetitor());
            psUpdate.setInt(3, chercherIdCompetition(c,competition));
            psUpdate.executeUpdate();
            psUpdate.close();

            c.commit();
    	}
        catch (SQLException e1) {
			verifierBonneMAJ(c,e1);

		}
		c.close();
		}


    /**
     *
     * @param competition
     * @param competitor1
     * @param competitor2
     * @param competitor3
     * @throws SQLException
     * @throws BadParametersException
     * @throws ClassNotFoundException
     */
	public static void settlePodium(String competition, Competitor winner, Competitor second,
			Competitor third )
			throws  ExistingCompetitionException,
			CompetitionException, SQLException, BadParametersException, ClassNotFoundException{
    	/*


    	/*
    	 */
    	if(findByName(competition)==false){throw new ExistingCompetitionException("invalid competition");}
    	/*
    	 */
    	if(winner.equals(second) | second.equals(third) |third.equals(winner)){throw new CompetitionException
    		("invalid choice");}
    	/*
    	 */
    	if(listCompetitors(competition).size()==0){throw new CompetitionException("no competitors");}
    	/*
    	 */
    	if(listCompetitors(competition).contains(winner)==false
    		| listCompetitors(competition).contains(second)==false
    		| listCompetitors(competition).contains(third)==false)
    	{throw new CompetitionException("invalid choice");}

    	/*
    	 */
    	rankCompetitor(winner,"1",competition);
    	rankCompetitor(second,"2",competition);
    	rankCompetitor(third,"3",competition);
    }

    /**
     * @param competition
     * @param competitor
     * @throws SQLException
     * @throws BadParametersException
     * @throws ClassNotFoundException
     */
    public static void settleWinner(String competition, Competitor winner)
			throws ExistingCompetitionException,
			CompetitionException, SQLException, BadParametersException, ClassNotFoundException{
    	/*

    	 */

    	/*
    	 */

    	if(findByName(competition)==false){throw new ExistingCompetitionException("invalid competition");}
    	/*

    	/*
    	 */
    	if(listCompetitors(competition).size()==0){throw new CompetitionException("no competitors");}
    	/*
    	 */
    	if(listCompetitors(competition).contains(winner)==false)
    	{throw new CompetitionException("invalid choice");}
    	rankCompetitor(winner,"1",competition);
    }

    public static ArrayList<Competitor> consultResultsCompetition(String competition)
    		throws ExistingCompetitionException, SQLException, BadParametersException, CompetitionException, ClassNotFoundException {
    	/*
    	 */

    	if(findByName(competition)==false)
    		throw new ExistingCompetitionException("invalid competition");
    	if(findByName1(competition).isClosed()==false){throw new CompetitionException("closed competition");}


		Connection c = DBConnection.newConnection();

		ArrayList<Competitor> les_competiteurs= new ArrayList<Competitor>();


		PreparedStatement preparedStatement1 = c.prepareStatement(
				"Select competitor1,competitor2,competitor3 From competition Where name="
				+competition);

		ResultSet resultSet1 = preparedStatement1.executeQuery();

		les_competiteurs.add(findCompetitorById(resultSet1.getInt("competitor1")));
		les_competiteurs.add(findCompetitorById(resultSet1.getInt("competitor2")));
		les_competiteurs.add(findCompetitorById(resultSet1.getInt("competitor3")));

		resultSet1.close();
		c.close();
		return les_competiteurs;




    	}
	public static Competitor findCompetitorById(int idCompetitor) throws BadParametersException, ClassNotFoundException, SQLException{
		Connection c = DBConnection.newConnection();

		Competitor competitor=null;


		PreparedStatement preparedStatement1 = c.prepareStatement(
				"Select competitortype From competitor Where idcompetitor="
				+Integer.toString(idCompetitor)+")");

		ResultSet resultSet1 = preparedStatement1.executeQuery();




		if (resultSet1.getString("team")=="team"){
			competitor=TeamDAO.findByIdCompetitor(idCompetitor);




			}


		else {
			competitor= PersonDAO.findByIdCompetitor(idCompetitor);

			}




		resultSet1.close();
		preparedStatement1.close();



		c.close();

		return competitor;
		}
	}
