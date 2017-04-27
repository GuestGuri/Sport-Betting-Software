package competitor;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import fr.uv1.bettingServices.Competitor;
import fr.uv1.bettingServices.competitor.Person;
import fr.uv1.bettingServices.competitor.Team;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;
/**
 *
 *
 */
public class TeamTest {
	private String teamName = "Team-Test 2015";
    @SuppressWarnings("deprecation")
    private Calendar cal1=Calendar.getInstance();
	private Date cal ;
    Team team;

	@Before
	public void beforeTests() throws BadParametersException, ClassNotFoundException, ExistingCompetitorException, SQLException{
		this.team = new Team(this.teamName);

		cal1.set(1995, Calendar.DECEMBER,24);
		cal=new Date(cal1.getTimeInMillis());
		this.team.addMember((Competitor) new Person("aaaa", "zzzz", this.cal));
		this.team.addMember((Competitor) new Person("Lqerda", "Diali", this.cal));
		this.team.addMember((Competitor) new Person("Toto", "Tata", this.cal));
		this.team.addMember((Competitor) new Person("Nono", "Nini", this.cal));

	}

	@Test
	public void testEquals() throws BadParametersException, ClassNotFoundException, ExistingCompetitorException, SQLException {
		Team team1 = new Team("Team-Test 2015");
		assertTrue(this.team.equals(team1));
		Team team2 = new Team("TeamNoTest");
		assertFalse(this.team.equals(team2));

	}


	@Test (expected = BadParametersException.class)
	public void testInvalidNameNoCapitalLetter() throws BadParametersException{
		Team team1 = new Team("first Letter Not Capital");
	}

	@Test (expected = BadParametersException.class)
	public void testInvalidNameLessThan3() throws BadParametersException, ClassNotFoundException, SQLException{
		Team team1 = new Team("Ab");
	}

	@Test (expected = BadParametersException.class)
	public void testInvalidNameInvalidCharacters() throws BadParametersException, ClassNotFoundException, SQLException{
		Team team1 = new Team("Abc @");
	}

	@Test (expected = ExistingCompetitorException.class)
	public void testAddingAnExistingMember() throws ClassNotFoundException, ExistingCompetitorException, BadParametersException, SQLException{
		this.team.addMember((Competitor) new Person("Nono", "Nini", this.cal));
	}

}
