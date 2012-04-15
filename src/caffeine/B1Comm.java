package caffeine;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

/*
 * This is the Communicator to go on caffeine
 * this will initiate bluetooth communications
 * 
 * 

Request for current coordinates (code)
 */
public class B1Comm implements Runnable {
	private CageController cage;
	private ObjectVerifier obj;
	private DataOutputStream dos;
	private DataInputStream dis;
	private boolean connected;
	private Mapper map;

	public B1Comm(){
		connected = false;
		LCD.drawString("Waiting...",0,0);
		NXTConnection connection = Bluetooth.waitForConnection(); 
		dos = connection.openDataOutputStream();
		dis = connection.openDataInputStream();
		connected = true;
		new Thread(this).start();
		run();
	}

	public void notifyLineDetected(){
		//Notify that a line was detected to navigator
		//Code: 1
		try {
			dos.writeByte(1);
		} catch (IOException e) {
		}
	}
	public void transmitColor (boolean wasRed){
		//Transmits the detected color
		//Code 2 for red, 3 for blue
		try {
			if (wasRed) dos.writeByte(2);
			else dos.writeByte(3);
		} catch (IOException e) {
		}
	}
	public void transmitNavCommand (long x, long y){
		//Transmits a command from the mapper to the navigator to go to coordinates (x,y)
		//Code 4, followed by 2 longs
		try {
			dos.writeByte(4);
			dos.writeLong(x);
			dos.writeLong(y);
			dos.flush();
		} catch (IOException e) {
		}
	}
	public void notifyReadyForCapture(){
		//Notifies that the cage is up so we can move forward to capture the ball
		//Code 5
		try {
			dos.writeByte(5);
		} catch (IOException e) {
		}
	}
	public void requestCoordinates(){
		
		try {
			dos.writeByte(6);
		} catch (IOException e) {
		}
		//Request the navigator send the current coordinates so the mapper can do what it needs to do
		//Code 6
	}
	public void run() {
		while(connected){
			try {
				byte b = dis.readByte();
				switch (b) {
				case 7: transmitColor(obj.isItRed()); break;
				case 8: cage.lowerCage(); break;
				case 9: map.getNewNav(); break;
				case 10: map.notifyCoordinates(dis.readLong(), dis.readLong()); break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
}
