package caffeine;

/*
 * This is the Communicator to go on caffeine2
 * this will receive communication initiation
 */
public class B2Comm {
	public B2Comm(){
		//Constructor
	}
	public void notifyInPosition(){
		//Notifies that it is in position for scanning a ball
	}
	public void notifyReadyToLower(){
		//Notifies that it is in position to lower the cage for capturing
	}
	public void requestNewNav(){
		//Asks the mapper to send a new navigation command
	}
	public void sendCoordinates(){
		//Sends coordinates to the other brick
	}
}
