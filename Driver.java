import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Driver {

	//Instance variables
	private static int numTakeoffs = 0;
	//Reduce search time complexity to log2n
	private static ListArrayBasedPlus<Runway> takeOffOrderedRunways = new ListArrayBasedPlus<>();
	private static ListArrayBasedPlus<Runway> nameOrderedRunways = new ListArrayBasedPlus<>();
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	//Reduce search times of waiting planes to log2n
	private static ListArrayBased<Runway> waitingRunways = new ListArrayBasedPlus<>();
	private static ListArrayBased<Plane> waitingPlanes = new ListArrayBasedPlus<>();
	//Keep track of the next runway to take off
	private static int nextTakeoffRunway = 0;
	//Keep track of planes taken off and landed
    private static ListArrayBasedPlus<Plane> takenOffPlanes = new ListArrayBasedPlus<>();
    private static StringBuilder sb = new StringBuilder();

	public static void main(String[] args) throws NumberFormatException, IOException 
	{
		System.out.println("Welcome to the Airport program!");

		//create the original runways
		getInitRunways();

		System.out.println("Select from the following menu:\r\n" +
                "  0.  Exit program.\r\n" +
                "  1.  Plane enters the system.\r\n" +
                "  2.  Plane takes off.\r\n" +
                "  3.  Plane is allowed to re-enter a runway.\r\n" +
                "  4.  Runway opens.\r\n" +
                "  5.  Runway closes.\r\n" +
                "  6.  Display info about planes waiting to take off.\r\n" +
                "  7.  Display info about planes waiting to be allowed to re-enter a runway.\r\n" +
                "  8.  Display number of planes who have taken off.\r\n" +
                "  9.  Land a plane.\r\n" +
                "  10. Display info about planes landed");
		boolean isDone = false;
		while(!isDone)
		{
			System.out.print("Make your menu selection now: ");
			int choice = Integer.parseInt(br.readLine());
			System.out.println(choice);
			switch(choice)
			{
			case 0: 
				isDone = true;
				System.out.println("The Airport is closing :Bye Bye....");
				break;
			case 1: 
				addFlight();
				break;
			case 2: 
				takeOff();
				break;
			case 3: 
				allowEntrance();
				break;
			case 4: 
				openNewRunway();
				break;
			case 5: 
				closeRunway();
				break;
			case 6: 
				displayReadyPlanes();
				break;
			case 7: 
				displayWaitingPlanes();
				break;
			case 8:
				System.out.println(numTakeoffs + " planes have taken off from the airport.");
				break;
			case 9:
				landPlane();
				break;
			case 10:
				displayLandedPlanes();
				break;
			}

			System.out.println();
		}

	}

	private static void getInitRunways() throws NumberFormatException, IOException {
		//Get the runways
		System.out.print("Enter number of runways: ");
		int numRunways = Integer.parseInt(br.readLine());
		System.out.println(numRunways);

		for(int i = 1; i <= numRunways; i++)
		{
			System.out.print("Enter the name of runway number " + i + ": ");
			String name = br.readLine();
			System.out.println(name);
			int size = takeOffOrderedRunways.size();
			Runway runwayToAdd = new Runway(name, size);
			try {
				//Insert a runway binarily to the list, add it to the end of the takeoff order
				insertBinarily(runwayToAdd);
				takeOffOrderedRunways.add(size, runwayToAdd);
			} catch (AlreadyExistsException e) {
				e.getMessage();
			}
		}
	}

	private static void addFlight() throws IOException {
		try
		{
			System.out.print("Enter flight number:");
			String fn = br.readLine();
			System.out.println(fn);

			System.out.print("Enter destination:");
			String d = br.readLine();
			System.out.println(d);
			boolean choseRunway = false;
			Runway myRunway = null;
			//While runway entered is not valid, loop an ask for input
			while(!choseRunway)
			{
				System.out.print("Enter runway:");
				//Binarily search for the runway
				String searchedRunway = br.readLine();
				System.out.println(searchedRunway);
				Runway myR = findBinarily(searchedRunway);
				if(myR == null) System.out.println("No such runway!");
				else
				{
					choseRunway = true;
					myRunway = myR;
				}
			}
			//Create a plane with the flight number and destination
			Plane myP = new Plane(fn, d);
			//get the binary flight's takeoff order and update the runway in that list
			takeOffOrderedRunways.get(myRunway.getRunwayNumber()).addReadyFlight(myP);
			System.out.println("Flight " + myP.getFlightNumber() + " ready for takeoff on runway " + myRunway.getRunwayName());
		}catch(AlreadyExistsException a)
		{
			System.out.println("Flight number already in use.");
		}
	}
	private static void takeOff() throws IOException {
		Plane plane = null;
		int size = takeOffOrderedRunways.size();
		Runway currRunway = null;

		for(int i = 0; i < size && plane == null; i++) {
			currRunway = takeOffOrderedRunways.get(nextTakeoffRunway);
			plane = currRunway.takeOff();
			nextTakeoffRunway = (nextTakeoffRunway + 1) % size;
		}
		if(plane == null) {
			System.out.println("No plane on any runway!");
		}else {
			System.out.print("Is flight " + plane.getFlightNumber() + " cleared for takeoff(Y/N): ");
			String confirmation = br.readLine();
			System.out.println(confirmation);

			if(confirmation.equals("Y")) {
				System.out.println("Flight " + plane.getFlightNumber() + " has now taken off from runway " + currRunway.getRunwayName());
				numTakeoffs++;
				takenOffPlanes.add(takenOffPlanes.size(), plane);
			}else {
				System.out.println("Flight " + plane.getFlightNumber() + " is now waiting to be allowed to re-enter a runway.");
				insertBinarily(plane, currRunway);
			}
		}
	}

	private static void allowEntrance() throws IOException {
		if(waitingPlanes.size() == 0) {
			System.out.println("There are no planes waiting for clearance!");
		}
		else
		{
			boolean adding = true;
			while(adding){
				//Look for planes until you find the one in the list of waiting. Not sure if this part works. 
				System.out.print("Enter flight number: ");
				String flightNum = br.readLine();
				System.out.println(flightNum);

				Object[] o = findPlaneBinarily(flightNum);
				Plane p = (Plane)o[0]; //method returns an object array, 0 is for the plane returned, 1 is for the runway it found. 
				Runway r = (Runway)o[1];
				if(p != null)
				{
					//Print and add that plane to the respective runway. The method removes both the plane and runway from the waiting list
					System.out.println("Flight " + p.getFlightNumber() + " is now wating for takeoff on runway " +  r.getRunwayName());
					System.out.println(r.getRunwayName());
					Runway newRunway= takeOffOrderedRunways.get(findBinarily(r.getRunwayName()).getRunwayNumber());
					newRunway.addReadyFlight(p);
					adding = false;
				}else {
					System.out.println("Flight " + flightNum + " is not waiting for clearance.");
				}
			}
		}
	}

	private static void openNewRunway() throws IOException {
		boolean found = false;
		while(!found)
		{
			//add an new runway to both lists
			System.out.print("Enter the name of the new runway : ");
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
				found = true;
			}
		}
	}

	private static void closeRunway() throws IOException {
		boolean removing = true;
		String foundRunway = "";
		Runway foundRunwayObject = null;

		while(removing) {
			System.out.print("Enter runway:");
			foundRunway = br.readLine();
			System.out.println(foundRunway);

			foundRunwayObject = findBinarily(foundRunway);
			if(foundRunwayObject == null) {
				System.out.println("No such runway!");
			}else {
				int it = 0;
				ListArrayBasedPlus<Plane> allReadyFlights = foundRunwayObject.getReadyFlights();

				int readySize = allReadyFlights.size();

				for(int i = 0; i < readySize; i++) {
					Plane currPlane = allReadyFlights.get(i);
					System.out.print("Enter new runway for plane " + currPlane.getFlightNumber() + ":");
					boolean reAdded = false;
					while(!reAdded) {
						String newRunway = br.readLine();
						System.out.println(newRunway);

						Runway runObj = findBinarily(newRunway);
						if(newRunway.equals(foundRunway)) {
							System.out.println("This is the runway that is closing!");
						}else if(runObj == null) {
							System.out.println("No such runway!");
						}else {
							System.out.println("Flight " + currPlane.getFlightNumber() + " is now waiting for takeoff on runway "+ newRunway+".");
							takeOffOrderedRunways.get(runObj.getRunwayNumber()).addReadyFlight(currPlane);
							int max = takeOffOrderedRunways.size();
							for(int subtracter = foundRunwayObject.getRunwayNumber() + 1; subtracter < max; subtracter++) {
								takeOffOrderedRunways.get(subtracter).setRunwayNumber(subtracter - 1);
							}
							reAdded=true;
						}
					}

				} 	

				int waitingSize = waitingRunways.size();
				for(int i = 0; i < waitingSize; i++) {
					Runway currRunway = waitingRunways.get(i);
					if(currRunway.getRunwayName().equals(foundRunway)) {
						Plane currPlane = waitingPlanes.get(i);
						System.out.print("Enter new runway for plane " + currPlane.getFlightNumber() + ":");
						boolean reAdded = false;
						while(!reAdded) {
							String newRunway = br.readLine();
							System.out.println(newRunway);
							Runway runObj = findBinarily(newRunway);
							if(newRunway.equals(foundRunway)) {
								System.out.println("This is the runway that is closing!");
							}else if(runObj == null) {
								System.out.println("No such runway!");
							}else {
								System.out.println("Flight " + currPlane.getFlightNumber() + " is now waiting for takeoff on runway "+ newRunway+".");
								waitingRunways.get(i).setRunwayName(newRunway);
								reAdded=true;
							}
						}
					}

				}
				takeOffOrderedRunways.remove(foundRunwayObject.getRunwayNumber());

				int index = findIndexBinarily(foundRunway);
				nameOrderedRunways.remove(index);

				boolean removedTakeOffOrdered = false;
				int orderIterator = 0;
				int takeOffSize = takeOffOrderedRunways.size();
				while(!removedTakeOffOrdered && orderIterator < takeOffSize) {
					if(takeOffOrderedRunways.get(orderIterator).getRunwayName().equals(foundRunway)) {
						takeOffOrderedRunways.remove(orderIterator);
						removedTakeOffOrdered = true;
					}
					orderIterator++;
				}

				System.out.println("Runway " + foundRunway + " has been closed.");
				removing = false;
			}

		}
	}
	private static void displayReadyPlanes() {
		int numRunways = takeOffOrderedRunways.size();
		for(int i = 0; i < numRunways; i++) {
			Runway currRunway = takeOffOrderedRunways.get(i);
			ListArrayBasedPlus<Plane> flights = currRunway.getReadyFlights();
			int numFlights = flights.size();
			if(numFlights == 0) {
				System.out.println("No planes are waiting for takeoff on runway " + currRunway.getRunwayName() + "!");
			}else{
				System.out.println("These planes are waiting for takeoff on runway " + currRunway.getRunwayName() + " : ");
				for(int j = 0; j < numFlights; j++) {
					System.out.println("Flight " + flights.get(j).getFlightNumber() + " to " + flights.get(j).getDestination() + ".");
				}
			}
		}
	}

	private static void displayWaitingPlanes() {
		int waitingSize = waitingPlanes.size();
		if(waitingSize == 0) System.out.println("No planes are waiting to be cleared to re-enter a runway!");
		else
		{
			System.out.println("These planes are waiting to be cleared to re-enter a runway:");
			for(int z = 0; z < waitingSize; z++)
			{
				Plane currFlight = waitingPlanes.get(z);
				System.out.println("Flight " + currFlight.getFlightNumber() + " to " + currFlight.getDestination());
			}
		}
	}

	private static void landPlane() throws IOException{
		if(takenOffPlanes.size() == 0) {
			System.out.println("There are no planes that have taken off!");
		}

		else
		{
			boolean landing = true;
			while(landing){
				//Look for planes until you find the one in the list of waiting.
				System.out.print("Enter flight number: ");
				String flightNum = br.readLine();
				System.out.println(flightNum);

				int index = 0;
				boolean found = false;
				int size = takenOffPlanes.size();
				for(;index < size && !found;){
					if(flightNum.equals(takenOffPlanes.get(index).getFlightNumber())){
						found = true;
					}else{
						index++;
					}
				}

				if(index == size){
					System.out.println("Flight " + flightNum + " has not yet taken off. Try again.");
				}else{
					String result = "Flight " + flightNum + " has landed in " + takenOffPlanes.get(index).getDestination() + "\n";
					sb.append(result);
					System.out.println(result);
					takenOffPlanes.remove(index);
					landing = false;
				}
			}
		}
	}

	private static void displayLandedPlanes(){
		if(sb.toString().equals("")){
			System.out.println("No planes have landed yet.");
		}else{
			System.out.println("These are the planes that have landed: ");
			System.out.println(sb.toString());
		}
	}

	//Insert a runway into the collection binarily
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
		mid = max;
		int end = -1;
		if(nameOrderedRunways.size() > mid) {
			end = r.getRunwayName().compareTo(nameOrderedRunways.get(mid).getRunwayName());
		}
		if(end == 0) {
			throw new AlreadyExistsException("Exception on adding into the runway.");
		}
		else
		{
			nameOrderedRunways.add(mid, r);
		}
	}

	//Find a Runway based on its name, binarily
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
		mid = max;
		int end = -1;
		if(nameOrderedRunways.size() > mid) {
			end = str.compareTo(nameOrderedRunways.get(mid).getRunwayName());
		}
		if(end == 0) {
			returnRunway = nameOrderedRunways.get(mid);
		}
		return returnRunway;
	}

	//Return the index of a runway in the name ordered collection by its name.
	private static int findIndexBinarily(String str)
	{
		Runway returnRunway = null;
		int min = 0;
		int max = nameOrderedRunways.size() - 1; 
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
		mid = max;
		//int end = str.compareTo(nameOrderedRunways.get(mid).getRunwayName());
		return mid;
	}

	//Find a plane binarily in the list of planes that are waiting. Index 0 of Object[] is the plane object itself, index 1 is the runway object (for printing purposes)
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
		mid = max;
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

	//Insert a plane and runway binarily into the waiting list
	private static void insertBinarily(Plane p, Runway r) 
	{
		int min = 0;
		int max = waitingPlanes.size(); 
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
		mid = max;
		waitingPlanes.add(mid, p);
		waitingRunways.add(mid, r);
	}



}
