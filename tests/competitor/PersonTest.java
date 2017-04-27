package competitor;

import fr.uv1.bettingServices.competitor.Person;
import fr.uv1.bettingServices.exceptions.BadParametersException;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.text.ParseException;
import java.util.Calendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 *
 *
 */
public class PersonTest {
	private Person competitor;
	private Calendar cal = Calendar.getInstance();


	@Before
	public void beforeTests() throws ParseException, BadParametersException{
		this.cal.set(1995, Calendar.DECEMBER,24);
		this.competitor=new Person("aaaa","zzzz", new Date(cal.getTimeInMillis()));
	}

	@Test
	public void testEqualsObjects() throws BadParametersException {
		//same
		this.cal.set(1995,Calendar.DECEMBER, 24);
		Person competitor1=new Person("aaaa", "zzzz", new Date(this.cal.getTimeInMillis()));
		assertTrue(this.competitor.equals(competitor1));
		// diferent born date
		this.cal.set(1994, Calendar.DECEMBER, 24);
		Person competitor2=new Person("aaaa","zzzz",new Date(this.cal.getTimeInMillis()));
		assertFalse(this.competitor.equals(competitor2));
		//diferent first name
		Person competitor3 = new Person("Lqred","zzzz",new Date(this.cal.getTimeInMillis()));
		assertFalse(this.competitor.equals(competitor3));
		//diferent last name
		Person competitor4 = new Person("aaaa","Diali",new Date(this.cal.getTimeInMillis()));
		assertFalse(this.competitor.equals(competitor4));
	}

	@Test (expected = BadParametersException.class)
	public void testConstructorBadFirstName() throws BadParametersException {
		Person competitor1 = new Person("Lqred4","Diali",new Date(cal.getTimeInMillis()));
	}

	@Test (expected = BadParametersException.class)
	public void testConstructorBadLastName() throws BadParametersException {
		Person competitor1 = new Person("Lqred","Diali1",new Date(this.cal.getTimeInMillis()));
	}
	@Test (expected = BadParametersException.class)
	public void testConstructorBadBornDate() throws BadParametersException {
		Person competitor1 = new Person("Lqred","Diali",new Date(2019,1,2));
	}



}
