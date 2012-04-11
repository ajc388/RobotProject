package caffeine;

import java.util.Stack;

import lejos.nxt.LCD;

/*
 * This is the mapper, which resides on caffeine
 * The mapper needs to:
 * Tell navigator where to go next
 * Plot path back to start when red ball is captured
 * 
 */
public class Mapper {
	private static enum mapObject {UNVISITED_LOCATION, VISITED_LOCATION, BORDER,
	    				STARTING_POINT, STARTING_BORDER, BLUE_BALL, RED_BALL }
	
	private static mapObject[][] map;
	//Arbitrarily defined unit
	//500*5 = 2500cm 
	private static final int mapSize = 500;
	//Units in centimeters
	private static final int mapIncrement = 5;
	private B1Comm commRef;
	
	//Robot position
	private double xRobotPosition;
	private double yRobotPosition;
	private final double xStartPosition;
	private final double yStartPosition;
	
	//Object Class
	Stack superStack = new Stack();
	
	
	//Constructor
	//Builds the map object based upon the constants mapsize
	//calculate the origin
	//build the border around the origin
    	public Mapper(B1Comm pComm ) {
    	    commRef = pComm;
    	    map = new mapObject[mapSize][mapSize];
    	    
    	    //Instantiate every point on the field as an empty object
    	    for ( int i = 0; i < mapSize; i++ ) {
    		for ( int j = 0 ; j < mapSize; j++ ) {
    		    map[i][j] = mapObject.UNVISITED_LOCATION;
    		}
    	    } 	    
    	    
    	    //At 250 250 create the starting location
    	    xStartPosition = mapSize/2;
    	    yStartPosition = mapSize/2;
    	    map[(int) xStartPosition][(int) yStartPosition] = mapObject.STARTING_POINT;
	    	
    	    //create surrounding field of 10"x12"
    	    int width = (int)25.4/mapIncrement;
    	    int height = (int)30.5/mapIncrement;
    	    
    	    //builds starting location rows
    	    for ( int i = 0 ; i < width; i++ ) {
    		map[((mapSize/2)-(width/2))+i ][((mapSize/2)-(height/2))] = mapObject.STARTING_BORDER;  
    		map[((mapSize/2)-(width/2))+i ][((mapSize/2)+(height/2))] = mapObject.STARTING_BORDER; 
    	    }
    	    //builds starting location columns
    	    for ( int j = 0 ; j < height; j++ ) {
    		map[((mapSize/2)-(width/2)) ][((mapSize/2)-(height/2))+j] = mapObject.STARTING_BORDER;  
    		map[((mapSize/2)+(width/2)) ][((mapSize/2)-(height/2))+j] = mapObject.STARTING_BORDER;
    	    }
	
    	}
    	
    	
    	//The events to update the map are outsourced to the navigator and light detector
    	//The specific coordinates of the update are tracked by the navigator
    	//Since that data is relient on how the navigator moves.
    	//This method just allows the person to give the current coordinates to the map
    	//along with the object needed for the update
    	public void updatePosition(double x, double y) {
    	    xRobotPosition = x;
    	    yRobotPosition = y;
    	    map[convertToMapCoordinates(x)][convertToMapCoordinates(y)] = mapObject.VISITED_LOCATION;    
    	}
    	
    	//Also updates position
    	public void updateBoundary(double x, double y ) {
    	    xRobotPosition = x;
    	    yRobotPosition = y;
    	    map[convertToMapCoordinates(x)][convertToMapCoordinates(y)] = mapObject.BORDER;
    	}
    	
    	//robot position does not change
    	public void updateBlueBall(double x, double y ) {
    	    map[convertToMapCoordinates(x)][convertToMapCoordinates(y)] = mapObject.BLUE_BALL;  	    
    	}
    	
    	
    	//This works under the assumption that the coordinates tracked by the navigator
    	//are measured in centimeters
    	//It also assumes that the starting location for the purposes of those coordinates is
    	//set to 0, however the starting location on the map is 250,250
    	//so it must be mapped to match this assuming that the map coordinates go
    	//in intervals of 5cm
    	private int convertToMapCoordinates( double coordinate ) {
    	    return (int)((coordinate + (mapSize*mapIncrement) ) / mapIncrement); 
    	}
    	
    	
    	/*Returns a queue of suggested points to return
    	 * back home
    	 */
	public void returnHome() {
	   
	   int xRobotPosition = (int) getXStartPosition();
	   int yRobotoPosition = (int) getYStartPosition();
	    
	}
	
	public void exploreField(int searchIncrement) {
	    double x = getXRobotPosition();
	    double y = getYRobotPosition();
	    
	    //Establish the lower bound x and y
	    //and the upper bound x and y
	    int lowerBoundX = (int)(x - (searchIncrement/2));
	    int lowerBoundY = (int)(y - (searchIncrement/2));
	    int upperBoundX = (int)(x + (searchIncrement/2));
	    int upperBoundY = (int)(y + (searchIncrement/2));
	    
	    //Base case
	    //If the boundaries are all beyond the array
	    //Then there is no unexplored areas to be found.
	    if (   (lowerBoundX <= 0) && (lowerBoundY < 0)
		 && (upperBoundX >= 500) && (upperBoundY > 500 )) {
		//Print robots actions
		LCD.clear();
		LCD.drawString("No unexplored", 0, 3);
		LCD.drawString(" areas found.", 0, 4);
		LCD.drawString("Going home.", 0 , 5);
		LCD.refresh();
		
		//Return home
		returnHome();
		
	    } else {
		//Makes sure there will be no stack
		//overflow exceptions
    	    	if ( lowerBoundX < 0 ) {
    	    	    lowerBoundX = 0;
    	    	}
    	    	if ( lowerBoundY < 0 ) {
    	    	    lowerBoundY = 0;
    	    	}
	    
    	    	//Scan the entire section for unvisited elements in the array
    	    	for ( int i = lowerBoundX ; i < searchIncrement && i < mapSize; i++ ) {
    	    	    for ( int j = lowerBoundY ; j < searchIncrement && j < mapSize; j++ ) {
		
    	    		if ( map[i][j] == mapObject.UNVISITED_LOCATION ) {
    	    		    //get points
    	    		    //pass data to navigator
    	    		    break;
    	    		}
		
    	    	    }
    	    	}
    	    	//If nothing is found, call the function again
    	    	//And increase the search range
	        exploreField(searchIncrement*2);
	    }
	}
	
	private void suggestPoint() {
	    
	    	    
	    
	}
	
	//If the location is a border or a blue ball then it is not passable
	//otherwise the location is good
	public boolean isPassable(double x, double y) {
	    if ( map[(int)x][(int)y] == mapObject.BORDER  ) {	return false;   } 
	    else if ( map[(int)x][(int)y] == mapObject.BLUE_BALL) {  return false; } 
	    else { return true; }
	}
	
	
	public void getNewNav() {
		//Determines the next point that needs to be explored, tells the navigator to explore it
	}
	
	
	public String toString() {	    
	    String mapPrintout = "Map Status\n";
	    for (int i = 0; i < mapSize; i++) {
		for (int j = 0; j < mapSize; j++ ) {
		   mapPrintout = " " + map[i][j];  
		}
		mapPrintout = "\n";
	    }
	    return mapPrintout;
	    
	}
	
	
	
	public double getXRobotPosition() { return xRobotPosition;  }
	public double getYRobotPosition() { return yRobotPosition;  }
	public double getXStartPosition() { return xStartPosition;  }
	public double getYStartPosition() { return yStartPosition;  }
}
