import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.ColorSensorHT;
import lejos.nxt.addon.CompassSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class Robot1 {


	private NXTRegulatedMotor cageMotor, leftMotor, rightMotor;
	private Mapper2 mapper;
	private CageController cageController;
	private Navigator nav;
	//private CompassPilot pilot;
	private DifferentialPilot pilot;
	private CompassSensor compass;
	private ObjectVerifier objectVerifier;
	private ObjectDetector objectDetector;
	private ColorSensorHT colorSense;
	private LineDetector lineDetector;
	private B1Comm b1;
	private LightSensor2 lightSensor;
	private UltrasonicSensor leftUS, rightUS, frontUS;
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
	    new Robot1();
	}
	
	public Robot1() {
		//Instantiate motors
	    cageMotor = new NXTRegulatedMotor(MotorPort.C);
		leftMotor = new NXTRegulatedMotor(MotorPort.A);
		rightMotor = new NXTRegulatedMotor(MotorPort.B);
		//Instantiate snesors
		//compass = new CompassSensor(SensorPort.S3);
		lightSensor = new LightSensor2(SensorPort.S1 );
		leftUS = new UltrasonicSensor(SensorPort.S2);
		frontUS = new UltrasonicSensor(SensorPort.S3);
		rightUS = new UltrasonicSensor(SensorPort.S4);
	
		//Calibrate sensor
		/*LCD.drawString( "Place on carpet", 0 , 1) ;
		Button.ENTER.waitForPressAndRelease();
		lightSensor.calibrateLow();
		
		LCD.drawString( "Place on line", 0 , 2) ;
		Button.ENTER.waitForPressAndRelease();
		lightSensor.calibrateHigh();
		
		LCD.clear();
		lightSensor.saveCalibration();*/
		
		//Build all robot classes
		b1 = new B1Comm();
		//pilot = new CompassPilot(compass, 2.3867536f, 16.19250f, leftMotor, rightMotor);
		pilot = new DifferentialPilot(2.3867536f, 16.19250f, leftMotor, rightMotor);
		pilot.setTravelSpeed(20.0f);
		pilot.setRotateSpeed(20.0f);
		pilot.setAcceleration(150);
		nav = new Navigator(pilot, leftMotor, rightMotor);
		objectDetector = new ObjectDetector(leftUS, frontUS, rightUS, nav);
		objectVerifier = new ObjectVerifier(leftUS, frontUS, rightUS, b1, objectDetector, nav);
		mapper = new Mapper2();
		cageController = new CageController(cageMotor);
		lineDetector = new LineDetector(lightSensor, nav, cageController);
		
		
		//Establish connections to other created objects
		

		//Startup sequence should go here. Leave the start boundaries 
		//before starting the object and line detecting.
		
		startupSequence();
		
		Thread lineThread = new Thread(lineDetector);
		lineThread.start();
		Thread detectorThread = new Thread(objectDetector);
		detectorThread.start();
		
		//new Thread(b1).start();
		
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
		
		while (true) {
			searchForObjects();
			verifyObject();
			break;
		}
		nav.navigateHome();
		
		
	}
	
	public void startupSequence() {
		nav.travel(20);
		nav.waitForTravel();
	}
	
	public void searchForObjects() {
		while (objectDetector.isDetecting()) {
			nav.travel(500);
			nav.waitForTravel();
			if (objectDetector.isDetecting()) {
				nav.rotate(90);
			}
		}
	}
	
	public void verifyObject() {
		int us = 0;
		if (objectDetector.isBallOnLeft()) us = 1;
		else if (objectDetector.isBallInFront()) us = 2;
		else if (objectDetector.isBallOnRight()) us = 3;
		objectVerifier.navToBall(us);
		if (!objectVerifier.isRed()) {
			cageController.raiseCage();
			nav.travel(25);
			nav.waitForTravel();
			cageController.lowerCage();
			lineDetector.setHasBall();
		} else {
			nav.avoidObject();
		}
	}
}
