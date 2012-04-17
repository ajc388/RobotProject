import lejos.nxt.addon.ColorSensorHT;

public class ObjectVerifier
{
	ColorSensorHT color;
	B2Comm communicator;
	
	public ObjectVerifier(ColorSensorHT cs, B2Comm comm)
	{	
	    color = cs;
	    communicator = new B2Comm();
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
