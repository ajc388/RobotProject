import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassSensor;
import lejos.robotics.navigation.CompassPilot;

public class Robot1 {


	private static NXTRegulatedMotor cageMotor;
//	private static Mapper mapper;
	private static CageController cageController;
	private static ObjectVerifier objectVerifier;

	private static B1Comm b1;

	public static void main(String[] args) {
		b1 = new B1Comm();

		cageMotor = new NXTRegulatedMotor(MotorPort.C);

		//mapper = new Mapper();
		cageController = new CageController(cageMotor);

		b1.setCageController(cageController);
		//b1.setMapper(mapper);
		b1.setObjectVerifier(objectVerifier);
		b1.run();
		b1.transmitNavCommand(0, 10);
		b1.transmitNavCommand(0, 20);
		b1.transmitNavCommand(0, 30);
		b1.transmitNavCommand(0, 40);
	}
}
