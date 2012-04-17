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
			case 1:
				nav.rotate(70);
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
				for(int i=0; i<=rotatecount/2; i++)
				{
					nav.rotate(5);
				}
				nav.travel(front.getDistance() - 15);
				nav.waitForTravel();
				rotatecount = 0;
				break;
			case 2:
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
				for(int i=0; i<=rotatecount/2; i++)
				{
					nav.rotate(5);
				}
				nav.travel(front.getDistance() - 10);
				nav.waitForTravel();
				rotatecount = 0;
				break;
			case 3:
				nav.rotate(-70);
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
				for(int i=0; i<=rotatecount/2; i++)
				{
					nav.rotate(5);
				}
				nav.travel(front.getDistance() - 15);
				nav.waitForTravel();
				rotatecount = 0;
				break;
		}
		setIsRed();
		
	}
}