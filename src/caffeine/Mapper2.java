import java.util.ArrayList;
import java.util.Stack;

public class Mapper2 {
	
	private ArrayList<Coordinates> knownBalls;
	
	public Mapper2() {
		knownBalls = new ArrayList<Coordinates>();
	}
	
	public void recordBallLocation(double x, double y) {
		knownBalls.add(new Coordinates(x, y));
	}
	
	/**
	 * *guess* if we have already know about this ball or not.
	 */
	public boolean alreadyDetectedThisBall(double robotX, double robotY, double robotAngle, int distToBall) {
		double dx = Math.cos(Math.toRadians(robotAngle)) * distToBall;
		double dy = Math.sin(Math.toRadians(robotAngle)) * distToBall;
		
		double approximateX = robotX + dx;
		double approximateY = robotY + dy;
		
		for (Coordinates knownBall : knownBalls) {
			if(knownBall.getX() < approximateX + 5 && 
					knownBall.getX() > approximateX - 5 ||
					knownBall.getY() < approximateY + 5 && 
					knownBall.getY() > approximateY - 5) {
				return true;
			}
		}
		
		return false;
	}
	
	class Coordinates {
		
		private double x, y;
		
		public Coordinates (double x, double y) {
			this.x = x;
			this.y = y;
		}
		
		public double getX() {
			return x;
		}
		
		public double getY() {
			return y;
		}
	}
}
