import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensorHT;
import lejos.nxt.addon.CompassSensor;
import lejos.robotics.navigation.CompassPilot;

public class Robot1 {


	private NXTRegulatedMotor cageMotor, leftMotor, rightMotor;
	private Mapper2 mapper;
	private CageController cageController;
	private Navigator nav;
	private CompassPilot pilot;
	private CompassSensor compass;
	private ObjectVerifier objectVerifier;
	private ObjectDetector objectDetector;
	private ColorSensorHT colorSense;
	private LineDetector lineDetector;
	private B1Comm b1;
	private LightSensor2 lightSensor;
	
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
		compass = new CompassSensor(SensorPort.S3);
		colorSense = new ColorSensorHT(SensorPort.S2);
		lightSensor = new LightSensor2(SensorPort.S1 );	
	
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
		pilot = new CompassPilot(compass, 2.3867536f, 16.19250f, leftMotor, rightMotor);
		pilot.setTravelSpeed(20.0f);
		pilot.setRotateSpeed(20.0f);
		pilot.setAcceleration(150);
		nav = new Navigator(compass, pilot, leftMotor, rightMotor);
		objectVerifier = new ObjectVerifier(colorSense);
		objectDetector = new ObjectDetector(nav);
		mapper = new Mapper2();
		lineDetector = new LineDetector(lightSensor, nav);
		cageController = new CageController(cageMotor);
		
		
		//Establish connections to other created objects
		b1.setObjectDetector(objectDetector);
		

		//Startup sequence should go here. Leave the start boundaries 
		//before starting the object and line detecting.
		
		nav.travel(20);
		nav.waitForTravel();
		
		Thread lineThread = new Thread(lineDetector);
		lineThread.start();
		
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
		
		nav.travel(500);
		nav.waitForTravel();
		nav.rotate(45);
		nav.travel(40);
		nav.waitForTravel();
		nav.navigateHome();
		nav.waitForTravel();
	}
}
