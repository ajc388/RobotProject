import lejos.nxt.SensorPort;

public class LineDetector implements Runnable {
    	private LightSensor2 ls;
	private Navigator nav;
	private Mapper map;
	
    	public LineDetector(Navigator nav, Mapper map){
		this.map = map;
		this.nav = nav;
		ls = new LightSensor2(SensorPort.S1);
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
			if(ls.getLightValue() <= ls.getHigh()-20 && ls.getLightValue() > ls.getLow()+40)
			{
				notifyMapper();
			}
		}
		
	}
}