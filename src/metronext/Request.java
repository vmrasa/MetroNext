package metronext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
		this.route = route;
		this.direction = convertDirection(direction);
		this.stop = stop;
		this.hasher = new JsonHasher();
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
	
	private static String getRequestErrors() {
		String errors = "";
		
		if (hasher.hasRoute(route) == false) {
			errors += "-Route specified is unavailable.\n";
		} 
		if (direction == 0) {
			errors += "-Direction specified is invalid. \n";
		}
		if (hasher.hasStop(route, stop, direction) == false) {
			errors += "-Stop specified is invalid.\n";
		}

		return errors;
	}
	public static String getNextDeparture () {
		Boolean hasNoErrors = getRequestErrors().isEmpty();
		if (hasNoErrors == false) {
			System.out.println("The request is invalid for the following reasons:");
			return getRequestErrors();
		}
		
		return hasher.getNextDeparture(route, direction, stop);
	}

}
