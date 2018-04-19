package metronext;

public class Main {

	/**
	 * Program determines how long a certain route will be at a particular stop
	 * for a particular direction
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 0) {
			System.out.println("I don't know how you've managed to do this.");
		}
		else if (args.length >= 0 && args.length < 3) {
			System.out.println("Too few arguments.");
		} 
		else if (args.length > 3) {
			System.out.println("Too many arguments.");
		} 
		else {
			Request r = new Request(args[0], args[1], args[2]);
			System.out.print(r.getNextDeparture());
		}
	}

}
