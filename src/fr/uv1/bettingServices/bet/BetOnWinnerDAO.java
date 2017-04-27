package fr.uv1.bettingServices.bet;


import fr.uv1.bettingServices.bet.BetOnWinner;
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
public class BetOnWinnerDAO {
	// -----------------------------------------------------------------------------
	/**
	 * Store a BetOnWinner in the database for a specific subscriber
	 * @param bet
	 *            the bet to be stored.
	 * @return the bet with the updated value for the id.
	 * @throws SQLException
	 */




	public static BetOnWinner persist(BetOnWinner bet) throws SQLException {

		// Persist a BetOnWinner when
		// (unique) transaction:
		//

		Connection c = null;
		try {
			c = DBConnection.newConnection();
			c.setAutoCommit(false);
			PreparedStatement psPersist = c.prepareStatement("insert into bet( tokens, player ,competition, COMPETITOR1) values (?,?,?,?) ");
			psPersist.setLong(1, bet.getTokens());
			psPersist.setInt(2, bet.getBetter().getId());
			//TODO : add getId method in player and competition
			psPersist.setInt(3, bet.getCompetition().getId());
			psPersist.setInt(4,((Competitor) bet.getCompetitor()).getIdCompetitor());
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


	public static BetOnWinner findById(Integer id) throws BadParametersException, ExistingCompetitionException, ClassNotFoundException, CompetitionException {
        BetOnWinner bet = null;

        try (Connection c = DBConnection.newConnection())  {

			PreparedStatement psSelect = c
					.prepareStatement("select (idbet,tokens,player,competition,competitor1,competitor2,competitor3) from bet where id=?");
			ResultSet resultSet = psSelect.executeQuery();

			while (resultSet.next()) {
				bet = new BetOnWinner(resultSet.getInt("idbet"));

				bet.setTokens(resultSet.getLong("tokens"));

				bet.setBetter(PlayersManager.findPlayerById(resultSet.getInt("player")));
				//TODO: add method findById in competition manager @naanani @gargouri
				bet.setCompetition(CompetitionManager.findById(resultSet.getInt("competition")));

				if (PersonDAO.findById(resultSet.getInt("competitor1")) != null ){

					bet.setCompetitor((fr.uv1.bettingServices.Competitor) PersonDAO.findById(resultSet.getInt("competitor1")));
				}
				else if(TeamDAO.findById(resultSet.getInt("competitor1")) != null)  {

					bet.setCompetitor(TeamDAO.findById(resultSet.getInt("competitor1")));
				}
			}

		} catch (SQLException  e) {
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
	public List<BetOnWinner> findBySubscriber(Player player) throws SQLException, BadParametersException, ExistingCompetitionException, ClassNotFoundException, CompetitionException {
		Connection c = null;
		try{
			c = DBConnection.newConnection();

		PreparedStatement psSelect = c
				.prepareStatement("select(idbet,tokens,player,competition,competitor1,competitor2,competitor3) from bet where player=? ");
		psSelect.setInt(1, player.getId());
		ResultSet resultSet = psSelect.executeQuery();
		List<BetOnWinner> bets = new ArrayList<BetOnWinner>();
		while (resultSet.next()) {
			BetOnWinner bet=new BetOnWinner(resultSet.getInt("idbet"));
			bet.setBetter(player);
			//TODO : add method findById in CompetitionManager @naanani @gargouri
			bet.setCompetition(CompetitionManager.findById(resultSet.getInt("competition")));
			bet.setTokens(resultSet.getInt("tokens"));
			if (PersonDAO.findByIdCompetitor(resultSet.getInt("competitor1")) != null ){

				bet.setCompetitor((fr.uv1.bettingServices.Competitor) PersonDAO.findById(resultSet.getInt("competitor1")));
			}
			else if(TeamDAO.findByIdCompetitor(resultSet.getInt("competitor1")) != null) {

				bet.setCompetitor(TeamDAO.findByIdCompetitor(resultSet.getInt("competitor1")));
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
		public static List<BetOnWinner> findByCompetition(Competition competition) throws ClassNotFoundException, BadParametersException, ExistingCompetitionException, CompetitionException {
            List<BetOnWinner> bets = new ArrayList<BetOnWinner>();
			try(Connection c = DBConnection.newConnection()){
				PreparedStatement psSelect = c
						.prepareStatement("SELECT * FROM bet WHERE competition=?");
				//TODO: add getId() method in competition @naanani @gargouri
				psSelect.setInt(1, competition.getId());
				ResultSet resultSet = psSelect.executeQuery();

				while (resultSet.next()) {
					bets.add(cursorToBetOnWinner(resultSet));
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
	public static List<BetOnWinner> findAll() throws SQLException, ClassNotFoundException, BadParametersException, ExistingCompetitionException, CompetitionException {
		Connection c = null;
		try {
		c = DBConnection.newConnection();
		PreparedStatement psSelect = c
				.prepareStatement("select * from bet order by username,id");
		ResultSet resultSet = psSelect.executeQuery();
		List<BetOnWinner> bets = new ArrayList<BetOnWinner>();
		while (resultSet.next()) {
			bets.add(cursorToBetOnWinner(resultSet));
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
	public static void update(BetOnWinner bet) throws SQLException {
		Connection c = null;
		try{
		 c = DBConnection.newConnection();
		PreparedStatement psUpdate = c
				.prepareStatement("update bet set token=?, player=?,competition=?,Competitor1 = ? where id=?");
		psUpdate.setLong(1, bet.getTokens());
		psUpdate.setInt(2, bet.getBetter().getId());
		//TODO : add getId() in Competition @naanani @gargouri
		psUpdate.setInt(3, bet.getCompetition().getId());
		psUpdate.setInt(4,((Competitor) bet.getCompetitor()).getIdCompetitor());
		psUpdate.setInt(5,bet.getId());
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
	public void deleteBet(BetOnWinner bet) throws SQLException, ClassNotFoundException {
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

	private static BetOnWinner cursorToBetOnWinner(ResultSet result) throws SQLException, BadParametersException, ExistingCompetitionException, ClassNotFoundException, CompetitionException {
		BetOnWinner bet = new BetOnWinner(result.getInt("id"));
		//TODO: add findById() method in CompetitionManager @naanani @gargouri
        Competition comp = CompetitionManager.findById(result.getInt("competition"));

        bet.setTokens(result.getInt("token_nbr"));
        bet.setBetter(PlayersManager.findPlayerById(result.getInt("player")));
        bet.setCompetition(comp);
        if (PersonDAO.findByIdCompetitor(result.getInt("competitor1")) != null ){

			bet.setCompetitor((fr.uv1.bettingServices.Competitor) PersonDAO.findById(result.getInt("competitor1")));
        }
		else if(TeamDAO.findByIdCompetitor(result.getInt("competitor1")) != null)  {

			bet.setCompetitor(TeamDAO.findByIdCompetitor(result.getInt("competitor1")));

		}
        return bet;
	}

}
