import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassSensor;
import lejos.robotics.navigation.CompassPilot;

public class Robot1 {
	
	private final double WHEEL_DIAMETER = 3.175;
	private final double WHEEL_WIDTH = 16.1925;
	
	private UltrasonicSensor left, right, front;
	private CompassSensor compass;
	private CompassPilot pilot;
	
	private NXTRegulatedMotor leftWheel, rightWheel, cageMotor;
	
	private Navigator navigator;
	private Mapper mapper;
	private CageController cageController;
	
	public Robot1(){
		left = new UltrasonicSensor(SensorPort.S1);
		front = new UltrasonicSensor(SensorPort.S2);
		right = new UltrasonicSensor(SensorPort.S3);
		compass = new CompassSensor(SensorPort.S4);
		
		leftWheel = new NXTRegulatedMotor(MotorPort.A);
		rightWheel = new NXTRegulatedMotor(MotorPort.B);
		cageMotor = new NXTRegulatedMotor(MotorPort.C);
		
		pilot = new CompassPilot(compass, WHEEL_DIAMETER, WHEEL_WIDTH, leftWheel, rightWheel);
		
		navigator = new Navigator(compass, pilot);
		mapper = new Mapper();
		cageController = new CageController(cageMotor);
		
		
	}

}
