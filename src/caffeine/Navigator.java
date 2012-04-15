import lejos.nxt.addon.CompassSensor;
import lejos.robotics.navigation.CompassPilot;
import java.lang.Math;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.Motor;

@SuppressWarnings("deprecation")
public class Navigator {

	/**
	 * Navigator
	 *
	 * Uses CompassPilot to navigate to certain points.
	 */

	private CompassSensor compass;
	private CompassPilot pilot;

	/** Simple representation of a point */
	private long lastX, lastY;
	private long x, y;
	private double curAngle;
	
	/** Boolean flag to decide what to listen to */
	private boolean listenToMapper;

	/**
	 * Constructor
	 * @param compass - takes a compass so we can abstract away from port selection
	 * @param pilot - takes a pilot so we can set that up elsewhere
	 */
	public Navigator(CompassSensor compass, CompassPilot pilot) {
		this.compass = compass;
		this.pilot = pilot;

		listenToMapper = true;
		
		compass.resetCartesianZero();
		x = 0;
		y = 0;
		lastX = x;
		lastY = y;
		curAngle = 0.0;
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
	public void navigateTo(long newX, long newY) {
		long dx = newX - x;
		long dy = newY - y;
		double angle = calcAngleTo(dx, dy);
		long distance = calcDistanceTo(dx, dy);

		pilot.rotate(angle);
		pilot.travel(distance);
		
		lastX = x;
		lastY = y;

		// these values will be correct if the move actually completes.
		// if it is interrupted, the x, y will need to be recalculated.
		x = newX;
		y = newY;
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
		long distActual = (long) pilot.getMovementIncrement();
		pilot.stop(); // API shows method quickStop() but it doesn't seem to be included. Use stop() for now.
		long dxTheoretical = x - lastX;
		long dyTheoretical = y - lastY;
		long distTheoretical = calcDistanceTo(dxTheoretical, dyTheoretical);
		long ratio = distActual / distTheoretical;
		long dxActual = dxTheoretical * ratio;
		long dyActual = dyTheoretical * ratio;
		x = lastX + dxActual;
		y = lastY + dyActual;
	}

	private double calcAngleTo(double dx, double dy) {
		//This is bad, but should work, it just won't always make the shortest turn at the moment.
		return (-Math.atan2(dx, dy) * 180 / Math.PI) - curAngle;
	}

	private long calcDistanceTo(long dx, long dy) {
		return (long) Math.sqrt(dx * dx + dy * dy);
	}
	
	public void travel(long distance) {
		pilot.travel(distance);
		//add ability to calc new coords.
	}
	
	public boolean listeningToMapper() {
		return listenToMapper;
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
	public long getX() {
		return x;
	}
	
	/**
	 * public getter for robots y position. Won't be 
	 * accurate if the robot is moving at the time. 
	 * This could potentially be fixed, but I don't see a reason
	 * for it at this time.
	 */
	public long getY() {
		return y;
	}
	
	public double getAngle() {
		return curAngle;
	}
	
	public static void main(String[] args) {
		Button.ENTER.waitForPress();
		CompassSensor compass = new CompassSensor(SensorPort.S4);
		CompassPilot pilot = new CompassPilot(compass, 2.9255357f, 16.19250f, Motor.A, Motor.B);
		pilot.setTravelSpeed(20.0f);
		pilot.setRotateSpeed(20.0f);
		Navigator nav = new Navigator(compass, pilot);

		nav.navigateTo(5, 0);
		nav.navigateTo(5, 5);
		nav.navigateTo(-5, 5);
		nav.navigateBackToZero();
	}
	
	
//Steffen, just put the code you already made in here
//Add things to notify the mapper that you're at coordinates at the end of movement, request new ones
//Add stop command to override current movement when a line or ball is detected
}
