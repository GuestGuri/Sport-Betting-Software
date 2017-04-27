package fr.uv1.bettingServices.competitor;


import fr.uv1.bettingServices.db.DBConnection;
import fr.uv1.bettingServices.exceptions.BadParametersException;

import java.sql.*;



/**
 *
 */

public class PersonDAO {
	/**
	 * Insert data in the database tables :(Competitor et Person)
	 * @param person
	 * @throws SQLException
	 */

	public static void persist(Person person) throws SQLException {
		persistPerson(person);
		persistCompetitor(person);
	}
	/**
	 * Insert data in the database tables : Person
	 * @param person
	 *
	 */
	public static Person persistPerson(Person person) {
		try (
			Connection c = DBConnection.newConnection()){
			c.setAutoCommit(false);
			//TODO: verify the column names
			PreparedStatement psPersist = c.prepareStatement("INSERT INTO Person(firstname, lastname, borndate) VALUES (?, ?, ?)");

			//the id will be auto-incremented
			psPersist.setString(1, person.getFirstName());
			psPersist.setString(2, person.getLastName());
			psPersist.setDate(3, person.getBornDate());

			psPersist.executeUpdate();
			psPersist.close();

			//TODO : look how the creation of sequence is done
			PreparedStatement psIdValue = c.prepareStatement("select idperson_seq.currval from dual");

			ResultSet resultSet = psIdValue.executeQuery();
            resultSet.next();
			int id =  resultSet.getInt("idperson");

			c.commit();
			//the auto-incremented iD will be settled in person object

			person.setId(id);
		} catch (SQLException e) {
            e.printStackTrace();
		}
		return person;
	}
	/**
	 * Insert data in the database tables : Competitor
	 * @param person
	 *
	 * @throws SQLException
	 */
	public static Competitor persistCompetitor(Person person) throws SQLException {
		Connection c=null;
		try {
			c = DBConnection.newConnection();
			c.setAutoCommit(false);
			//TODO: verify the column names
			PreparedStatement psPersist = c.prepareStatement("INSERT INTO competitor(competitortype,person) VALUES ('person', ?)");

			//the id will be auto-incremented
			psPersist.setInt(1, person.getId());

			psPersist.executeUpdate();
			psPersist.close();

			//TODO : look how the creation of sequence is done
			PreparedStatement psIdValue = c
			.prepareStatement("select idcompetitor_seq.currval from dual");

			ResultSet resultSet = psIdValue.executeQuery();
            resultSet.next();
			int id =  Integer.parseInt(resultSet.getString("idcompetitor"));

			c.commit();
			//the auto-incremented iD will be settled in person object

			person.setIdCompetitor(id);
		} catch (SQLException e) {
            e.printStackTrace();
            c.setAutoCommit(true);
            throw e;
		}
		finally {
			c.close();
		}
		return person;
	}


	/**
	 * Search for a specific competitor by his Id
	 * @param id
	 * @return
	 * @throws BadParametersException
	 */
	public static Person findById(int id) throws BadParametersException {
        Person competitor = null;
        try(Connection c = DBConnection.newConnection()) {
            PreparedStatement psSelect = c
                    .prepareStatement("SELECT * FROM person WHERE id=?");

            psSelect.setInt(1, id);
            ResultSet resultSet = psSelect.executeQuery();
            while (resultSet.next()) {
                competitor = new Person(resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getDate("borndate"));

            }
            c.commit();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        	}
        return competitor;
    }
		/**
		 *
		 * @param id
		 * @return competitor
		 * @throws BadParametersException
		 */
    	public static Person findByIdCompetitor(int id) throws BadParametersException {
            Person competitor = null;
            try(Connection c = DBConnection.newConnection()) {
                PreparedStatement psSelect = c
                        .prepareStatement("SELECT * FROM competitor WHERE id=?");

                psSelect.setInt(1, id);
                ResultSet resultSet = psSelect.executeQuery();
                while (resultSet.next()) {
                    int idPerson=resultSet.getInt("Person");
                    competitor=findById(idPerson);

                }
                c.commit();
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }


        return competitor;
	}

	/**
	 * update the person in the table competitor
	 *
	 * @param competitor
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */

	public static void update(Person competitor) throws SQLException, ClassNotFoundException {
		Connection c = DBConnection.newConnection();
		PreparedStatement psUpdate = c
				.prepareStatement("update person set firtsname=?, lastname=?, borndate=? where id=?");

		psUpdate.setString(1, competitor.getFirstName());
		psUpdate.setString(2, competitor.getLastName());
		psUpdate.setDate(3, new java.sql.Date(competitor.getBornDate().getTime()));

		psUpdate.setInt(4, competitor.getId());
		psUpdate.executeUpdate();
		psUpdate.close();
		c.commit();
		c.close();
	}


	/**
	 * Delete a specific competitor
	 * @param competitor
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public static void delete(Person competitor) throws SQLException, ClassNotFoundException {
		Connection c = DBConnection.newConnection();
		PreparedStatement psUpdate = c
				.prepareStatement("delete from person where id=?");
		psUpdate.setInt(1, competitor.getId());
		psUpdate.executeUpdate();
		psUpdate.close();
		c.commit();
		c.close();
	}
	/**
	 *
	 * @param lastName
	 * @param firstName
	 * @param borndate
	 * @return The competitor
	 * @throws BadParametersException
	 * @throws SQLException
	 */

	public static Person findOrCreateCompetitor(String lastName, String firstName, Date borndate) throws BadParametersException, SQLException {
		Date borndateSQL = new Date(borndate.getTime());
		try(Connection conn = DBConnection.newConnection()) {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM person WHERE lastname=? AND firstname=? AND borndate=? ");
			ps.setString(1, lastName);
			ps.setString(2, firstName);
			ps.setDate(3, borndateSQL);

			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				Person competitor = new Person(firstName, lastName, borndate);
				competitor.setId(rs.getInt("id"));
			}
		} catch (BadParametersException | SQLException e) {
            e.printStackTrace();
        }

        Person comp = new Person(firstName, lastName, borndate);
        persist(comp);

        return comp;
	}
	/**
	 *
	 * @param competitor
	 * @return The competitor
	 * @throws BadParametersException
	 * @throws SQLException
	 */
	 public static Person findOrCreateCompetitor(Person competitor) throws BadParametersException, SQLException {
	        return findOrCreateCompetitor(competitor.getLastName(), competitor.getFirstName(), competitor.getBornDate());
	    }
}
