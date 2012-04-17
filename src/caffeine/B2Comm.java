import java.io.DataInputStream;
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
	private DataInputStream dis;
	private boolean connected;
	
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
		connected = true;
		dos = btc.openDataOutputStream();
		dis = btc.openDataInputStream();
	}

	public boolean waitingForCommand( ) {
	    byte commandReceived = (byte)0;
	    while ( commandReceived == (byte)0 ) {
        	    try {
        		commandReceived = dis.readByte();
        		LCD.drawString("Command was received", 0 , 1);
        	    } catch (IOException ioe ) {
        		LCD.clear();
        		LCD.drawString("Did not receive a command", 0 , 1);
        	    }
	    }
            return commandReceived == (byte)1;
	}
	
	public void sendColorData(byte redBall) {
	    try {
		dos.writeByte(redBall);
		LCD.drawString( "Redball data sent: " + redBall , 0 , 1);
	    } catch (IOException ioe ) {
		LCD.drawString( "Failed to send data" , 0 , 1);
	    }
	}
	
	//Getters
	public boolean isConnected() {
	    return connected;
	}
}
