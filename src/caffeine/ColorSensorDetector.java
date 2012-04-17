import lejos.nxt.LCD;
import lejos.nxt.addon.ColorSensorHT;

public class ColorSensorDetector {
 
    ColorSensorHT color;
    B2Comm communicator;
    	
    public ColorSensorDetector(ColorSensorHT cs) {  color = cs; }
    
    public byte isItRed()
    {
    	//Scans ahead of it, returns true if detecting a red ball, otherwise false
    	LCD.drawString("R:" + color.getColor().getRed() + " B:" + color.getColor().getBlue(), 0, 2);
    	if(color.getColor().getRed() > color.getColor().getBlue()) { return 1;  }
    	else { return 0; }
    }
}
