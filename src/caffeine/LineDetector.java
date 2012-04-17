import lejos.nxt.SensorPort;

public class LineDetector implements Runnable {
    	private LightSensor2 ls;
	private Navigator nav;
	private boolean hasBall;
	private CageController cageController;
	
	public void setHasBall() {
		hasBall = true;
	}
	
	public LineDetector(LightSensor2 ls, Navigator nav, CageController cageController){
		this.nav = nav;
		this.ls = ls;
		this.cageController = cageController;
		ls.loadCalibration();
		ls.setFloodlight(true);
	}
    	
	public void run()  {
		//Starts sensing for line.
		while(true)  {
			if(ls.getLightValue() > 88)
			{
				if (!hasBall) {
				nav.emergencyStop();
				nav.rotate(180);
				nav.travel(10);
				nav.waitForTravel();
				} else {
					nav.emergencyStop();
					cageController.raiseCage();
					nav.travel(-10);
					nav.waitForTravel();
					nav.rotate(720);
				}
			}
		}
		
	}
}
