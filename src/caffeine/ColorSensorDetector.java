import lejos.nxt.addon.ColorSensorHT;

public class ColorSensorDetector {
 
    ColorSensorHT color;
    B2Comm communicator;
    	
    public ColorSensorDetector(ColorSensorHT cs) {  color = cs; }
    
    public boolean isItRed()
    {
    	//Scans ahead of it, returns true if detecting a red ball, otherwise false
    	if(color.getColor().getRed() > color.getColor().getBlue()) { return true;  }
    	else { return false; }
    }
}
