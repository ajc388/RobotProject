import lejos.nxt.UltrasonicSensor;

public class ObjectVerifier
{
	UltrasonicSensor left;
	UltrasonicSensor front;
	UltrasonicSensor right;
	B1Comm comm;
	private boolean isRed;

	ObjectDetector objd;
	Navigator nav;

	public ObjectVerifier(UltrasonicSensor u1, UltrasonicSensor u2, UltrasonicSensor u3, B1Comm comm, ObjectDetector objd, Navigator nav)
	{
		left = u1;
		front = u2;
		right = u3;
		this.comm = comm;
		this.objd = objd;
		this.nav = nav;
	}
	private void setIsRed()
	{
		//Scans ahead of it, returns true if detecting a red ball, otherwise false
		isRed = comm.getColorSensorData();
	}

	public boolean isRed() {
		return isRed;
	}

	public void navToBall(int ultrasonic)
	{
		int rotatecount = 0;
		switch(ultrasonic)
		{
		//Turn left
		case 1: rotateToFrontSensor(true); break;
		case 2: break;
		//Turn right
		case 3: rotateToFrontSensor(false); break;
		}
		/*while (front.getDistance() > 60){//In case we're not quite in range, and stopped sensing it, move forward a bit.
			nav.travel(5);
			nav.waitForTravel();
		}*/
		while(front.getDistance() < 60)
		{
			nav.rotate(5);
		}
		nav.rotate(-10);
		while(front.getDistance() < 60)
		{
			nav.rotate(-5);
			rotatecount++;
		}
		nav.rotate(rotatecount*5/2); //This is simpler than an for loop
		/*for(int i=0; i<=rotatecount/2; i++)
				{
					nav.rotate(5);
				}*/
		//Insures that it doesnt go further than 
		//The sensor should have detected
		if ( front.getDistance() < 60 ) {
        		nav.travel(front.getDistance() - 15);
        		nav.waitForTravel();
        		rotatecount = 0;
        		setIsRed();
		}	
		

	}
	
	//Once an object is detected from the left or right sensor
	//It will rotate to the object (either left or right)
	//Until it detects the object on the front sensor.
	public void rotateToFrontSensor(boolean left) {
	    double distance = front.getDistance();
	    int loopIterations = 0;
	    while( loopIterations < 25 && 
		   !(distance >= 0 && distance <= 60) ) {
		    //Check to see where its supposed to rotate 
		    if ( left ) {
			//Rotate left
        		nav.rotate(5); 
        	    } else {
        		//Rotate right
        		nav.rotate(-5);
        	    }
		    loopIterations++;
	    }
	}

}
