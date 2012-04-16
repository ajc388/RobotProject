import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.CompassSensor;
import lejos.robotics.navigation.CompassPilot;

public class Robot1 {


	private static NXTRegulatedMotor cageMotor, leftMotor, rightMotor;
	private static Mapper mapper;
	private static CageController cageController;
	private static Navigator nav;
	private static CompassPilot pilot;
	private static CompassSensor compass;
	private static ObjectVerifier objectVerifier;
	private static ObjectDetector objectDetector;

	private static B1Comm b1;

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		b1 = new B1Comm();
		LCD.drawString("Got here", 1, 0);
		cageMotor = new NXTRegulatedMotor(MotorPort.C);
		leftMotor = new NXTRegulatedMotor(MotorPort.A);
		rightMotor = new NXTRegulatedMotor(MotorPort.B);
		
		compass = new CompassSensor(SensorPort.S3);
		pilot = new CompassPilot(compass, 2.3867536f, 16.19250f, leftMotor, rightMotor);
		
		nav = new Navigator(compass, pilot, leftMotor, rightMotor);

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
