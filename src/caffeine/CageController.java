import lejos.nxt.NXTRegulatedMotor;

/*
 * Controls the cage, on brick caffeine
 */
public class CageController
{
	NXTRegulatedMotor cagemotor;

	public CageController(NXTRegulatedMotor cagemotor)
	{
		this.cagemotor = cagemotor;
		this.cagemotor.setSpeed(25);
		levelCage();
	}
	public void raiseCage()
	{
		cagemotor.rotate(-40); //Raises cage, notifies necessary objects when it's raised
	}
	public void lowerCage()
	{
		cagemotor.rotate(40); //Lowers cage, notifies necessary objects when it's lowered
	}
	public void levelCage()
	{
		cagemotor.rotate(-12); //Levels cage parallel to the ground
		cagemotor.stop();
	}
}
