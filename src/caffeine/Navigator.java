import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.MotorPort;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.CompassSensor;
import lejos.robotics.navigation.CompassPilot;

@SuppressWarnings("deprecation")
public class Navigator {

	/**
	 * Navigator
	 *
	 * Uses CompassPilot to navigate to certain points.
	 */

	private CompassSensor compass;
	private CompassPilot pilot;
	//private DifferentialPilot pilot;

	/** Simple representation of a point */
	private double lastX, lastY;
	private double x, y;
	private double curAngle;
	
	/** Boolean flag to decide what to listen to */
	private boolean listenToMapper;
	
	/** Motors */
	NXTRegulatedMotor leftMotor, rightMotor;

	/**
	 * Constructor
	 * @param compass - takes a compass so we can abstract away from port selection
	 * @param pilot - takes a pilot so we can set that up elsewhere
	 */
	public Navigator(CompassSensor compass, CompassPilot pilot, NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
		this.compass = compass;
		this.pilot = pilot;
		
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;

		listenToMapper = true;
		
		compass.resetCartesianZero();
		x = 0;
		y = 0;
		lastX = x;
		lastY = y;
		curAngle = 90.0;
	}

	/**
	 * Rotates robot towards specified point.
	 * Begins traveling and returns immediately.
	 * This allows for the movement to be interrupted.
	 * 
	 * After testing... The travel method DOES immediately
	 * return... but the robot doesn't move. Not sure how to make this work.
	 * So I reverted to the not immediate return.
	 */
	public void navigateTo(double newX, double newY) {
		double dx = newX - x;
		double dy = newY - y;
		double angle = calcAngleTo(dx, dy);
		double distance = calcDistanceTo(dx, dy);

		rotate(angle);
		travel(distance);
	}
	
	public void travel(double distance) {
		pilot.travel(distance, true);
		double dx = Math.cos(Math.toRadians(curAngle)) * distance;
		double dy = Math.sin(Math.toRadians(curAngle)) * distance;
		
		lastX = x;
		lastY = y;
		
		x += dx;
		y += dy;
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			
		}
	}
	
	public void rotate(double angle) {
		pilot.rotate(angle);
		
		curAngle = angle + curAngle;
	}

	public void navigateBackToZero() {
		navigateTo(0, 0);
	}
	
	/**
	 * Interrupts current movement and recalculates position.
	 * Probably could use real geometry here, but Java's math
	 * class is slow and also I don't remember trig off the top
	 * of my head.
	 */
	public void emergencyStop() {
		double distance = pilot.getMovementIncrement();
		pilot.setAcceleration(10000);
		pilot.stop(); // API shows method quickStop() but it doesn't seem to be included. Use stop() for now.
		pilot.setAcceleration(150);
		double dx = Math.cos(Math.toRadians(curAngle)) * distance;
		double dy = Math.sin(Math.toRadians(curAngle)) * distance;
		
		x = lastX + dx;
		y = lastY + dy;
	}

	private double calcAngleTo(double dx, double dy) {
		//This is bad, but should work, it just won't always make the shortest turn at the moment.
		return (-Math.atan2(dx, dy) * 180 / Math.PI) - curAngle + 90;
	}

	private double calcDistanceTo(double dx, double dy) {
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	public boolean listeningToMapper() {
		return listenToMapper;
	}
	
	public boolean isTraveling() {
		return leftMotor.isMoving() || rightMotor.isMoving();
	}
	
	public void toggleListenToMapper(boolean listenToMapper) {
		this.listenToMapper = listenToMapper;
	}
	
	/**
	 * public getter for robots x position. Won't be 
	 * accurate if the robot is moving at the time. 
	 * This could potentially be fixed, but I don't see a reason
	 * for it at this time.
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * public getter for robots y position. Won't be 
	 * accurate if the robot is moving at the time. 
	 * This could potentially be fixed, but I don't see a reason
	 * for it at this time.
	 */
	public double getY() {
		return y;
	}
	
	public double getAngle() {
		return curAngle;
	}
	
	public static void main(String[] args) {
		Button.ENTER.waitForPress();
		
		NXTRegulatedMotor leftMotor = new NXTRegulatedMotor(MotorPort.A);
		NXTRegulatedMotor rightMotor = new NXTRegulatedMotor(MotorPort.B);
		CompassSensor compass = new CompassSensor(SensorPort.S3);
		CompassPilot pilot = new CompassPilot(compass, 2.3867536f, 16.19250f, leftMotor, rightMotor);
		//DifferentialPilot pilot = new DifferentialPilot(2.3867536f, 16.19250f, Motor.A, Motor.B);
		pilot.setTravelSpeed(20.0f);
		pilot.setRotateSpeed(20.0f);
		pilot.setAcceleration(150);
		Navigator nav = new Navigator(compass, pilot, leftMotor, rightMotor);

		nav.navigateTo(40.0, 40.0);
		LCD.drawString("Traveling: " + nav.isTraveling(), 0, 1);
		while (nav.isTraveling()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				
			}
		}
		nav.navigateTo(-40.0, 40.0);
		while (nav.isTraveling()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				
			}
		}
		nav.navigateTo(5.0, 15.0);
		while (leftMotor.isMoving() || rightMotor.isMoving()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				
			}
		}
		nav.navigateTo(7.0, -19.0);
		while (leftMotor.isMoving() || rightMotor.isMoving()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				
			}
		}
		nav.navigateTo(-9.0, 11.0);
		while (leftMotor.isMoving() || rightMotor.isMoving()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				
			}
		}
		nav.navigateTo(10.0, 15.0);
		while (leftMotor.isMoving() || rightMotor.isMoving()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				
			}
		}
		nav.navigateBackToZero();
		while (leftMotor.isMoving() || rightMotor.isMoving()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				
			}
		}
		/*nav.travel(2.54);
		Button.ENTER.waitForPress();
		nav.travel(2.54);
		Button.ENTER.waitForPress();
		nav.travel(2.54);
		Button.ENTER.waitForPress();
		nav.travel(2.54);
		Button.ENTER.waitForPress();
		nav.travel(2.54);
		Button.ENTER.waitForPress();
		nav.travel(2.54);
		Button.ENTER.waitForPress();
		nav.travel(2.54);
		Button.ENTER.waitForPress();
		nav.travel(2.54);
		Button.ENTER.waitForPress();
		nav.travel(2.54);
		Button.ENTER.waitForPress();*/
	}
	
	
//Steffen, just put the code you already made in here
//Add things to notify the mapper that you're at coordinates at the end of movement, request new ones
//Add stop command to override current movement when a line or ball is detected
}
