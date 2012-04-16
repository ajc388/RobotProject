import lejos.nxt.LCD;
/*
 * This is the mapper, which resides on caffeine
 * The mapper needs to:
 * Tell navigator where to go next
 * Plot path back to start when red ball is captured
 */
public class Mapper {
	//private B1Comm commRef;
	private static enum mapObject {
		UNVISITED_LOCATION, SCANNED_LOCATION, BORDER, STARTING_POINT, STARTING_BORDER, BLUE_BALL, RED_BALL
	}

	private static mapObject[][] map;
	private static final int mapSize = 200; // Units
	private static final int mapIncrement = 5; // Units in centimeters

	// Robot position
	private double xRobotPosition;
	private double yRobotPosition;
	private final double xStartPosition;
	private final double yStartPosition;

	// Builds the map object based upon the constants mapsize
	// issues order to navigate forward
	public Mapper() {
	//	commRef = pComm;
		map = new mapObject[mapSize][mapSize];

		// Instantiate every point on the field as an empty object
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				map[i][j] = mapObject.UNVISITED_LOCATION;
			}
		}

		// At 250 250 create the starting location
		xStartPosition = mapSize / 2;
		yStartPosition = mapSize / 2;
		map[(int) xStartPosition][(int) yStartPosition] = mapObject.STARTING_POINT;

		// create surrounding field of 10"x12"
		int width = (int) 25.4 / mapIncrement;
		int height = (int) 30.5 / mapIncrement;

		// builds starting location rows
		for (int i = 0; i < width; i++) {
			map[((mapSize / 2) - (width / 2)) + i][((mapSize / 2) - (height / 2))] = mapObject.STARTING_BORDER;
			map[((mapSize / 2) - (width / 2)) + i][((mapSize / 2) + (height / 2))] = mapObject.STARTING_BORDER;
		}
		// builds starting location columns
		for (int j = 0; j < height; j++) {
			map[((mapSize / 2) - (width / 2))][((mapSize / 2) - (height / 2))
					+ j] = mapObject.STARTING_BORDER;
			map[((mapSize / 2) + (width / 2))][((mapSize / 2) - (height / 2))
					+ j] = mapObject.STARTING_BORDER;
		}
		// Start by asking the motor to move forward 20cm
	//	commRef.transmitNavCommand(goToX(xRobotPosition),
	//			goToY(yRobotPosition + 5));
	}

	// ==============================================\\
	// MOVEMENT COMMANDS \\
	// ==============================================\\
	// GoTo Command
	public double goToX(int x) {
		return (double)convertToNavCoordinates(x);
	}

	public double goToY(int y) {
		return (double)convertToNavCoordinates(y);
	}

	/* Tell the robot to go back home at 0,0 */
	public void returnHome() {
	//	commRef.transmitNavCommand(0, 0);
	}

	// Exploration algorithm
	public void exploreField(int searchIncrement) {
		double x = getXRobotPosition();
		double y = getYRobotPosition();

		// Establish the lower bound x and y
		// and the upper bound x and y
		int lowerBoundX = (int) (x - (searchIncrement / 2));
		int lowerBoundY = (int) (y - (searchIncrement / 2));
		int upperBoundX = (int) (x + (searchIncrement / 2));
		int upperBoundY = (int) (y + (searchIncrement / 2));

		// Base case
		// If the boundaries are all beyond the array
		// Then there is no unexplored areas to be found.
		if ((lowerBoundX <= 0) && (lowerBoundY < 0) && (upperBoundX >= mapSize)
				&& (upperBoundY > mapSize)) {
			// Print robots actions
			LCD.clear();
			LCD.drawString("No unexplored", 0, 3);
			LCD.drawString(" areas found.", 0, 4);
			LCD.drawString("Going home.", 0, 5);
			LCD.refresh();
			// Return home
			returnHome();
		} else {
			// Makes sure there will be no stack
			// overflow exceptions
			if (lowerBoundX < 0) {
				lowerBoundX = 0;
			}
			if (lowerBoundY < 0) {
				lowerBoundY = 0;
			}
			// Scan the entire section for unvisited elements in the array
			for (int i = lowerBoundX; i < searchIncrement && i < mapSize; i++) {
				for (int j = lowerBoundY; j < searchIncrement && j < mapSize; j++) {
					if (map[i][j] == mapObject.UNVISITED_LOCATION) {
						// send data
						//commRef.transmitNavCommand(goToX(i), goToY(j));
						break;
					}
				}
			}
			// If nothing is found, call the function again
			// increase the search range and check again
			exploreField(searchIncrement * 2);
		}
	}

	// UPDATE MAP COMMANDS
	// Should also mark the position of scanned regions on the field
	public void updatePosition(double x, double y) {
		int sensorRange = 40; // in centimeters
		// Update position coordinates
		xRobotPosition = x;
		yRobotPosition = y;

		// Get adjusted map coordinates
		int mapX = (int) convertToMapCoordinates(x);
		int mapY = (int) convertToMapCoordinates(y);

		// Loop positions
		int leftPosition = (int) (mapX - (sensorRange / mapIncrement));
		int rightPosition = (int) (mapX + (sensorRange / mapIncrement));
		int topPosition = (int) (mapY - (sensorRange / mapIncrement));

		// Loop through coordinates
		for (int i = leftPosition; i < rightPosition; i++) {
			for (int j = topPosition; j >= y; j--) {
				if (map[i][j] == mapObject.UNVISITED_LOCATION) {
					map[i][j] = mapObject.SCANNED_LOCATION;
				}
			}
		}
	}

	// Also updates position
	public void updateBoundary(double x, double y) {
		xRobotPosition = x;
		yRobotPosition = y;
		map[convertToMapCoordinates(x)][convertToMapCoordinates(y)] = mapObject.BORDER;
	}

	// robot position does not change
	public void updateBlueBall(int x, int y) {
		map[convertToMapCoordinates(x)][convertToMapCoordinates(y)] = mapObject.BLUE_BALL;
	}

	// Converts to valid coordinates to and from
	// The navigator coordinate system
	private int convertToMapCoordinates(double coordinate) {
		return (int) ((coordinate / mapIncrement) + (mapSize / 2));
	}

	private double convertToNavCoordinates(int arrayCoordinate) {
		return (double) ((arrayCoordinate - (mapSize / 2)) * mapIncrement);
	}

	// If the location is a border or a blue ball then it is not passable
	// otherwise the location is good
	public boolean isPassable(int x, int y) {
		if (map[x][y] == mapObject.BORDER) {
			return false;
		} else if (map[(int) x][(int) y] == mapObject.BLUE_BALL) {
			return false;
		} else {
			return true;
		}
	}

	// GETTERS
	public double getXRobotPosition() {return xRobotPosition;}
	public double getYRobotPosition() {return yRobotPosition;}
	public double getXStartPosition() {return xStartPosition;}
	public double getYStartPosition() {return yStartPosition;}

	// Printout map
	public String toString() {
		String mapPrintout = "Map Status\n";
		for (int i = 0; i < mapSize; i++) {
			for (int j = 0; j < mapSize; j++) {
				mapPrintout = " " + map[i][j];
			}
			mapPrintout = "\n";
		}
		return mapPrintout;
	}
}