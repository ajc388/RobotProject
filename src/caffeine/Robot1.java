import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensorHT;
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
	private static ColorSensorHT colorSense;
	private static LineDetector lineDetector;
	private static B1Comm b1;
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
	    	//Instantiate motors
	    	cageMotor = new NXTRegulatedMotor(MotorPort.C);
		leftMotor = new NXTRegulatedMotor(MotorPort.A);
		rightMotor = new NXTRegulatedMotor(MotorPort.B);
		//Instantiate snesors
		compass = new CompassSensor(SensorPort.S3);
		colorSense = new ColorSensorHT(SensorPort.S2);
			
		//Build all robot classes
		b1 = new B1Comm();
		pilot = new CompassPilot(compass, 2.3867536f, 16.19250f, leftMotor, rightMotor);
		pilot.setTravelSpeed(20.0f);
		pilot.setRotateSpeed(20.0f);
		pilot.setAcceleration(150);
		nav = new Navigator(compass, pilot, leftMotor, rightMotor);
		objectVerifier = new ObjectVerifier(colorSense);
		objectDetector = new ObjectDetector(nav);
		//mapper = new Mapper();
		lineDetector = new LineDetector(nav, mapper);
		cageController = new CageController(cageMotor);
		
		//Establish connections to other created objects
		b1.setObjectDetector(objectDetector);
		
		LCD.drawString("Got here", 1, 0);
		
		Button.ENTER.waitForPressAndRelease();
		
		

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
		
		nav.travel(2000);
		while (nav.isTraveling()) {
		    try {
			Thread.sleep(100);
		    } catch (InterruptedException e) {
			
		    }
		}
	}
}
