
import java.net.ServerSocket;

public class FalsePositive16 {

	protected ServerSocket serverSocket = null;
	private boolean running;

	/**
	 * Tomcat In file: JIoEndpoint.java line 434
	 * 
	 * @throws Exception
	 */
	public void unbind() throws Exception {
		if (running) {
			stop();
		}
		if (serverSocket != null) {
			try {
				if (serverSocket != null)
					serverSocket.close();
			} catch (Exception e) {
				// log.error(sm.getString("endpoint.err.close"), e);
			}
			serverSocket = null;
		}
		recycle();
	}

	private void recycle() {
		// TODO Auto-generated method stub

	}

	private void stop() {
		// TODO Auto-generated method stub

	}

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
 * In file: AuthenticatorBase.java
	line 563	
 */
	public void bar(Object[] constraints) {
		// Since authenticate modifies the response on failure,
		// we have to check for allow-from-all first.
		boolean authRequired;
		if (constraints == null) {
			authRequired = false;
		} else {
			authRequired = true;
			for (int i = 0; i < constraints.length && authRequired; i++) {
				if (!getAuthConstraint(constraints[i])) {
					authRequired = false;
					break;
				} else if (!getAllRoles(constraints[i])
						&& !getAuthenticatedUsers(constraints[i])) {
					String[] roles = findAuthRoles(constraints[i]);
					if (roles == null || roles.length == 0) {
						authRequired = false;
						break;
					}
				}
			}
		}

		if (!authRequired) {
		}

	}

	private String[] findAuthRoles(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean getAuthenticatedUsers(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean getAllRoles(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean getAuthConstraint(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

}