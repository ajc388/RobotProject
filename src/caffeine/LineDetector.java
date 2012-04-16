import lejos.nxt.SensorPort;

public class LineDetector implements Runnable {
    	private LightSensor2 ls;
	private Navigator nav;
	
	public LineDetector(LightSensor2 ls, Navigator nav){
		this.nav = nav;
		this.ls = ls;
		ls.loadCalibration();
		ls.setFloodlight(true);
		new Thread(this).start();
	}
    	
	public void run()  {
		//Starts sensing for line.
		while(true)  {
			if(ls.getLightValue() > 88)
			{
				nav.emergencyStop();
			    try {
				nav.rotate(180);
				nav.travel(10);
				Thread.sleep(1000);
			    } catch (InterruptedException e) {
				nav.travel(20);
			    }
			}
		}
		
	}
}
