package caffeine;

import lejos.nxt.SensorPort;

public class LineDetector {
	private LightSensor2 ls;
	public LineDetector(){
		ls = new LightSensor2(SensorPort.S1);
		//This should start the thread that scans and calls notif()
	}
	private void notif(){
		//Notify the mapper
		//Notify the Navigator over BT
	}
}
