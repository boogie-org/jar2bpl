public class TruePositive06 {

	public int doubleLoop1(int x, int y) {  
        int ret=0;
        for (int i=0; i<x; i++) 
        {
            for (int j=0; j<y; i++) {
                ret++;
            }
        }
 		return ret;
	}  

	
	public int doubleLoop2() {  
        int ret=0;
        for (int i=0; i<10; i++) 
        {
            for (int j=0; j<10; i++) {
                ret++;
            }
        }
 		return ret;
	}  

	
}
