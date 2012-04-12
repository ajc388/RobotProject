import lejos.nxt.addon.ColorSensorHT;

public class ObjectVerifier
{
	ColorSensorHT color;
	
	public ObjectVerifier(ColorSensorHT cs, B1Comm comm)
	{
		color = cs;
	}
	public boolean isItRed()
	{
		//Scans ahead of it, returns true if detecting a red ball, otherwise false
		if(color.getColor().getRed() > color.getColor().getBlue())
		{
			return true;
		}
		else return false;
	}
}
