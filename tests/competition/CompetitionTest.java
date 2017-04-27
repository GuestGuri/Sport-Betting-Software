package competition;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Calendar;
import java.sql.Date;
import org.junit.Test;

import fr.uv1.bettingServices.Competitor;
import fr.uv1.bettingServices.competition.Competition;
import fr.uv1.bettingServices.competitor.Person;
import fr.uv1.bettingServices.exceptions.AuthenticationException;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitionException;
import fr.uv1.bettingServices.exceptions.ExistingCompetitorException;

public class CompetitionTest {

	@Test (expected = BadParametersException.class)
	public void testCompetitionBadName1() throws BadParametersException, CompetitionException {
		Competition competition1 = new Competition("+alfaBeta", new Date(2018, 10, 10));	
	}

	@Test (expected = BadParametersException.class)
	public void testCompetitionBadName2() throws BadParametersException, CompetitionException {
		Competition competition2 = new Competition("al", new Date(2018, 10,10));	
	}
	
	@SuppressWarnings("deprecation")
	@Test 
	public void testCompetitionBadName3() throws BadParametersException, CompetitionException {
		Competition competition3 = new Competition("Alutiy", new Date(2018, 01, 02));	
	}

	@SuppressWarnings("deprecation")
	@Test (expected = CompetitionException.class)
	public void testCompetitionBadName4() throws BadParametersException, CompetitionException {
		Competition competition4 = new Competition("Alutiy", new Date(99, 01, 02));	
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testAddCompetitor() throws BadParametersException, CompetitionException, ExistingCompetitorException, AuthenticationException, ExistingCompetitionException, SQLException {
		Calendar cal=Calendar.getInstance();
		cal.set(1995, Calendar.DECEMBER,24);
		Competitor c = new Person("Alain", "Roger", new Date(cal.getTimeInMillis()));
		Competition competition4 = new Competition("Alutiy", new Date(2018, 10, 10));	
        competition4.addCompetitor(c);
	}

	@Test (expected = ExistingCompetitorException.class)
	public void testAddExistingCompetitor() throws BadParametersException, CompetitionException, ExistingCompetitorException, AuthenticationException, ExistingCompetitionException, SQLException {
		Calendar cal=Calendar.getInstance();
		cal.set(1995, Calendar.DECEMBER,24);
		Competitor c = new Person("Alain", "Roger", new Date(cal.getTimeInMillis()));
		Competition competition4 = new Competition("Alutiy", new Date(2018, 10, 10));	
        competition4.addCompetitor(c);
        competition4.addCompetitor(c);
	}
	
	@Test
	public void testDeleteCompetitor() throws BadParametersException, CompetitionException, ExistingCompetitorException, AuthenticationException, ExistingCompetitionException, SQLException {
		Calendar cal=Calendar.getInstance();
		cal.set(1995, Calendar.DECEMBER,24);
		Competitor c = new Person("Alain", "Roger", new Date(cal.getTimeInMillis()));
		Competition competition4 = new Competition("Alutiy", new Date(2018, 10, 10));	
        competition4.addCompetitor(c);
        competition4.deleteCompetitor(c);
	}

	@Test
	public void testFindCompetitor() throws BadParametersException, CompetitionException, ExistingCompetitorException, AuthenticationException, ExistingCompetitionException, SQLException {
		Calendar cal=Calendar.getInstance();
		cal.set(1995, Calendar.DECEMBER,24);
		Competitor c = new Person("Alain", "Roger", new Date(cal.getTimeInMillis()));
		Competition competition4 = new Competition("Alutiy", new Date(2018, 10, 10));	
        competition4.addCompetitor(c);
        competition4.findCompetitor("Alain");
	}

	@Test
	public void testIsClosed() throws BadParametersException, CompetitionException {
		Competition competition4 = new Competition("Alutiy", new Date(2018, 10, 10));	
		boolean b = competition4.isClosed();
	}

}
