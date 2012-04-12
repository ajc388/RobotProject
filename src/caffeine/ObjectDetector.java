
/*
 * Object detector residing on caffeine2
 */
public class ObjectDetector {
	public ObjectDetector(){
		//Initialize list of found blue balls to ignore
		//Initialize the three sensors, start a thread that queries them repeatedly
	}
	public void stopScanning(){
		//When red ball is found, don't need to find it any more!
	}
	
	
	//Make a thread here to scan and notify
	
	
	public void notifyBallColor(boolean wasRed){
		//wasRed tells whether the ball that was navigated to was red or not.
		//If blue, add to list of blue balls to ignore for detection purposes
	}
	public void notifyCageIsUp(){
		//Lets it know it can move forward to capture
	}
	public void notifyCaptureReady(){
		//Lets it know it can capture
	}
	public void notifyCageIsBackDown(){
		//Cage is down, we have captured ball, tell mapper to go home
	}
}
