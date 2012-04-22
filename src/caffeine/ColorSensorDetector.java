import lejos.nxt.LCD;
import lejos.nxt.ColorSensor.Color;
import lejos.nxt.addon.ColorSensorHT;

public class ColorSensorDetector {
 
    ColorSensorHT color;
    B2Comm communicator;
    	
    public ColorSensorDetector(ColorSensorHT cs) {  color = cs; }
    
    public byte isItRed()
    {
    	//Scans ahead of it, returns true if detecting a red ball, otherwise false
    	Color c = (Color) color.getColor();
    	LCD.drawString("R:" + c.getRed() + " B:" + c.getBlue(), 0, 2);
    	if(c.getRed() > c.getBlue()) { return 1;  }
    	else { return 0; }
    }
}
