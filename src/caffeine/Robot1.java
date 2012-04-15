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
		
		try{
		b1.transmitNavCommand(0, 10);
		Thread.sleep(2000);
		b1.transmitNavCommand(0, 20);
		Thread.sleep(2000);
		b1.transmitNavCommand(0, 30);
		Thread.sleep(2000);
		b1.transmitNavCommand(0, 40);
		} catch (Exception e){}
	}
}
