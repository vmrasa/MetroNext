package testPackage;

import java.util.Arrays;
import java.util.Collection;
 
import org.junit.Test;
import org.junit.Before;

import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import metronext.JsonHasher;

import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;


/**
 * 
 * @author Victoria Rasavanh
 *
 */
@RunWith(Parameterized.class)
public class JsonRouteTest {

	private JsonHasher jsonHasher;
	private String input;
	private boolean expectedResult;
	
	@Before
	public void initialize() {
		jsonHasher = new JsonHasher();
	}
	
	public JsonRouteTest(String input, boolean expected) {
		this.input = input;
		this.expectedResult = expected;
	}
	
	/**
	 * Parameters indicated as true are known 24 hour routes
	 * according to https://www.metrotransit.org/24-7-service-on-more-routes
	 * @return
	 */
	@Parameterized.Parameters
	public static Collection inputRequests() {
		return Arrays.asList(new Object[][] {
			{ "METRO Blue Line", true },
			{ "METRO Green Line", true },
			{ "10 - Central Av - University Av - Northtown", true },
			{ "18 - Nicollet Av - South Bloomington", true},
			{ "3 - U of M - Como Av - Energy Park Dr - Maryland Av", true},
			{ "5 - Brklyn Center - Fremont - 26th Av - Chicago - MOA", true },
			{ "54 - Ltd Stop - W 7St - Airport - MOA", true },
			{ "I am a ruse.", false},
			{ "10", false},
			{ "1234", false },
			{ "12.34", false },
		});
	}
	
	@Test
	public void hasRouteTest() {
		 assertEquals(expectedResult, jsonHasher.hasRoute(input));
	}

}
