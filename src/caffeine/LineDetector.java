import lejos.nxt.SensorPort;

public class LineDetector implements Runnable {
    	private LightSensor2 ls;
	private Navigator nav;
	private Mapper map;
	
	public LineDetector( ) {
	    
	}
	
    	public LineDetector(LightSensor2 ls, Navigator nav, Mapper map){
		this.map = map;
		this.nav = nav;
		this.ls = ls;
		ls.loadCalibration();
		ls.setFloodlight(true);
		new Thread(this).start();
	}
    	
	public void notifyMapper()
	{
		//Notify the Mapper
		map.updateBoundary(nav.getX(), nav.getY());
		//Notify the Navigator
		nav.emergencyStop();
		nav.rotate(180);
		nav.travel(5);
		map.updatePosition(nav.getX(), nav.getY() );
	}
	
	public void run()
	{
		//Starts sensing for line.
		while(true)
		{
			if(ls.getLightValue() > 80)
			{
				notifyMapper();
			}
		}
		
	}
}