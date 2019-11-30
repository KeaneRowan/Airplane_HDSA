
public class Runway {

	private static ListArrayBasedPlus<String> takenNames = new ListArrayBasedPlus<>();
	private String runwayName;
	private ListArrayBasedPlus<Plane> readyFlights = new ListArrayBasedPlus<>();
	private int runwayNumber;
	public Runway(String runwayName, int runwayNumber) {
		this.runwayName = runwayName;
		this.runwayNumber = runwayNumber;
	}

	public int getRunwayNumber() {
		return runwayNumber;
	}

	public void setRunwayNumber(int runwayNumber) {
		this.runwayNumber = runwayNumber;
	}

	public String getRunwayName() {
		return runwayName;
	}

	public ListArrayBasedPlus<Plane> getReadyFlights() {
		return readyFlights;
	}

	public void addReadyFlights(Plane readyPlane) {
		readyFlights.add(readyFlights.size(), readyPlane);
	}
	
	public Plane takeOff()
	{
		return readyFlights.get(0);
	}
	public void setRunwayName(String runwayName) {
		this.runwayName = runwayName;
	}
	
	public void addReadyFlight(Plane p)
	{
		readyFlights.add(readyFlights.size(), p);
	}

}
