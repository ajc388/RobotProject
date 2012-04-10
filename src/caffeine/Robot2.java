package caffeine;


public class Robot2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		B2Comm comm = new B2Comm();
		Mapper map = new Mapper(comm);
		LineDetector ld = new LineDetector(comm);
		B2.setMapper(map);
	}

}
