package testPackage;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import metronext.Request;

/**
 * Test cases are based on Request parameter inputs.
 * These include the route, stop, and direction.
 * 
 * @author Victoria Rasavanh
 *
 */
@RunWith(Parameterized.class)
public class RouteErrorTest {
	
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
		
	//private Request request;
	//private boolean expected;
	
	/**
	public RouteErrorTest(Request input, boolean expectedResults) {
		this.request = input;
		this.expected = expectedResults;
	} **/
	
    @Parameter(value = 0)
    public String route;

    @Parameter(value = 1)
    public String stop;

    @Parameter(value = 2)
    public String direction;
    
    @Parameter(value = 3)
    public boolean expectedResult;
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
			{VALIDROUTES[0], VALIDSTOPS[0] , "North", false},
			{VALIDROUTES[1], VALIDSTOPS[1] , "", false},
			{VALIDROUTES[2], "\n" , "EAST", false},
			{VALIDROUTES[3], "I'm not even trying" , "/southeast", false},
			{null, VALIDSTOPS[1] , "south", true},
			{":::", VALIDSTOPS[1] , "0", true},
			{"-1345", VALIDSTOPS[2] , "North", true},
			{"w/o", "asdfoijafnlwaenl" , "1", true}
		});
	}
	
	
	@Test
	public void containsRouteError() {
		Request r = new Request(route, stop, direction);
		String errMsg = r.getNextDeparture();
		System.out.println(errMsg);
		assertEquals(expectedResult, (errMsg.contains(RQERR) && errMsg.contains(RTERR)));
	}
	

}
