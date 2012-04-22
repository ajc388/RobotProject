import lejos.nxt.SensorPort;

public class LineDetector implements Runnable {
    	private LightSensor2 ls;
	private Navigator nav;
	private CageController cageController;
	private boolean onLine;
	
	public LineDetector(LightSensor2 ls, Navigator nav, CageController cageController){
		onLine = false;
		this.nav = nav;
		this.ls = ls;
		this.cageController = cageController;
		ls.loadCalibration();
		ls.setFloodlight(true);
	}
	
	public void setOnLine(boolean onLine) {
		this.onLine = onLine;
	}
    	
	public void run()  {
		//Starts sensing for line.
		while(true)  {
			if(ls.getLightValue() > 88 && !onLine)
			{
				nav.emergencyStop();
			}
		}
		
	}
}
