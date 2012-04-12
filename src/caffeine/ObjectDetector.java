import lejos.nxt.UltrasonicSensor;

/*
 * Object detector residing on caffeine2
 */
public class ObjectDetector implements Runnable{
	//Add arraylist later
	private boolean running;
	Navigator nav;
	UltrasonicSensor US1, US2, US3; //Front, Left, and Right
	public ObjectDetector(UltrasonicSensor US1, UltrasonicSensor US2, UltrasonicSensor US3, Navigator nav){
		//Initialize list of found blue balls to ignore
		//Initialize the three sensors, start a thread that queries them repeatedly
		this.US1 = US1;
		this.US2 = US2;
		this.US3 = US3;
		this.nav = nav;
		running = true;
	}
	public void stopScanning(){
		running = false;
		//When red ball is found, don't need to find it any more!
	}



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

	public void run() {
		while (running){
			int	d1 = US1.getDistance();
			int d2 = US2.getDistance();
			int d3 = US3.getDistance();
			if (d1<50){
				notifyFront(d1);
			}
			if (d2<50){
				notifyRight(d2);
			}
			if (d3<50){
				notifyLeft(d3);
			}
		}
	}

	private void notifyFront (int dist){
		
	}
	private void notifyRight (int dist){
		stopScanning();
		nav.toggleListenToMapper(false);
		Thread.sleep(1000);// TODO: Make this more precise later!
		int d1 = US2.getDistance();
		nav.travel(10);
		int d2 = US2.getDistance();
		int deltaX = (d1*d1+100-(d2*d2))/20;
		int deltaY = (int)(Math.sqrt((double)(d1*d1-deltaX*deltaX)));
		int Xn = (int)(nav.getX() + 9 + deltaX);
		int Yn = (int)(nav.getY() - 4.5 + deltaY);
		nav.navigateTo(Xn, Yn);
		
	}
	private void notifyLeft (int dist){
		
	}
}