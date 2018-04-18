package testPackage;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import metronext.Request;

/**
 * Test cases are based on Request parameter inputs.
 * These include the route, stop, and direction.
 * 
 * @author Victoria Rasavanh
 *
 */
@RunWith(Parameterized.class)
public class RequestTest {
	
	/**
	 * The following routes are known 24/7 routes and their corresponding stops.
	 * https://www.metrotransit.org/24-7-service-on-more-routes
	 */
	private static final String VALIDROUTES[] = {
			"5 - Brklyn Center - Fremont - 26th Av - Chicago - MOA",
			"10 - Central Av - University Av - Northtown",
			"54 - Ltd Stop - W 7St - Airport - MOA",
			"METRO Green Line"
	};
	private static final String VALIDSTOPS[] = {
			"Mall of America Transit Station",
			"Northtown Transit Center",
			"MSP Airport  Terminal 1 - Lindbergh",
			"Union Depot"
	};
	
	private static final String RQERR = "The request is invalid for the following reasons:";
	private static final String RTERR = "-Route specified is invalid or unavailable for today.";
	private static final String DIRERR = "-Direction specified is invalid.";
	private static final String STOPERR = "The stop is invalid or is not associated with the given route/direction.";
	private static final String NXTBUSMSG = "Next bus";
		
	private Request request;
	private boolean expected;
	
	public RequestTest(Request input, boolean expectedResults) {
		this.request = input;
		this.expected = expectedResults;
	}

	/**
	 * Request input combinations are:
	 * - VVV - returns time
	 * - VVI - direction error
	 * - VIV - Stop Error
	 * - VII - Direction error
	 * - IVV - Route error
	 * - IVI - Route and direction error
	 * - IIV - Route and direction error
	 * - III - Route and direction error
	 * @return
	 */
	@Parameterized.Parameters
	public static Collection inputRequests() {
		return Arrays.asList(new Object[][] {
			{new Request(VALIDROUTES[0], VALIDSTOPS[0] , "North"), false},
			{new Request(VALIDROUTES[1], VALIDSTOPS[1] , ""), false},
			{new Request(VALIDROUTES[2], "\n" , "EAST"), false},
			{new Request(VALIDROUTES[3], "I'm not even trying" , "/southeast"), false},
			{new Request(null, VALIDSTOPS[1] , "south"), true},
			{new Request(":::", VALIDSTOPS[1] , "North"), true},
			{new Request("-1345", VALIDSTOPS[2] , "North"), true},
			{new Request("w/o", "asdfoijafnlwaenl" , "North"), true}
		});
	}
	
	
	@Test
	public void containsRouteError() {
		String errMsg = request.getNextDeparture();
		assertEquals(expected, errMsg.contains(RQERR) && errMsg.contains(RTERR));
	}
	

}
