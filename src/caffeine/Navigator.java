import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.addon.CompassSensor;
import lejos.robotics.navigation.CompassPilot;
import lejos.robotics.navigation.DifferentialPilot;

@SuppressWarnings("deprecation")
public class Navigator {

	/**
	 * Navigator
	 *
	 * Uses CompassPilot to navigate to certain points.
	 */

	//private CompassSensor compass;
	private DifferentialPilot pilot;
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
	public Navigator(DifferentialPilot pilot, NXTRegulatedMotor leftMotor, NXTRegulatedMotor rightMotor) {
		this.pilot = pilot;
		
		//compass.resetCartesianZero();
		
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;

		listenToMapper = true;
		
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

	public void navigateHome() {
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
		pilot.setAcceleration(100000);
		pilot.stop(); // API shows method quickStop() but it doesn't seem to be included. Use stop() for now.
		pilot.setAcceleration(150);
		double dx = Math.cos(Math.toRadians(curAngle)) * distance;
		double dy = Math.sin(Math.toRadians(curAngle)) * distance;
		
		x = lastX + dx;
		y = lastY + dy;
		
		travel(0);
	}
	
	public void avoidObject() {
		avoidObject(true);
	}
	
	public void avoidObject(boolean toTheRight) {
		int i = 1;
		if (toTheRight) i = -1; 
		rotate(90 * i);
		travel(10);
		waitForTravel();
		rotate(-90 * i);
		travel(10);
		waitForTravel();
		rotate(-90 * i);
		travel(10);
		waitForTravel();
		rotate(90 * i);
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
	
	public void waitForTravel() {
		while (isTraveling()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				
			}
		}
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
	
}
