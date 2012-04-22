import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.addon.ColorSensorHT;

public class Robot2 {
	// Class Variables
	private static ColorSensorDetector colorDetector;
	private static B2Comm comm;
	private static ColorSensorHT colorSense;

	public static void main(String[] args) {
		// Instantiate sensor
		colorSense = new ColorSensorHT(SensorPort.S1);

		// Instantiate objects
		colorDetector = new ColorSensorDetector(colorSense);
		comm = new B2Comm();

		boolean receivedCommand = false;
		while ( comm.isConnected()) {
			//LCD.drawString("Command received =  " + receivedCommand , 0 , 5);
			Sound.beepSequenceUp();
			if ( receivedCommand = comm.waitingForCommand() ) {
				byte isRed = colorDetector.isItRed();
				comm.sendColorData(isRed);
			}
		}
		LCD.drawString("Exited loop.", 0, 2);
	}

	/*
	 * public static void startScanning(){ while(true){ d1 = (byte)
	 * LeftUS.getDistance(); d2 = (byte) FrontUS.getDistance(); d3 = (byte)
	 * RightUS.getDistance();
	 * 
	 * if (d1<THRESHOLD){ comm.sendReading((byte)1, d1); } else if
	 * (d2<THRESHOLD){ comm.sendReading((byte)2, d2); } else if (d3<THRESHOLD){
	 * comm.sendReading((byte)3, d3); } } }
	 */
}
