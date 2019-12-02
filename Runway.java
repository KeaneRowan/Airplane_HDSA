import airport.ListArrayBasedPlus;
import airport.Plane;

/**
 * A runway class to store information regarding the name of the runway itself and the planes within it
 */
public class Runway {

	 /**
	  * The list of taken names of runways among all that exist
	  */
	private static ListArrayBasedPlus<String> takenNames = new ListArrayBasedPlus<>();
	
	/**
	 * The name of the runway
	 */
	private String runwayName;
	
	/**
	 * The list of all ready flights on the runway
	 */
	private ListArrayBasedPlus<Plane> readyFlights = new ListArrayBasedPlus<>();
	
	/**
	 * The number of the runway
	 */
	private int runwayNumber;
	
	/**
	 * Constructs a newly allocated Runway object that represents the specified runway name and number.
	 * @param runwayName The name of the runway represented by a String
	 * @param runwayNumber The runway number
	 */
	public Runway(String runwayName, int runwayNumber) {
		this.runwayName = runwayName;
		this.runwayNumber = runwayNumber;
	}

	/**
	 * Returns the runway number of this runway
	 * @return the int runway number
	 */
	public int getRunwayNumber() {
		return runwayNumber;
	}

	/**
	 * Sets runway number to the int specified
	 * @param runwayNumber The new runway number to be assinged to this runway
	 */
	public void setRunwayNumber(int runwayNumber) {
		this.runwayNumber = runwayNumber;
	}

	/**
	 * Gets the String representation of the name of the Runway
	 * @return the String containing the name of the Runway
	 */
	public String getRunwayName() {
		return runwayName;
	}

	/**
	 * Returns the list of ready flights on the Runway
	 * @return ListArrayBasedPlus of ready flights
	 */
	public ListArrayBasedPlus<Plane> getReadyFlights() {
		return readyFlights;
	}

	/**
	 * Adds a new flight to the list of ready ones
	 * @param readyPlane The new Plane object to be added to the runway
	 */
	public void addReadyFlights(Plane readyPlane) {
		readyFlights.add(readyFlights.size(), readyPlane);
	}

	/**
	 * Returns the plane at the beginning of the list of ready flights for take off
	 * @return the first plane in the List of ready planes
	 */
	public Plane takeOff()
	{
		Plane result = null;
		if(readyFlights.size() !=0) {
			result = readyFlights.get(0);
			readyFlights.remove(0);
		}
		return result;
	}
	
	/**
	 * Sets the runway name to the new String specified
	 * @param runwayName String containing the new runway name
	 */
	public void setRunwayName(String runwayName) {
		this.runwayName = runwayName;
	}

	/**
	 * Adds a new flight to the list of ready ones
	 * @param readyPlane The new Plane object to be added to the runway
	 */
	public void addReadyFlight(Plane p)
	{
		readyFlights.add(readyFlights.size(), p);
	}

}

