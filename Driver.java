import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Driver {

	private static int numTakeoffs = 0;
	private static ListArrayBasedPlus<Runway> takeOffOrderedRunways = new ListArrayBasedPlus<>();
	private static ListArrayBasedPlus<Runway> nameOrderedRunways = new ListArrayBasedPlus<>();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static ListArrayBased<Runway> waitingRunways = new ListArrayBasedPlus<>();
	private static ListArrayBased<Plane> waitingPlanes = new ListArrayBasedPlus<>();
	private static int nextTakeoffRunway = 0;
	public static void main(String[] args) throws NumberFormatException, IOException 
	{
		System.out.println("Enter number of runways: ");
		int numRunways = Integer.parseInt(br.readLine());
		for(int i = 1; i <= numRunways; i++)
		{
			System.out.println("Enter the name of runway number: " + i + ":");
			String name = br.readLine();
			int size = takeOffOrderedRunways.size();
			Runway runwayToAdd = new Runway(name, size);
			try {
				insertBinarily(runwayToAdd);
				takeOffOrderedRunways.add(size, runwayToAdd);
			} catch (AlreadyExistsException e) {
				e.getMessage();
			}
		}
		System.out.println("Select from the following menu:\r\n" + 
				"  0. Exit program.\r\n" + 
				"  1. Plane enters the system.\r\n" + 
				"  2. Plane takes off.\r\n" + 
				"  3. Plane is allowed to re-enter a runway.\r\n" + 
				"  4. Runway opens.\r\n" + 
				"  5. Runway closes.\r\n" + 
				"  6. Display info about planes waiting to take off.\r\n" + 
				"  7. Display info about planes waiting to be allowed to re-enter a runway.\r\n" + 
				"  8. Display number of planes who have taken off.");
		boolean isDone = false;
		while(!isDone)
		{
			System.out.println("Make your menu selection now: ");
			int choice = Integer.parseInt(br.readLine());
			
			switch(choice)
			{
			case 0: 
				isDone = true;
				break;
			case 1: 
				try
				{
					System.out.println("Enter flight number:");
					String fn = br.readLine();
					System.out.println("Enter destination:");
					String d = br.readLine();
					boolean choseRunway = false;
					Runway myRunway = null;
					while(!choseRunway)
					{
						System.out.println("Enter runway:");
						Runway myR = findBinarily(br.readLine());
						if(myR == null) System.out.println("No such runway!");
						else
						{
							choseRunway = false;
							myRunway = myR;
						}
					}
					Plane myP = new Plane(fn, d);
					myRunway.addReadyFlight(myP);
					System.out.println("Flight " + myP.getFlightNumber() + " ready for takeoff on runway " + myRunway.getRunwayName());
				}catch(AlreadyExistsException a)
				{
					System.out.println("Flight number already in use.");
				}
				break;
			case 2: 
				boolean doneSearching = false;
				int size = takeOffOrderedRunways.size();
				int i = nextTakeoffRunway;
				int j = 0;
				while(!doneSearching && j < size)
				{
					Runway currRunway = takeOffOrderedRunways.get(i%size);
					Plane takeOffPlane = currRunway.takeOff();
					if( takeOffPlane != null)
					{
						System.out.println("Is flight " + takeOffPlane.getFlightNumber() + " cleared for takeoff(Y/N): ");
						if(br.readLine().toLowerCase().trim().compareTo("y") == 0)
						{
							System.out.println("Flight " + takeOffPlane.getFlightNumber() + " has now taken off on runway " + currRunway.getRunwayName());
							nextTakeoffRunway = ++i;
							numTakeoffs++;
						}
						else
						{
							System.out.println("Flight " + takeOffPlane.getFlightNumber() + " is now waiting to be allowed to re-enter a runway");
							nextTakeoffRunway = ++i;
							insertBinarily(takeOffPlane, currRunway);
						}
					}
					else
					{
						i++;
						j++;
						nextTakeoffRunway = i;
					}
				}
				break;
			case 3: 
				if(waitingPlanes.size() == 0) System.out.println("There are no planes waiting for clearance!");
				else
				{
					System.out.println("Enter flight number");
					String flightNum = br.readLine();
					Object[] o = findPlaneBinarily(flightNum);
					Plane p = (Plane)o[0]; //method
					Runway r = (Runway)o[1];
					if(p != null)
					{
						System.out.println("Flight " + p.getFlightNumber() + " is now wating for takeoff on runway " +  r.getRunwayName());
						Runway newRunway= takeOffOrderedRunways.get(findBinarily(r.getRunwayName()).getRunwayNumber());
						newRunway.addReadyFlight(p);
					}
				}
				break;
			case 4: 
				boolean found = false;
				while(!found)
				{
					System.out.println("Enter the name of the new runway: ");
					String input = br.readLine();
					Runway foundRunway = findBinarily(input);
					if(foundRunway != null)
					{
						System.out.println("Runway " + input + " already exists, please choose another name.");
					}
					else
					{
						System.out.println("Runway " + input + " has opened.");
						Runway runwayToAdd = new Runway(input, takeOffOrderedRunways.size());
						try {
							insertBinarily(runwayToAdd);
						} catch (AlreadyExistsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						takeOffOrderedRunways.add(takeOffOrderedRunways.size(), runwayToAdd);
					}
				}
				break;
			case 5: 
				boolean foundClosedRunway = false;
				String foundRunway = "";
				Runway foundRunwayObject = null;
				while(!foundClosedRunway)
				{
					System.out.println("Enter runway: ");
					foundRunway = br.readLine();
					foundRunwayObject = findBinarily(foundRunway);
					if(foundRunwayObject == null) System.out.println("No such runway!");
					else
					{
						foundRunwayObject = takeOffOrderedRunways.get(foundRunwayObject.getRunwayNumber());
						int it = 0;
						ListArrayBased<Plane> allReadyFlights = foundRunwayObject.getReadyFlights();
						int readysize = allReadyFlights.size();
						while(it < readysize)
						{
							System.out.println("Enter runway: ");
							String runway = br.readLine();
							if(runway.compareTo(foundRunway) == 0) System.out.println("This is the runway that is closing!");
							else
							{
								Runway runObj = findBinarily(runway);
								if(runObj == null) System.out.println("No such runway!");
								else
								{
									takeOffOrderedRunways.get(runObj.getRunwayNumber()).addReadyFlight(allReadyFlights.get(it));
									it++;
								}
							}
						}
						int index = findIndexBinarily(foundRunway);
						if(index >= nameOrderedRunways.size())
						{
							//Do nothing
						}
						else
						{
							int takeOffIndex = nameOrderedRunways.get(index).getRunwayNumber();
							Runway nextRunway = takeOffOrderedRunways.get( takeOffIndex + 1);
							int newRunwayNumber = nextRunway.getRunwayNumber() - 1;
							nextRunway.setRunwayNumber(newRunwayNumber);
							int nIndex = findIndexBinarily(nextRunway.getRunwayName());
							nameOrderedRunways.get(nIndex).setRunwayNumber(newRunwayNumber);
							takeOffOrderedRunways.remove(takeOffIndex);
							nameOrderedRunways.remove(index);
							
						}
					}
				}
				break;
			case 6: 
				int iterator = nextTakeoffRunway;
				int sizeTracker = 0;
				int takeoffSize = takeOffOrderedRunways.size();
				while(sizeTracker < takeoffSize)
				{
					Runway currentRunway = takeOffOrderedRunways.get(iterator % takeoffSize);
					ListArrayBasedPlus<Plane> readyFlights = currentRunway.getReadyFlights();
					if(readyFlights.size() == 0) System.out.println("No planes are waiting for takeoff on runway " + currentRunway.getRunwayName());
					else
					{
						System.out.println("These planes are waiting on runway " + currentRunway.getRunwayName());
					}
						int flightsSize = readyFlights.size();
						for(int t = 0; t < flightsSize; t ++)
						{
							Plane currFlight = readyFlights.get(t);
							System.out.println("Flight " + currFlight.getFlightNumber() + " to " + currFlight.getDestination());
						}
				}
				break;
			case 7: 
				int waitingSize = waitingPlanes.size();
				if(waitingSize == 0) System.out.println("No planes are waiting to be cleared to re-enter a runway!");
				else
				{
					System.out.println("These planes are waiting to be cleared to re-enter a runway:");
					for(int z = 0; z < waitingSize; z++)
					{
						Plane currFlight = waitingPlanes.get(z);
						System.out.println("Flight " + currFlight.getFlightNumber() + currFlight.getDestination());
					}
				}
				break;
			case 8: 
				System.out.println(numTakeoffs + " planes have taken off from the airport.");
			}
		}
		
	}
	
	private static void insertBinarily(Runway r) throws AlreadyExistsException
	{
		int min = 0;
		int max = nameOrderedRunways.size(); 
		int mid = 0;
		while(max - min != 0)
		{
			mid = (min + max)/2;
			if(r.getRunwayName().compareTo(nameOrderedRunways.get(mid).getRunwayName()) <= 0)
			{
				max = mid;
			}
			else
			{
				min = mid + 1;
			}
		}
		int end = r.getRunwayName().compareTo(nameOrderedRunways.get(mid).getRunwayName());
		if(end == 0) throw new AlreadyExistsException("Exception on adding into the runway.");
		else
		{
			nameOrderedRunways.add(mid, r);
		}
	}
	
	private static Runway findBinarily(String str)
	{
		Runway returnRunway = null;
		int min = 0;
		int max = nameOrderedRunways.size(); 
		int mid = 0;
		while(max - min != 0)
		{
			mid = (min + max)/2;
			if(str.compareTo(nameOrderedRunways.get(mid).getRunwayName()) <= 0)
			{
				max = mid;
			}
			else
			{
				min = mid + 1;
			}
		}
		int end = str.compareTo(nameOrderedRunways.get(mid).getRunwayName());
		if(end == 0) returnRunway = nameOrderedRunways.get(mid);
		return returnRunway;
	}
	
	private static int findIndexBinarily(String str)
	{
		Runway returnRunway = null;
		int min = 0;
		int max = nameOrderedRunways.size(); 
		int mid = 0;
		while(max - min != 0)
		{
			mid = (min + max)/2;
			if(str.compareTo(nameOrderedRunways.get(mid).getRunwayName()) <= 0)
			{
				max = mid;
			}
			else
			{
				min = mid + 1;
			}
		}
		int end = str.compareTo(nameOrderedRunways.get(mid).getRunwayName());
		return end;
	}
	
	private static Object[] findPlaneBinarily(String flightNum)
	{
		Object[] returnArr = new Object[2];
		int min = 0;
		int max = waitingPlanes.size(); 
		int mid = 0;
		while(max - min != 0)
		{
			mid = (min + max)/2;
			if(flightNum.compareTo(waitingPlanes.get(mid).getFlightNumber()) <= 0)
			{
				max = mid;
			}
			else
			{
				min = mid + 1;
			}
		}
		int end = flightNum.compareTo(waitingPlanes.get(mid).getFlightNumber());
		if(end == 0) 
		{
			returnArr[0] = waitingPlanes.get(mid);
			returnArr[1] = waitingRunways.get(mid);
			waitingRunways.remove(mid);
			waitingPlanes.remove(mid);
		}
		return returnArr;
	}
	
	private static void insertBinarily(Plane p, Runway r) 
	{
		int min = 0;
		int max = nameOrderedRunways.size(); 
		int mid = 0;
		while(max - min != 0)
		{
			mid = (min + max)/2;
			if(p.getFlightNumber().compareTo(waitingPlanes.get(mid).getFlightNumber()) <= 0)
			{
				max = mid;
			}
			else
			{
				min = mid + 1;
			}
		}
		waitingPlanes.add(mid, p);
		waitingRunways.add(mid, r);
	}
	
	

}
