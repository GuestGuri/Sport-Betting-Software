package fr.uv1.bettingServices.competitor;


import fr.uv1.bettingServices.db.DBConnection;
import fr.uv1.bettingServices.exceptions.BadParametersException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 *
 */


public class TeamDAO {
	/**
	 *
	 * @param team
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void persist(Team team) throws ClassNotFoundException, SQLException{
		TeamDAO.persistTeam(team);
		TeamDAO.persistCompetitor(team);
	}
	public static Team persistTeam(Team team) throws SQLException, ClassNotFoundException {
		Connection c = DBConnection.newConnection();
		c.setAutoCommit(false);

		PreparedStatement psPersist = c.prepareStatement("insert into team (teamname)  values (?)");
		psPersist.setString(1, team.getName());
		psPersist.executeUpdate();
		psPersist.close();

		PreparedStatement psIdValue = c.prepareStatement("SELECT idcompetitor_seq.currval from dual");
		ResultSet resultSet = psIdValue.executeQuery();
		int id=(Integer) null;
		if (resultSet.next()) {
            id = resultSet.getInt("idteam");
        }
		c.commit();
		team.setId(id);
		c.close();

		return team;
	}
	/**
	 *
	 * @param competitor
	 * @return
	 * @throws SQLException
	 */
	public static Competitor persistCompetitor(Team competitor) throws SQLException {
		Connection c=null;
		try{

			c = DBConnection.newConnection();
			c.setAutoCommit(false);
            PreparedStatement psPersist = c.prepareStatement("INSERT INTO competitor(type, team)  VALUES ('team', ?)");
            psPersist.setInt(1, competitor.getId());
            psPersist.executeUpdate();
            psPersist.close();

            PreparedStatement psIdValue = c
			.prepareStatement("select idcompetitor_seq.currval from dual");

			ResultSet resultSet = psIdValue.executeQuery();
            resultSet.next();
			int id =  Integer.parseInt(resultSet.getString("idcompetitor"));

			c.commit();
			//the auto-incremented iD will be settled in person object

			competitor.setIdCompetitor(id);


        } catch (SQLException e) {
            e.printStackTrace();
            throw e;

        	} finally {
    			c.setAutoCommit(true);
    			c.close();
        	}
        return competitor;
	}
	/**
	 *
	 * @param team
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */

	public static void updateTeam(Team team) throws SQLException, ClassNotFoundException {
		Connection c = DBConnection.newConnection();
		PreparedStatement psUpdate = c.prepareStatement("update team set teamname=? where id=?");
		psUpdate.setString(1, team.getName());
		psUpdate.setInt(2, team.getId());
		psUpdate.executeUpdate();
		psUpdate.close();
		c.close();
	}



	/**
	 * Persist a competitor into a new team in the database
	 * @param team
	 * @param competitor
	 * @throws ClassNotFoundException
     */
	public static void addCompetitorToTeam(Team team, Person competitor) throws ClassNotFoundException {
		try(Connection c = DBConnection.newConnection()) {
            // Check if the competitor is in the database:
            Person comptSaved = PersonDAO.findOrCreateCompetitor(competitor);
            Team teamSaved = findOrCreateTeam(team);

			PreparedStatement psUpdate = c.prepareStatement("update PERSON set TEAM= ?  WHERe ID=?");
			psUpdate.setInt(1, teamSaved.getId());
			psUpdate.setInt(2, comptSaved.getId());
			psUpdate.executeUpdate();
		} catch (SQLException | BadParametersException e) {
			e.printStackTrace();
		}
    }
	/**
	 *
	 * @param team
	 * @param competitor
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void deleteCompetitor(Team team,Person competitor) throws SQLException, ClassNotFoundException {
		Connection c = DBConnection.newConnection();
		PreparedStatement psUpdate = c.prepareStatement("update PERSON set TEAM=null  WHERe ID=?");
		psUpdate.setInt(1, competitor.getId());
		psUpdate.setInt(2, team.getId());
		psUpdate.executeUpdate();
		psUpdate.close();
		c.close();
	}

	/**
	 * Search in the database if a competitor exist, and create one if necessary
     * @param name
	 * @return
	 * @throws BadParametersException
	 * @throws ClassNotFoundException
	 * @throws SQLException
     */
    public static Team findOrCreateTeam(String name) throws BadParametersException, ClassNotFoundException, SQLException {
        try(Connection conn = DBConnection.newConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM team WHERE teamname=? ");
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
            Team competitor = new Team(rs.getString("teamname"));
            competitor.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Team team = new Team(name);
        persistCompetitor(team);
        return team;
    }

    public static Team findOrCreateTeam(Team competitor) throws BadParametersException, ClassNotFoundException, SQLException {
        return findOrCreateTeam(competitor.getName());
    }

	public static Team findById(int id) throws BadParametersException, ClassNotFoundException {
        Connection c;
        Team competitor = null;
        try {
            c = DBConnection.newConnection();
            PreparedStatement psSelect = c.prepareStatement("select * from team where id=?");
            psSelect.setInt(1, id);
            ResultSet resultSet = psSelect.executeQuery();

            if (resultSet.next())
                competitor = new Team(resultSet.getString("teamname"));
            	competitor.setId(id);
            resultSet.close();
            psSelect.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return competitor;
	}
	public static Team findByIdCompetitor(int id) throws BadParametersException, ClassNotFoundException {
        Connection c;
        Team competitor = null;
        try {
            c = DBConnection.newConnection();
            PreparedStatement psSelect = c.prepareStatement("select * from Competitor where idcompetitor=?");
            psSelect.setInt(1, id);
            ResultSet resultSet = psSelect.executeQuery();

            if (resultSet.next())
                competitor = new Team();
            	int idTeam=resultSet.getInt("team");
            	competitor.setId(idTeam);
            	competitor.setName(findById(id).getName());

            resultSet.close();
            psSelect.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return competitor;
	}

}
