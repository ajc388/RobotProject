import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class Robot2 {
	private static UltrasonicSensor LeftUS, RightUS, FrontUS;
	private static byte d1, d2, d3;
	private static final int THRESHOLD = 50;
	private static B2Comm comm;
	public static void main(String[] args) {
		comm = new B2Comm();
		LeftUS = new UltrasonicSensor(SensorPort.S1);
		RightUS = new UltrasonicSensor(SensorPort.S3);
		FrontUS = new UltrasonicSensor(SensorPort.S2);
		startScanning();
	}
	public static void startScanning(){
		while(true){//TODO: Make that not be while true
			d1 = (byte) LeftUS.getDistance();
			if (d1<THRESHOLD){
				comm.sendReading((byte)1, d1);
			}
			d2 = (byte) FrontUS.getDistance();
			if (d2<THRESHOLD){
				comm.sendReading((byte)2, d2);
			}
			d3 = (byte) RightUS.getDistance();
			if (d3<THRESHOLD){
				comm.sendReading((byte)3, d3);
			}
		}
	}
}
