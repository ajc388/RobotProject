import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.ColorSensorHT;

public class ColorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ColorSensorHT cs = new ColorSensorHT(SensorPort.S1);
		while(true)
		{
			LCD.drawString("R:" + cs.getColor().getRed(), 0, 2);
			LCD.drawString("B:" + cs.getColor().getBlue(), 0, 3);

		}

	}

}
