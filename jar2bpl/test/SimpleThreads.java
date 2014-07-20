import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;



public class SimpleThreads {

//	public int shared = 0;
//	
//	public class MyThread implements Runnable {
//		SimpleThreads me;
//		public MyThread(SimpleThreads me) {
//			this.me = me;
//		}
//		public void run() {
//			this.me.shared ++;
//			System.err.println(this.me.shared);
//		}
//	}
//	
//	public void startTheMess() {
//    	Thread t1 = new Thread(new MyThread(this));
//    	Thread t2 = new Thread(new MyThread(this));
//    	t1.start();
//    	t2.start();
//    	while (t1.isAlive() || t2.isAlive()) {
//    		this.shared--;
//    		System.err.println("waiting");
//    	}		
//	}
//	
//    public static void main(String args[])
//        throws InterruptedException {
//
//    	SimpleThreads st = new SimpleThreads();
//    	st.startTheMess();
//    }

	private List<Integer> list;
	private static final int SIZE = 10;
	
	public void main(String[] args) {

		PrintWriter out = null;

		try {
//		    for (Integer x : list) {
//		        x.toString();
//		    }
//		    list.size();
		    for (Integer x : list) {
		        x.toString();
		    }
		} catch (Exception e) {
			System.err.println(e.toString());
		}	
	}
	
}
