package caffeine;

/*
 * This is the mapper, which resides on caffeine
 * The mapper needs to:
 * Tell navigator where to go next
 * Plot path back to start when red ball is captured
 * 
 */
public class Mapper {
	public Mapper(){
		//Probably needs reference to communicator
	}
	public void goHome(){
		//Plots path to start, and sends commands to navigator
	}
	public void getNewNav(){
		//Determines the next point that needs to be explored, tells the navigator to explore it
	}
	public void notifyBlueBall(long x, long y){
		//Tells the mapper a blue ball was found at coordinate (x, y)
	}
}
