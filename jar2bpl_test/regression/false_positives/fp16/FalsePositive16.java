
import java.net.ServerSocket;
import java.util.List;

public class FalsePositive16 {

	
	/*
	 * ====================================================
	 * 
	 * In file: PooledMultiSender.java line 50
	 */
	public void sendMessage(Object[] destination, String msg) throws Exception {
		Object sender = null;
		try {
			sender = getSender();
			if (sender == null) {
				Exception cx = new Exception(
						"Unable to retrieve a data sender, time out(  ms) error.");
				for (int i = 0; i < destination.length; i++)
					addFaultyMember(destination[i], new NullPointerException(
							"Unable to retrieve a sender from the sender pool"));
				throw cx;
			} else {
				sender.hashCode();
			}
			sender.toString();
		} finally {
			if (sender != null)
				returnSender(sender);
		}
	}

	private void returnSender(Object sender) {
		// TODO Auto-generated method stub

	}

	private void addFaultyMember(Object object,
			NullPointerException nullPointerException) {
		// TODO Auto-generated method stub

	}

	private Object getSender() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * ====================================================
	 * 
	 * In file: ApplicationDispatcher.java line 684
	 */
	public void foo(long available) {
		if ((available > 0L) && (available < Long.MAX_VALUE)) {
			available = 1L;
		}
	}

	/*
	 * In file: BooleanConditionSimplification.java line 160 line 167 line 171
	 */
	public void decideCondition(int truthValue, boolean notEqual) {

		// find out whether we are dealing with a false or true
		// decide and return
		if (notEqual && truthValue == 0) { // A != false -->A
			return;
		} else if (notEqual && truthValue == 1) {// A != true --> !A

			return;
		} else if (!notEqual && truthValue == 0) {// A == false --> !A
			return;
		} else if (!notEqual && truthValue == 1) {// A == true --> A
			return;
		} else
			throw new RuntimeException();
	}


	public int decisions(boolean lowercheck, boolean uppercheck) {
        if (lowercheck && uppercheck) return 0;
        if (lowercheck && !uppercheck) return 1;
        else if (!lowercheck && uppercheck) return 2;
        else return 3;
	}


}