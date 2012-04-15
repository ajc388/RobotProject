import java.io.DataInputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

/*
 * This is the Communicator to go on caffeine1
 * this will initiate bluetooth communications
 * 
 * 

Request for current coordinates (code)
 */
public class B1Comm implements Runnable {
	private DataInputStream dis;
	private boolean connected;
	private ObjectDetector od;

	public B1Comm(){
		connected = false;
		LCD.drawString("Waiting...",0,0);
		NXTConnection connection = Bluetooth.waitForConnection(); 
		dis = connection.openDataInputStream();
		connected = true;
	}
	
	public void setObjectDetector(ObjectDetector od){
		//Sets the object detector reference and starts the scanning
		this.od = od;
		new Thread(this).start();
	}

	public void run() {
		while(connected){
			try {
				byte b = dis.readByte();
				int dist = (int) dis.readByte();
				switch (b) {
				case 1: od.processLeft(dist); break;
				case 2: od.processFront(dist); break;
				case 3: od.processRight(dist); break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
}
