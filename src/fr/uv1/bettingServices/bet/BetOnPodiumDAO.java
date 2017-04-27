package fr.uv1.bettingServices.bet;


import fr.uv1.bettingServices.bet.BetOnPodium;
import fr.uv1.bettingServices.competition.Competition;
import fr.uv1.bettingServices.competitor.Competitor;
import fr.uv1.bettingServices.competitor.PersonDAO;
import fr.uv1.bettingServices.competitor.TeamDAO;
import fr.uv1.bettingServices.db.DBConnection;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitionException;
import fr.uv1.bettingServices.manager.CompetitionManager;
import fr.uv1.bettingServices.player.Player;
import fr.uv1.bettingServices.manager.PlayersManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
//import fr.uv1.jdbcExample.*;
//import fr.uv1.utils.DatabaseConnection;

/**
 *
 *
 */
public class BetOnPodiumDAO {
	// -----------------------------------------------------------------------------
	/**
	 * Store a BetOnPodium in the database for a specific subscriber
	 * @param bet
	 *            the bet to be stored.
	 * @return the bet with the updated value for the id.
	 * @throws SQLException
	 */




	public static BetOnPodium persist(BetOnPodium bet) throws SQLException {

		// Persist a BetOnPodium when
		// (unique) transaction:
		//

		Connection c = null;
		try {
			c = DBConnection.newConnection();
			c.setAutoCommit(false);
			PreparedStatement psPersist = c.prepareStatement("insert into bet( tokens, player ,competition, COMPETITOR1,competitor2,competitor3) values (?,?,?,?,?,?) ");
			psPersist.setLong(1, bet.getTokens());
			psPersist.setInt(2, bet.getBetter().getId());
			//TODO : add getId method in player and competition
			psPersist.setInt(3, bet.getCompetition().getId());
			psPersist.setInt(4,((Competitor) bet.getWinner()).getIdCompetitor());
			psPersist.setInt(5,((Competitor) bet.getSecond()).getIdCompetitor());

			psPersist.setInt(6,((Competitor) bet.getThird()).getIdCompetitor());

			psPersist.executeUpdate();
			psPersist.close();
			c.commit();
		}

		catch (SQLException e) {
			c.rollback();
			throw e;
		} finally {
			c.setAutoCommit(true);
			c.close();
		}
		return bet;

	}

	// -----------------------------------------------------------------------------


	/**
	 * Find a bet by his id.
	 *
	 * @param id
	 *            the id of the bet to retrieve.
	 * @return the bet or null if the id does not exist in the database.
	 * @throws SQLException
	 * @throws BadParametersException
	 * @throws ExistingCompetitionException
	 * @throws ClassNotFoundException
	 * @throws CompetitionException
	 */


	public static BetOnPodium findById(Integer id) throws BadParametersException, ExistingCompetitionException, ClassNotFoundException, CompetitionException {
        BetOnPodium bet = null;

        try (Connection c = DBConnection.newConnection())  {

			PreparedStatement psSelect = c
					.prepareStatement("select (idbet,tokens,player,competition,competitor1,competitor2,competitor3) from bet where id=?");
			ResultSet resultSet = psSelect.executeQuery();

			while (resultSet.next()) {
				bet = new BetOnPodium(resultSet.getInt("idbet"));

				bet.setTokens(resultSet.getLong("tokens"));

				bet.setBetter(PlayersManager.findPlayerById(resultSet.getInt("player")));
				//TODO: add method findById in competition manager @naanani @gargouri
				bet.setCompetition(CompetitionManager.findById(resultSet.getInt("competition")));

				if (PersonDAO.findById(resultSet.getInt("competitor1")) != null ){

					bet.setWinner((fr.uv1.bettingServices.Competitor) PersonDAO.findById(resultSet.getInt("competitor1")));
					bet.setSecond((fr.uv1.bettingServices.Competitor) PersonDAO.findById(resultSet.getInt("competitor2")));
					bet.setThird((fr.uv1.bettingServices.Competitor) PersonDAO.findById(resultSet.getInt("competitor3")));
				}

				else if(TeamDAO.findById(resultSet.getInt("competitor1")) != null)  {

					bet.setWinner(TeamDAO.findById(resultSet.getInt("competitor1")));
					bet.setSecond(TeamDAO.findById(resultSet.getInt("competitor2")));

					bet.setThird(TeamDAO.findById(resultSet.getInt("competitor3")));

				}
			}
        }


		 catch (SQLException  e) {
			e.printStackTrace();
		}
        return bet;
	}


	// -----------------------------------------------------------------------------

	/**
	 * Find all the bets for a specific subscriber in the database.
	 *
	 * @return
	 * @throws SQLException
	 * @throws ExistingCompetitionException
	 * @throws BadParametersException
	 * @throws ClassNotFoundException
	 * @throws CompetitionException
	 */
	public List<BetOnPodium> findBySubscriber(Player player) throws SQLException, BadParametersException, ExistingCompetitionException, ClassNotFoundException, CompetitionException {
		Connection c = null;
		try{
			c = DBConnection.newConnection();

		PreparedStatement psSelect = c
				.prepareStatement("select(idbet,tokens,player,competition,competitor1,competitor2,competitor3) from bet where player=? ");
		psSelect.setInt(1, player.getId());
		ResultSet resultSet = psSelect.executeQuery();
		List<BetOnPodium> bets = new ArrayList<BetOnPodium>();
		while (resultSet.next()) {
			BetOnPodium bet=new BetOnPodium(resultSet.getInt("idbet"));
			bet.setBetter(player);
			//TODO : add method findById in CompetitionManager @naanani @gargouri
			bet.setCompetition(CompetitionManager.findById(resultSet.getInt("competition")));
			bet.setTokens(resultSet.getInt("tokens"));
			if (PersonDAO.findById(resultSet.getInt("competitor1")) != null ){

				bet.setWinner((fr.uv1.bettingServices.Competitor) PersonDAO.findById(resultSet.getInt("competitor1")));
				bet.setSecond((fr.uv1.bettingServices.Competitor) PersonDAO.findById(resultSet.getInt("competitor2")));
				bet.setThird((fr.uv1.bettingServices.Competitor) PersonDAO.findById(resultSet.getInt("competitor3")));
			}

			else if(TeamDAO.findById(resultSet.getInt("competitor1")) != null)  {

				bet.setWinner(TeamDAO.findById(resultSet.getInt("competitor1")));
				bet.setSecond(TeamDAO.findById(resultSet.getInt("competitor2")));

				bet.setThird(TeamDAO.findById(resultSet.getInt("competitor3")));

			}
			bets.add(bet);
		}
		resultSet.close();
		psSelect.close();
			return bets;
		}catch(SQLException e2){
			throw e2;
		}
		finally{
		c.close();
		}

	}
	//------------------------------------------------------------------------------
		/**
		 * Find all bets by a competition
		 *
		 * @param competition
		 * 					the competition.
		 *
		 * @return all bets
		 * @throws ExistingCompetitionException
		 * @throws BadParametersException
		 * @throws ClassNotFoundException
		 * @throws CompetitionException
		 *
		 */
		public static List<BetOnPodium> findByCompetition(Competition competition) throws ClassNotFoundException, BadParametersException, ExistingCompetitionException, CompetitionException {
            List<BetOnPodium> bets = new ArrayList<BetOnPodium>();
			try(Connection c = DBConnection.newConnection()){
				PreparedStatement psSelect = c
						.prepareStatement("SELECT * FROM bet WHERE competition=?");
				//TODO: add getId() method in competition @naanani @gargouri
				psSelect.setInt(1, competition.getId());
				ResultSet resultSet = psSelect.executeQuery();

				while (resultSet.next()) {
					bets.add(cursorToBetOnPodium(resultSet));
				}
				resultSet.close();
				psSelect.close();
				return bets;
			} catch(SQLException e) {
				e.printStackTrace();
			}
            return bets;
		}

	// -----------------------------------------------------------------------------
	/**
	 * Find all the bets in the database.
	 *
	 * @return
	 * @throws SQLException
	 * @throws ExistingCompetitionException
	 * @throws BadParametersException
	 * @throws ClassNotFoundException
	 * @throws CompetitionException
	 */
	public static List<BetOnPodium> findAll() throws SQLException, ClassNotFoundException, BadParametersException, ExistingCompetitionException, CompetitionException {
		Connection c = null;
		try {
		c = DBConnection.newConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from bet order by username,id");
		ResultSet resultSet = psSelect.executeQuery();
		List<BetOnPodium> bets = new ArrayList<BetOnPodium>();
		while (resultSet.next()) {
			bets.add(cursorToBetOnPodium(resultSet));
		}
		resultSet.close();
		psSelect.close();
			return bets;
		}catch(SQLException e2){
			throw e2;
		}finally{
		c.close();
		}

	}

	// -----------------------------------------------------------------------------
	/**
	 * Update on the database the values from a bet.
	 *
	 * each time, a method of set is called, it update all the values, usable by all
	 *
	 * @param bet
	 *            the bet to be updated.
	 * @throws SQLException
	 */
	public static void update(BetOnPodium bet) throws SQLException {
		Connection c = null;
		try{
		 c = DBConnection.newConnection();
		PreparedStatement psUpdate = c
				.prepareStatement("update bet set token=?, player=?,competition=?,Competitor1 = ?,Competitor2 = ?,Competitor3 = ? where id=?");
		psUpdate.setLong(1, bet.getTokens());
		psUpdate.setInt(2, bet.getBetter().getId());
		psUpdate.setInt(3, bet.getCompetition().getId());
		psUpdate.setInt(4,((Competitor) bet.getWinner()).getIdCompetitor());
		psUpdate.setInt(5,((Competitor) bet.getSecond()).getIdCompetitor());
		psUpdate.setInt(6,((Competitor) bet.getThird()).getIdCompetitor());

		psUpdate.setInt(7,bet.getId());
		psUpdate.executeUpdate();
		psUpdate.close();
		c.commit();


		} catch (SQLException  e2) {
			c.rollback();
			throw e2;
		}
		finally {
			c.setAutoCommit(true);
			c.close();
		}
	}

	// -----------------------------------------------------------------------------
	/**
	 * Delete from the database a specific bet.
	 *
	 * @param bet
	 *            the bet to be deleted.
	 * @throws SQLException
	 */
	public void deleteBet(BetOnPodium bet) throws SQLException, ClassNotFoundException {
		Connection c = null;
        try {
        	c = DBConnection.newConnection();
            PreparedStatement psUpdate = c.prepareStatement("delete from bet where id=?");
            psUpdate.setInt(1, bet.getId());
            psUpdate.executeUpdate();
            psUpdate.close();
            c.commit();

        } catch (SQLException e) {
        	c.rollback();
            throw e;
        }finally {
			c.setAutoCommit(true);
			c.close();
		}
    }

	private static BetOnPodium cursorToBetOnPodium(ResultSet result) throws SQLException, BadParametersException, ExistingCompetitionException, ClassNotFoundException, CompetitionException {
		BetOnPodium bet = new BetOnPodium(result.getInt("id"));
		//TODO: add findById() method in CompetitionManager @naanani @gargouri
        Competition comp = CompetitionManager.findById(result.getInt("competition"));

        bet.setTokens(result.getInt("token_nbr"));
        bet.setBetter(PlayersManager.findPlayerById(result.getInt("player")));
        bet.setCompetition(comp);
        if (PersonDAO.findByIdCompetitor(result.getInt("competitor1")) != null ){

			bet.setWinner((fr.uv1.bettingServices.Competitor) PersonDAO.findById(result.getInt("competitor1")));
			bet.setSecond((fr.uv1.bettingServices.Competitor) PersonDAO.findById(result.getInt("competitor2")));
			bet.setThird((fr.uv1.bettingServices.Competitor) PersonDAO.findById(result.getInt("competitor3")));

        }
		else if(TeamDAO.findByIdCompetitor(result.getInt("competitor1")) != null) {

			bet.setWinner(TeamDAO.findByIdCompetitor(result.getInt("competitor1")));
			bet.setSecond(TeamDAO.findByIdCompetitor(result.getInt("competitor2")));
			bet.setThird(TeamDAO.findByIdCompetitor(result.getInt("competitor3")));
		}

        return bet;
	}

}
