

/*
 * Object detector residing on caffeine2
 */
public class ObjectDetector{
	//Add ArrayList later for detected objects
	private boolean listening, waitingOnFirstRight, waitingOnFirstLeft, waitingOnSecondRight, waitingOnSecondLeft;
	//Toggles whether we're listening to each and if we're on the first one
	private Navigator nav;
	private static final int MOVEDIST = 10;
	private static final int MOVEDISTSQ = 100;
	private static final double XOFFSET = 4.5;
	private static final double YOFFSET = 9.0;
	private int dist1, dist2;
	public ObjectDetector(Navigator nav){
		//Initialize list of found blue balls to ignore
		//Initialize the three sensors, start a thread that queries them repeatedly
		listening = true;
		waitingOnFirstRight = false;
		waitingOnFirstLeft = true;
		this.nav = nav;
	}

	public void processFront (int dist){
		//TODO: All this shit! Turn and call right or left
	}

	public void processRight (int dist){
		if (listening){//We are listening for a new object detected
			//We're not waiting for new input, so start
			listening = false;
			nav.toggleListenToMapper(false);//Make the navigator stop listening to the mapper
			while (nav.isTravelling()){
				Thread.sleep(100); //Wait for it to stop moving
			}
			waitingOnFirstRight = true;
		} else 	if (waitingOnFirstRight){
			dist1 = dist;
			waitingOnFirstRight = false;
			waitingOnSecondRight = true;
		} else if (waitingOnSecondRight){
			dist2 = dist;
			//Do the processing here
			double deltaX = (double) ((dist2*dist2) - (dist1*dist1)-MOVEDISTSQ);
			double deltaY = Math.sqrt((dist1*dist1) - (deltaX*deltaX));
			double DELTAX = deltaX + XOFFSET;
			double DELTAY = deltaY - YOFFSET; //Make this addition for other side
			//if it's red, head home
			//if it's blue, then:
			waitingOnSecondRight = false;
			listening = true;
		}
	}

	public void processLeft(int dist) {
		if (listening){//We are listening for a new object detected
			//We're not waiting for new input, so start
			listening = false;
			nav.toggleListenToMapper(false);//Make the navigator stop listening to the mapper
			while (nav.isTravelling()){
				Thread.sleep(100); //Wait for it to stop moving
			}
			waitingOnFirstLeft = true;
		} else 	if (waitingOnFirstLeft){
			dist1 = dist;
			waitingOnFirstLeft = false;
			waitingOnSecondLeft = true;
		} else if (waitingOnSecondLeft){
			dist2 = dist;
			//Do the processing here
			double deltaX = (double) ((dist2*dist2) - (dist1*dist1)-MOVEDISTSQ);
			double deltaY = Math.sqrt((dist1*dist1) - (deltaX*deltaX));
			double DELTAX = deltaX + XOFFSET;
			double DELTAY = deltaY + YOFFSET; //Make this addition for other side
			double heading = nav.getAngle();
			double deltaXT = DELTAX*cosD(heading) - DELTAY*sinD(heading);
			double deltaYT = DELTAX*sinD(heading) + DELTAY*cosD(heading);
			nav.navigateTo((long)(nav.getX()+deltaXT), (long)(nav.getY()+deltaYT));
			//if it's red, head home
			//if it's blue, then:
			waitingOnSecondLeft = false;
			listening = true;
		}
	}
	
	private double cosD(double angle){
		return Math.cos(Math.PI/180*angle);
	}
	private double sinD(double angle){
		return Math.sin(Math.PI/180*angle);
	}
}