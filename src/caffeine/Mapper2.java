import java.util.ArrayList;
import java.util.Stack;

public class Mapper2 {
	
	private ArrayList<Coordinates> knownBalls;
	private Stack<Coordinates> placesToVisit;
	
	public Mapper2() {
		knownBalls = new ArrayList<Coordinates>();
	}
	
	public void recordBallLocation(double x, double y) {
		knownBalls.add(new Coordinates(x, y));
	}
	
	public double getNextX() {
		return placesToVisit.peek().getX();
	}
	
	public double getNextY() {
		return placesToVisit.peek().getY();
	}
	
	public void prepareForNext() {
		placesToVisit.pop();
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
