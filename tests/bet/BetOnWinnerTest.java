package bet;

import fr.uv1.bettingServices.Competitor;
import fr.uv1.bettingServices.bet.BetOnWinner;
import fr.uv1.bettingServices.competition.Competition;
import fr.uv1.bettingServices.competitor.Team;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import fr.uv1.bettingServices.exceptions.CompetitionException;
import fr.uv1.bettingServices.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 *
 *
 */
//import
//import static
public class BetOnWinnerTest {

	private long tokens;
	private Player better1;
	private Competition competition;
	private Integer id;
	Competitor A;
	Competitor B;
	Calendar closingDate;
	BetOnWinner bet;

	@Before
	public void beforeTests() throws Exception {

		ArrayList<Competitor> competitors = new ArrayList<Competitor>();
		A = new Team("CompetitorA");
		B = new Team("CompetitorB");
		competitors.add(A);
		competitors.add(B);
		id = 100;
		tokens = 50;
        closingDate = Calendar.getInstance();
        closingDate.set(2017, Calendar.DECEMBER, 10);

        Calendar bornDate = Calendar.getInstance();
        bornDate.set(1994, Calendar.JANUARY, 11);
		better1 = new Player("balach", "malach", "talach", new Date(bornDate.getTimeInMillis()), 80);
		competition = new Competition("COMPET", new Date(closingDate.getTimeInMillis()), competitors);
		competition.setCompetitors(competitors);

		bet = new BetOnWinner(tokens,better1,competition,A);
	}

	@Test
	public void testOKBet() throws BadParametersException, CompetitionException {
		new BetOnWinner(70, better1, competition, A);
	}

	@Test(expected = BadParametersException.class)
	public void testMoneyLessZeroBet() throws Exception {
		new BetOnWinner(-12, better1, competition, A);
	}





	@Test(expected = CompetitionException.class)

	public void testCompetitionClosedBet() throws Exception {

		Competition comp=new Competition("Comp", new Date(2006,5,6));
		new BetOnWinner(tokens,better1,comp ,A);
	}

	@Test
	public void testEqualsObject() throws Exception {
		BetOnWinner bet2 = new BetOnWinner(1);
		BetOnWinner bet3 = new BetOnWinner(1);
		assertTrue(bet3.equals(bet2));

		bet2 = new BetOnWinner (2);
		assertFalse(bet3.equals(bet2));
	}


}
