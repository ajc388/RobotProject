import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

/*
 * This is the Communicator to go on caffeine2
 * this will receive communication initiation
 */
public class B2Comm {
	private DataOutputStream dos;

	public B2Comm(){
		//Constructor
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
		dos = btc.openDataOutputStream();
	}


	public void sendReading(byte US, byte dist){
		//Send which US it's from and the distance
		try {
			dos.writeByte(US);
			dos.writeByte(dist);
		} catch (IOException e) {
		}

	}
}
