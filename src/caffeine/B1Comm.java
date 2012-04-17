import java.io.DataInputStream;
import java.io.DataOutputStream;
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
public class B1Comm {
	private DataInputStream dis;
	private DataOutputStream dos;
	private boolean connected;

	public B1Comm() {
		connected = false;
		LCD.drawString("Waiting...", 0, 0);
		NXTConnection connection = Bluetooth.waitForConnection();
		dis = connection.openDataInputStream();
		dos = connection.openDataOutputStream();
		connected = true;
	}

	// Will return a boolean of a detected object
	// If it is red it will return true
	// If it is blue it will return false
	// To accomplish this it asks the other brick to use its color sensor
	// To return its boolean color value
	public boolean getColorSensorData() {
		// Send the request to the B2 communicator
		byte colorControl = (byte)2;
	    	boolean isRed = false;
		boolean request = false;
		try {
			request = true;
			dos.writeByte((byte)1);
			LCD.clear();
			LCD.drawString("Sent object request", 0, 1);
		} catch (IOException ioe) {
			LCD.clear();
			LCD.drawString("Failed to send request. ", 0, 1);
			LCD.drawString("Color Sensor ", 0, 2);
		}
		
		// Receive boolean response from communicator
		while ( colorControl != (byte)2 ) {
        		try {
        		    	colorControl = dis.readByte();
        		    	if ( colorControl == (byte)1) {  isRed = true;  } 
        		    	else if ( colorControl == (byte)0 ) { isRed = false; }
        		    	//Print received data
        			LCD.clear();
        			LCD.drawString("Received colorByte: " + isRed, 0, 1);
        			// Return boolean
        		} catch (IOException e) {
        			LCD.clear();
        			LCD.drawString("Failed to receive color data", 0, 1);
        			e.printStackTrace();
        		}
        	}
		return isRed;
	}
}
