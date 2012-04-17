import lejos.nxt.SensorPort;

public class LineDetector implements Runnable {
    	private LightSensor2 ls;
	private Navigator nav;
	
	public LineDetector(LightSensor2 ls, Navigator nav){
		this.nav = nav;
		this.ls = ls;
		ls.loadCalibration();
		ls.setFloodlight(true);
	}
    	
	public void run()  {
		//Starts sensing for line.
		while(true)  {
			if(ls.getLightValue() > 88)
			{
				nav.emergencyStop();
				nav.rotate(180);
				nav.travel(10);
				nav.waitForTravel();
			}
		}
		
	}
}
