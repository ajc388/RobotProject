import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

/*
 * This is the Communicator to go on caffeine2
 * this will receive communication initiation
 */
public class B2Comm implements Runnable{
	private Navigator nav;
	private DataInputStream dis;
	private DataOutputStream dos;
	private boolean connected;
	private ObjectDetector od;

	public B2Comm(){
		//Constructor
		connected = false;
		String name = "caffeine";
		LCD.drawString("Connecting...", 0, 0);
		RemoteDevice btrd = Bluetooth.getKnownDevice(name);
		if (btrd == null) {
			LCD.clear();
			LCD.drawString("No such device", 0, 0);
			Button.waitForPress();
			System.exit(1);
		}

		BTConnection btc = Bluetooth.connect(btrd);

		if (btc == null) {
			LCD.clear();
			LCD.drawString("Connect fail", 0, 0);
			Button.waitForPress();
			System.exit(1);
		}

		LCD.clear();
		LCD.drawString("Connected", 0, 0);
		connected = true;
		dis = btc.openDataInputStream();
		dos = btc.openDataOutputStream();
	}

	public void setNavigator(Navigator nav){
		this.nav = nav;
	}

	public void setObjectDetector(ObjectDetector od){
		this.od = od;
	}

	public void notifyInPosition(){
		//Notifies that it is in position for scanning a ball
		//Code 7
		try {
			dos.writeByte(7);
		} catch (IOException e) {
		}
	}
	public void notifyReadyToLower(){
		//Notifies that it is in position to lower the cage for capturing
		//Code 8
		try {
			dos.writeByte(8);
		} catch (IOException e) {
		}

	}
	public void requestNewNav(){
		//Asks the mapper to send a new navigation command
		//Code 9
		try {
			dos.writeByte(9);
		} catch (IOException e) {
		}
	}
	public void sendCoordinates(){
		//Sends coordinates to the other brick
		//Code 10 followed by 2 longs
		try{
			dos.writeByte(10);
			dos.writeLong(nav.getX());
			dos.writeLong(nav.getY());
		} catch (IOException e) {
		}
	}
	public void run() {
		while(connected){
			try {
				byte b = dis.readByte();
				switch (b) {
				case 1: nav.emergencyStop(); break;
				case 2: od.notifyBallColor(false); break;
				case 3: od.notifyBallColor(true); break; //Check what else needs to happen here
				case 4: nav.navigateTo(dis.readLong(), dis.readLong()); break;
				case 5: nav.travel(6); break;
				case 6: sendCoordinates(); break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
}
