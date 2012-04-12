import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassSensor;
import lejos.robotics.navigation.CompassPilot;

public class Robot2 {

	private final static double WHEEL_DIAMETER = 2.925535;
	private final static double WHEEL_WIDTH = 16.1925;

	private static UltrasonicSensor left;
	private static UltrasonicSensor right;
	private static UltrasonicSensor front;
	private static ObjectDetector od;
	private static CompassSensor compass;
	private static CompassPilot pilot;
	private static NXTRegulatedMotor leftWheel;
	private static NXTRegulatedMotor rightWheel;
	private static Navigator navigator;

	private static B2Comm b2;

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		b2 = new B2Comm();

		left = new UltrasonicSensor(SensorPort.S1);
		front = new UltrasonicSensor(SensorPort.S2);
		right = new UltrasonicSensor(SensorPort.S3);
		compass = new CompassSensor(SensorPort.S4);

		leftWheel = new NXTRegulatedMotor(MotorPort.A);
		rightWheel = new NXTRegulatedMotor(MotorPort.B);

		pilot = new CompassPilot(compass, (float) WHEEL_DIAMETER, (float) WHEEL_WIDTH, leftWheel, rightWheel);

		navigator = new Navigator(compass, pilot);

		b2.setNavigator(navigator);
		b2.setObjectDetector(od);
		b2.run();
	}
}
