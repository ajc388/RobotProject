package caffeine;

/*
 * This is the Communicator to go on caffeine
 * this will initiate bluetooth communications
 * 
 * 

/*==============================================*
 *  	        PORTS CONVENTION	   	*
 *  						*
 *	Brick 1					*
 *	MotorA == Left wheel			*
 *	MotorB == Right wheel			*
 *	MotorC == Cage				*
 *	SensorPort1 == Left Ultra Sensor	*
 *	SensorPort2 == Forward Ultra Sensor	*
 *	SensorPort3 == Right Ultra Sensor	*
 *	SensorPort4 == Compass Sensor		*
 *						*
 *  	Brick 2					*
 *  	SensorPort1 == LightSensor		*
 *  	SensorPort2 == ColorSensor		*
 *  						*
 * =============================================*
 */
 



//Request for current coordinates (code)
public class B1Comm {
	public B1Comm(){
		
	}
	public void notifyLineDetected(){
		//Notify that a line was detected to navigator
	    
	}
	public void transmitColor (boolean wasRed){
		//Transmits the detected color
	}
	public void transmitNavCommand (long x, long y){
		//Transmits a command from the mapper to the navigator to go to coordinates (x,y)
	}
	public void notifyReadyForCapture(){
		//Notifies that the cage is up so we can move forward to capture the ball
	}
	public void requestCoordinates(){
		//Request the navigator send the current coordinates so the mapper can do what it needs to do
	}
	
}
