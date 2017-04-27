package fr.uv1.bettingServices.manager;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import fr.uv1.bettingServices.exceptions.AuthenticationException;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingSubscriberException;
import fr.uv1.bettingServices.exceptions.SubscriberException;
import fr.uv1.bettingServices.player.*;
import fr.uv1.bettingServices.competition.Competition;
import fr.uv1.bettingServices.db.DBConnection;


public class PlayersManager {

	private final static String VERIFYUSERNAME = "[A-Za-z0-9]{4,}";

	public static void AuthenticateUsername(String username,String pwdSubs) throws AuthenticationException{
		try {
			if (findPlayerByUserName(username)==null){throw new AuthenticationException("invalid player");}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if(!(findPlayerByUserName(username).getPwdPlayer()==pwdSubs)){
				throw new AuthenticationException("invalid password");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public static String subscribe(String lastName, String firstName, String username, String borndate) throws BadParametersException,SQLException {


		if(!hasValidUserName(username)){
			throw new BadParametersException("UserName invalid");
		}
		Connection connection1 = DBConnection.newConnection();
		connection1.setAutoCommit(false);
		try {

			PreparedStatement preparedStatement1 = connection1.prepareStatement("INSERT INTO player (LASTNAME, FIRSTNAME, "
					+ "USERNAME,"
					+ "PWD, BORNDATE, TOKENS) "
					+ "values (?, ?, ? , ? , ? , ?)");

			preparedStatement1.setString(1,lastName);
			preparedStatement1.setString(2, firstName);
			preparedStatement1.setString(3, username);
			preparedStatement1.setString(4, "0000");
			preparedStatement1.setDate(5, Date.valueOf(borndate));
			preparedStatement1.setLong(6, 0);

			preparedStatement1.executeUpdate();
			preparedStatement1.close();

			//TODO : look how the creation of sequence is done
		//	PreparedStatement psIdValue = connection1
			//.prepareStatement("select currval('idplayer_seq') as idplayer");

			//ResultSet resultSet = psIdValue.executeQuery();
          //  resultSet.next();
			//int id =  Integer.parseInt(resultSet.getString("idplayer"));

			connection1.commit();
			/*on va valider avec commit les executions faites*/
			//the auto-incremented iD will be settled in person object
			/* on va fermer la preparedSTatement*/

		} catch (SQLException e1) {
			try {

				connection1.rollback();

			} catch (SQLException e2) {
				e1.printStackTrace();
			}
		}
		connection1.close();
		return username;
	}

	public static Player findPlayerByUserName(String username) throws SQLException {
		Connection connection1 = DBConnection.newConnection();
		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("SELECT * FROM player WHERE USERNAME LIKE ?");
		preparedStatement1.setString(1, username);
		ResultSet resultSet1 = preparedStatement1.executeQuery();
		Player playerRecherchee = null;

		while (resultSet1.next()) {
			try {
				playerRecherchee = new Player(
						resultSet1.getString("LASTNAME"),
						resultSet1.getString("FIRSTNAME"),
						resultSet1.getString("USERNAME"),
						resultSet1.getDate("BORNDATE"),
						resultSet1.getLong("TOKENS")
						);
			} catch (BadParametersException e) {
				// TODO Auto-generated catch block
				System.out.println("error");
				e.printStackTrace();
			}
		}

		resultSet1.close();
		preparedStatement1.close();
		connection1.close();

		return playerRecherchee;
	}

	public static Player findPlayerById(int id) throws SQLException {

		Connection connection1 = DBConnection.newConnection();
		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("SELECT * FROM player WHERE id LIKE ?");
		preparedStatement1.setInt(1, id);
		ResultSet resultSet1 = preparedStatement1.executeQuery();
		Player playerRecherchee = null;

		while (resultSet1.next()) {
			try {
				playerRecherchee = new Player(
						resultSet1.getString("LASTNAME"),
						resultSet1.getString("FIRSTNAME"),
						resultSet1.getString("USERNAME"),
						resultSet1.getDate("BORNDATE"),
						resultSet1.getLong("TOKENS")
						);
			} catch (BadParametersException e) {
				// TODO Auto-generated catch block
				System.out.println("error");
				e.printStackTrace();
			}
		}

		resultSet1.close();
		preparedStatement1.close();
		connection1.close();

		return playerRecherchee;
	}

	public static long deletePlayerById(int id) throws SQLException {

		Connection connection1 = DBConnection.newConnection();
		try {
			PreparedStatement psDelete = connection1
					.prepareStatement("DELETE FROM Player WHERE id LIKE ?");

			psDelete.setInt(1,id);
			psDelete.executeUpdate();

			System.out.println("user deleted");
			psDelete.close();
			connection1.close();
			return id;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("error ");
			return -1;
		}

	}

    public static long creditSubscriberById(int id, long numberTokens) throws BadParametersException{

    	Player p=null;
		try {
			p = findPlayerById(id);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

    	if (numberTokens == 0L) {
			throw new BadParametersException("");
		}
    	if (numberTokens>0){
    		try {
				if(p!=null){
		    		p.setNbTokens(p.getNbTokens()+numberTokens);
					update(p);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		return p.getNbTokens();
    	}
    	else {
    		return -1;
    	}

    }

    public static void creditSubscriberByUserName(String username, long numberTokens)
			throws AuthenticationException, ExistingSubscriberException,
			BadParametersException, SQLException{



		Player p = findPlayerByUserName(username);
		if(p==null){throw new ExistingSubscriberException();}


    	if (numberTokens == 0L | numberTokens<0) {
			throw new BadParametersException("");
		}


    		try {

		    		p.setNbTokens(p.getNbTokens()+numberTokens);
					update(p);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}


    public static void debitSubscriber(String username, long numberTokens, String managerPwd)
			throws AuthenticationException, ExistingSubscriberException,
			SubscriberException, BadParametersException, SQLException{



		Player p = findPlayerByUserName(username);
		if(p==null){throw new ExistingSubscriberException();}


    	if (numberTokens == 0L | numberTokens<0) {
			throw new BadParametersException("");
		}

    	if (numberTokens == 0L | numberTokens<0 ) {
			throw new BadParametersException("invalid tockens");
		}
    	if (numberTokens > p.getNbTokens()){throw new SubscriberException();}
    		try {

		    		p.setNbTokens(p.getNbTokens()-numberTokens);
					update(p);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    	}


    public static long debitSubscriberByUserName(String username, long numberTokensToDebit) throws BadParametersException{


    	Player p=null;
		try {
			p = findPlayerByUserName(username);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

    	if (numberTokensToDebit == 0L) {
			throw new BadParametersException("invalid tockens");
		}
    	if (numberTokensToDebit>0&&numberTokensToDebit<=p.getNbTokens()){
    		try {
				if(p!=null){
		    		p.setNbTokens(p.getNbTokens()-numberTokensToDebit);
					update(p);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		p.setNbTokens(p.getNbTokens()-numberTokensToDebit);
    		return p.getNbTokens();
    	}
    	else {
    		return -1;
    	}

    }


	/*
	public static ArrayList<Player> listPlayers() throws SQLException {
		Connection connection1 = DBConnection.newConnection();
		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("SELECT * FROM player ORDER BY username, first_name, last_name");
		ResultSet resultSet1 = preparedStatement1.executeQuery();

		*/
		/*on va creer un liste pour contenir tous les elements trouves dans la resultset*/
	/*	ArrayList<Player> playerList = new ArrayList<Player>();
		Player player = null;
		int count = 0;
		while (resultSet1.next()) {
			count ++;
			try {
				player = new Player(
						resultSet1.getString("LASTNAME"),
						resultSet1.getString("FIRSTNAME"),
						resultSet1.getString("USERNAME"),
						resultSet1.getDate("BORNDATE"),
						resultSet1.getLong("TOKENS")
						);
			} catch (BadParametersException e) {
				// TODO Auto-generated catch block
				System.out.println("erreur est survenue pour trouver la liste des players, veuillez reessayer");
				e.printStackTrace();
			}
			playerList.add(player);
		}
		resultSet1.close();
		preparedStatement1.close();
		connection1.close();

		return playerList;
	}
*/

	public static List<List<String>> listPlayers() throws SQLException {

		Connection connection1 = DBConnection.newConnection();
		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("SELECT * FROM player ORDER BY username");
		ResultSet resultSet1 = preparedStatement1.executeQuery();

		List<List<String>> listeePlayers = new ArrayList<>();
		List<String> list =new ArrayList<>();

		while (resultSet1.next()) {

			while (resultSet1.next()) {
						list.add("le nom du player est"+ resultSet1.getString("LASTNAME"));
						list.add("le prenom du player est "+resultSet1.getString("FIRSTNAME"));
						list.add("le username du player est "+resultSet1.getString("USERNAME"));
						list.add("la date de naissance du player est "+resultSet1.getDate("BORNDATE").toString()	);
						list.add("le nombre du jetons restant est "+new Long(resultSet1.getLong("TOKENS")).toString());
						listeePlayers.add(list);
			}
		}

		resultSet1.close();
		preparedStatement1.close();
		connection1.close();
		return listeePlayers;
	}

	public static ArrayList<String> infosSubscriber(String username,String pwdSubs) throws SQLException{

		Connection connection1 = DBConnection.newConnection();
		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("SELECT * FROM player WHERE USERNAME LIKE ? AND PASSWORD LIKE ?");
		preparedStatement1.setString(1, username);
		preparedStatement1.setString(2, pwdSubs);
		ResultSet resultSet1 = preparedStatement1.executeQuery();
		ArrayList<String> list = new ArrayList<String>();
		while (resultSet1.next()) {

					list.add(resultSet1.getString("LASTNAME"));
					list.add(resultSet1.getString("FIRSTNAME"));
					list.add(resultSet1.getString("USERNAME"));
					list.add(resultSet1.getDate("BORNDATE").toString()	);
					list.add(new Long(resultSet1.getLong("TOKENS")).toString());
		}

		resultSet1.close();
		preparedStatement1.close();
		connection1.close();


		return list;
	}

	public static Player update(Player player) throws SQLException {
		Connection connection1 = DBConnection.newConnection();
		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("UPDATE player SET LASTNAME=?, FIRSTNAME=?, BORNDATE=?, TOKENS=? WHERE USERNAME LIKE ?");

		preparedStatement1.setString(1, player.getLastName());
		preparedStatement1.setString(2, player.getFirstName());
		preparedStatement1.setString(3, player.getUserName());
		preparedStatement1.setDate(4, java.sql.Date.valueOf(player.getBornDate().toString()));
		preparedStatement1.setLong(5, player.getNbTokens());

		preparedStatement1.executeUpdate();

		preparedStatement1.close();

		connection1.close();
		return player;
	}

	public static void changeSubsPwd(String username, String newPwd, String currentPwd) throws SQLException {

		Connection connection1 = DBConnection.newConnection();

		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("SELECT * FROM player WHERE USERNAME LIKE ? AND password LIKE ?");
		preparedStatement1.setString(1, username);
		preparedStatement1.setString(2, currentPwd);
		ResultSet resultSet1 = preparedStatement1.executeQuery();
		preparedStatement1.close();
		if(resultSet1.next()== true){

			PreparedStatement preparedStatement2 = connection1.prepareStatement("UPDATE player SET PWD=? WHERE USERNAME LIKE ?");
			preparedStatement2.setString(1, newPwd);
			preparedStatement2.setString(2, username);
			preparedStatement2.executeUpdate();
			preparedStatement2.close();
		}
			resultSet1.close();
			connection1.close();
		}


	public static long unsubscribe(String username) throws SQLException {

		Player p=null;
		p=PlayersManager.findPlayerByUserName(username);
			if(p!=null){
		Connection connection1 = DBConnection.newConnection();
		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("DELETE FROM player WHERE USERNAME LIKE ?");
		preparedStatement1.setString(1, username );
		Scanner sc = new Scanner(System.in);
		int i = sc.nextInt();

		System.out.println("0/1");
		if(i==0){
			preparedStatement1.executeUpdate();
			preparedStatement1.close();
			connection1.close();
			return p.getNbTokens();
		}
		else{
			preparedStatement1.close();
			connection1.close();
			return p.getNbTokens();
			}
		}
			return -1;

	}


	public static void deleteBetsCompetition(String competition, String username, String pwdSubs)
			throws AuthenticationException, CompetitionException, ExistingCompetitionException, SQLException, BadParametersException {
		/*
		 */
		if(CompetitionManager.findByName(competition)==false){throw new ExistingCompetitionException("invalid competition");}
		Competition compet=CompetitionManager.findByName1(competition);
		Player user=findPlayerByUserName(username);

		/*
		 */
		if(compet.isClosed()==true){throw new CompetitionException("closed cometitipn");}
		Connection connection1 = DBConnection.newConnection();
		PreparedStatement preparedStatement1 = connection1
				.prepareStatement("DELETE FROM bet WHERE player=? AND competition=?");
		preparedStatement1.setString(1, Integer.toString(user.getId()) );
		preparedStatement1.setString(2, Integer.toString(compet.getId()) );
		preparedStatement1.executeUpdate();
		preparedStatement1.close();
		connection1.close();

	}

	public static boolean hasValidUserName(String username) {

		return username.matches(VERIFYUSERNAME);
	}

}
