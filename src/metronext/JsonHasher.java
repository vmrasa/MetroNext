package metronext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is tasked with retrieving, parsing, and
 * outputting JSON to be used by Requests.
 * 
 * URLs needed:
 * - For getting today's route #'s - http://svc.metrotransit.org/NexTrip/Routes?format=json
 * - For getting the stop ID's - http://svc.metrotransit.org/NexTrip/Stops/{int ROUTE}/{int DIRECTION}
 * - For getting departure times - http://svc.metrotransit.org/NexTrip/{int ROUTE}/{int DIRECTION}/{STOP}
 * @author Victoria Rasavanh
 *
 */
public class JsonHasher {
	
	private static Hashtable<String, Integer> routeHash; //route names & numbers for the entire day
	
	/**
	 * Constructor - simply initializes the route hash.
	 */
	public JsonHasher (){
		JsonHasher.routeHash = setRouteHash();
	}
	
	/**
	 * Returns true if the Hasher contains a key corresponding
	 * to the given route.
	 * @param route - the route key being searched for
	 * @return
	 */
	public boolean hasRoute(String route) {
		return routeHash.containsKey(route);
	}
	
	/**
	 * Handles the validation and creation of a URL. 
	 * Returns a null if the url cannot be created.
	 * @param urlToCheck
	 * @return
	 */
	private static URL createUrl(String urlToCheck) {
		URL url;
		try {
			url = new URL(urlToCheck);
			return url;
		} catch (MalformedURLException e) {
			System.out.print("Error creating URL.");
		}
		return null;
	}
	
	/**
	 * Retrieves and returns JSON in the form of a string. 
	 * If the JSON cannot be retrieved, it returns null.
	 * @param url - the URL to fetch the JSON from.
	 * @return
	 */
	private static StringBuffer getJsonString(URL url) {
		HttpURLConnection con;
		BufferedReader in;
		StringBuffer response = new StringBuffer();
		try {
			con = (HttpURLConnection) url.openConnection();
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (IOException e) {
			System.out.println("Failed to get JSON string.");
			response = null;
		}
		
		return response;
	}
	
	/**
	 * Creates and sets the value for private variable routeHash.
	 * The hash will contain route names paired with the route number.
	 * @return
	 */
	private static Hashtable<String, Integer> setRouteHash() {
		URL url = createUrl("http://svc.metrotransit.org/NexTrip/Routes?format=json");
		Hashtable<String, Integer> table = new Hashtable<String, Integer>();
		
		JSONArray array;
		try {
			array = new JSONArray(getJsonString(url).toString());
			for (int i = 0; i < array.length(); i++) {
				JSONObject temp = array.getJSONObject(i);
				table.put(temp.getString("Description"), temp.getInt("Route"));		
			}
		} catch (JSONException e) {
			System.out.println("Unable to retrieve route JSON.");
			e.printStackTrace();
		}
		return table;
	}
	
	/**
	 * Retrieves a HashMap consisting of all the stops on this route.
	 * Requires the HashMap routeHash to be non-empty.
	 * @param route
	 * @param direction
	 * @return
	 */
	private static HashMap<String, String> setStopsHash(int route, int direction) {
		URL url = createUrl("http://svc.metrotransit.org/NexTrip/Stops/" + route +"/" + direction + "?format=json");
		HashMap<String, String> map = new HashMap<String, String>();
		
		JSONArray array;
		try {
			array = new JSONArray(getJsonString(url).toString());
			for (int i = 0; i < array.length(); i++) {
				JSONObject temp = array.getJSONObject(i);
				map.put(temp.getString("Text"), temp.getString("Value"));		
			}
		} catch (JSONException e) {
			System.out.println("Unable to create stops JSON.");
			//e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * Retrieves the next departure time using the given route, direction and stop.
	 * @param route - the name of a route as a string
	 * @param direction - the direction of the route as an int
	 * @param stop - the stop of the route as a string
	 * @return Returns the next departure time as a string, or "Departure unavailable".
	 */
	public String getNextDeparture(String route, int direction, String stop) {
		
		int routeNum = routeHash.get(route);
		
		HashMap<String, String> stopsHash = setStopsHash(routeHash.get(route), direction);
		String stopId = stopsHash.get(stop);
		
		URL url = createUrl("http://svc.metrotransit.org/NexTrip/" + routeNum +"/" + direction + "/" + stopId + "?format=json");
		String departTime = "";
		JSONArray array;
		
		// Retrieve the departure time
		try {
			array = new JSONArray(getJsonString(url).toString());
			
			
			if (array.length() == 0) {
				departTime = "The stop is invalid or is not associated with the given route/direction.";
			} else {
				// The next departure time will always be at the top
				JSONObject response = array.getJSONObject(0);
				if (response.getBoolean("Actual") != false) {
					departTime = "Next bus in " + response.get("DepartureText").toString();
				} else {
					departTime = "Next bus will depart at " + response.get("DepartureText");
				}
			} 
		} catch (JSONException e) {
			System.out.println("Error: unable to create JSONArray for departure times.");
		}
		
		return departTime;
	}
	
	
	
}
