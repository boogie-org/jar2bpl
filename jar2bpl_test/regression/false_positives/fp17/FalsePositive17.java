
import java.util.Map;

public class FalsePositive17 {
	
	private static final int Divisions = 100;
	public Map<Object, FalsePositive17> new_pts;
	private boolean is_new;
	private FalsePositive17 next;
	
	
	/* In Soot
	 * In file: HeapInsNode.java
	line 522
	 */
	public int count_new_pts_intervals() 
	{
		int ans = 0;
		
		for ( FalsePositive17 im : new_pts.values() ) {
			FalsePositive17[] int_entry = im.getFigures();
			for ( int i = 0; i < FalsePositive17.Divisions; ++i ) {
				FalsePositive17 p = int_entry[i];
				while ( p != null && p.is_new == true ) {
					++ans;
					p = p.next;
				}
			}
		}
		
		return ans;
	}

	private FalsePositive17[] getFigures() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
