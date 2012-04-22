import lejos.nxt.LCD;
import lejos.nxt.addon.ColorSensorHT;
import lejos.robotics.Color;

public class ColorSensorDetector {
 
    ColorSensorHT color;
    	
    public ColorSensorDetector(ColorSensorHT cs) {  color = cs; }
    
    public byte isItRed()
    {
    	//Scans ahead of it, returns true if detecting a red ball, otherwise false
    	Color c = color.getColor();
    	LCD.drawString("R:" + c.getRed() + " B:" + c.getBlue(), 0, 2);
    	if(c.getRed() > c.getBlue()) { return 1; }
    	else { return 0; }
    }
}
