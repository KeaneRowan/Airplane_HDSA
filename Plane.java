
public class Plane {
	private static ListArrayBased<String> takenNumbers = new ListArrayBased<>();
	private String flightNumber; 
	private String destination;
	
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
	
	public String getFlightNumber() {
		return flightNumber;
	}
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}

}
