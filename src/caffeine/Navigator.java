package caffeine;

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
	private float lastX, lastY;
	private float x, y;
	private float curAngle;

	/** Scale for movement distance between adjacent points */
	private final float SCALE = 20.0f;

	/**
	 * Constructor
	 * @param compass - takes a compass so we can abstract away from port selection
	 * @param pilot - takes a pilot so we can set that up elsewhere
	 */

	public Navigator(CompassSensor compass, CompassPilot pilot) {
		this.compass = compass;
		this.pilot = pilot;

		compass.resetCartesianZero();
		x = 0.0f;
		y = 0.0f;
		lastX = x;
		lastY = y;
		curAngle = 0.0f;
	}

	/**
	 * Rotates robot towards specified point.
	 * Begins traveling and returns immediately.
	 * This allows for the movement to be interrupted.
	 */

	public void navigateTo(float newX, float newY) {
		float dx = newX - x;
		float dy = newY - y;
		float angle = calcAngleTo(dx, dy);
		float distance = calcDistanceTo(dx, dy) * SCALE;

		pilot.rotate(angle);
		pilot.travel(distance, true);
		
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
		float distActual = pilot.getMovementIncrement();
		pilot.stop(); // API shows method quickStop() but it doesn't seem to be included. Use stop() for now.
		float dxTheoretical = x - lastX;
		float dyTheoretical = y - lastY;
		float distTheoretical = calcDistanceTo(dxTheoretical, dyTheoretical);
		float ratio = distActual / distTheoretical;
		float dxActual = dxTheoretical * ratio;
		float dyActual = dyTheoretical * ratio;
		x = lastX + dxActual;
		y = lastY + dyActual;
	}

	private float calcAngleTo(float dx, float dy) {
		//This is bad, but should work, it just won't always make the shortest turn at the moment.
		return (float) (-Math.atan2(dx, dy) * 180 / Math.PI) - curAngle;
	}

	private float calcDistanceTo(float dx, float dy) {
		return (float) Math.sqrt(dx * dx + dy * dy);
	}
	
	public static void main(String[] args) {
		Button.ENTER.waitForPress();
		CompassSensor compass = new CompassSensor(SensorPort.S4);
		CompassPilot pilot = new CompassPilot(compass, 3.17500f, 16.19250f, Motor.A, Motor.B);
		pilot.setTravelSpeed(20.0f);
		pilot.setRotateSpeed(20.0f);
		Navigator nav = new Navigator(compass, pilot);

		nav.navigateTo(5.0f, 1.0f);
		nav.navigateTo(-5.0f, 3.0f);
		nav.navigateTo(-5.0f, 5.0f);
		nav.navigateBackToZero();
	}
}