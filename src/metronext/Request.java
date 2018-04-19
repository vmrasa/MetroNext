package metronext;

/**

 * 
 * @author Victoria Rasavanh
 *
 */
public class Request {

	public static final int NORTH = 4;
	public static final int SOUTH = 1;
	public static final int EAST = 2;
	public static final int WEST = 3;

	private static String route;
	private static int direction;
	private static String stop;
	private static JsonHasher hasher;

	public Request(String route, String stop, String direction) {
		Request.route = route;
		Request.direction = convertDirection(direction);
		Request.stop = stop;
		Request.hasher = new JsonHasher();
	}
	
	/**
	 * Takes the input text direction and converts it into an ID number.
	 * 
	 * @return North - 4 South - 1 East - 2 West - 3 Invalid - 0;
	 */
	private static int convertDirection(String direction) {
		int dNum = 0;
		switch (direction.toLowerCase()) {
		case "north":
			dNum = NORTH;
			break;
		case "south":
			dNum = SOUTH;
			break;
		case "east":
			dNum = EAST;
			break;
		case "west":
			dNum = WEST;
			break;
		}
		return dNum;
	}
	
	/**
	 * Checks the request for errors by checking the input
	 * for route and direction. Note that the stop will automatically
	 * be incorrect given an invalid route or direction, since it
	 * requires both to be hashed.
	 * @return
	 */
	private static String getRequestErrors() {
		String errors = "";
		boolean isValidRoute = hasher.hasRoute(route);
		
		if (isValidRoute == false) {
			errors += "-Route specified is invalid or unavailable for today.\n";
		} 
		if (direction == 0) {
			errors += "-Direction specified is invalid.";
		}

		return errors;
	}
	public String getNextDeparture () {
		Boolean hasNoErrors = getRequestErrors().isEmpty();
		//System.out.println("Route: " + route + "; Stop: " + stop + "; Direction: " + direction);
		if (hasNoErrors == false) {
			String errors = "The request is invalid for the following reasons:\n";
			return errors += getRequestErrors();
		}
		
		return hasher.getNextDeparture(route, direction, stop);
	}

}
