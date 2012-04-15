import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;

public class Robot1 {


	private static NXTRegulatedMotor cageMotor;
	private static Mapper mapper;
	private static CageController cageController;
	private static ObjectVerifier objectVerifier;
	private static ObjectDetector objectDetector;

	private static B1Comm b1;

	public static void main(String[] args) {
		b1 = new B1Comm();
		LCD.drawString("Got here", 1, 0);
		cageMotor = new NXTRegulatedMotor(MotorPort.C);

		//mapper = new Mapper();
		cageController = new CageController(cageMotor);

		new Thread(b1).start();
		
		/*
		 * While red ball is not found:
		 * 	If ObjectDetector not examining an object
		 * 		Ask mapper for new coordinates to explore
		 * 			while navigator is travelling or investigating a ball 
		 * 				Sleep for 100 ms
		 * 			If we found red, continue loop
		 * 			Explore those coordinates
		 * 			
		 * Go home
		 */
	}
}
