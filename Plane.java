import airport.AlreadyExistsException;
import airport.ListArrayBasedPlus;

/**
 * A Plane class to store information regarding the number of the plane and its destination
 */
public class Plane {
	/**
	 * The list of taken numbers shared among all instances of the Plane class
	 */
	private static ListArrayBasedPlus<String> takenNumbers = new ListArrayBasedPlus<>();
	
	/**
	 * The flight number of the plane
	 */
	private String flightNumber; 
	
	/**
	 * The destination of the plane
	 */
	private String destination;
	
	/**
	 * Constructs a newly allocated Plane object that represents the specified plane number and destination.
	 * @param flightNumber the String flight number of the plane
	 * @param destination the String destiantion of the plane
	 * @throws AlreadyExistsException
	 */
	public Plane(String flightNumber, String destination) throws AlreadyExistsException{
		
		for(int i = 0; i < takenNumbers.size(); i++)
		{
			if(flightNumber.compareTo(takenNumbers.get(i)) == 0)
			{
				throw new AlreadyExistsException();
			}
		}
		takenNumbers.add(takenNumbers.size(), flightNumber);
		this.flightNumber = flightNumber;
		this.destination = destination;
	}
	
	/**
	 * Gets the String representation of the flight number of the plane
	 * @return the String representation of the flight number
	 */
	public String getFlightNumber() {
		return flightNumber;
	}
	
	/**
	 * Changes the flight number of the plane to the String specified
	 * @param flightNumber the new flight number of plane
	 */
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	
	/**
	 * Gets the String reperesentation of the destination of the plane
	 * @return the String representation of destination of the plane
	 */
	public String getDestination() {
		return destination;
	}
	
	/**
	 * Changes the destination of the plane to the String specified
	 * @param destination the new destination of the plane
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

}
