

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.List;

public class CornerCases01 {

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
				//the else case is unreachable
				//however, this is most likeley 
				//meant to be run in a parallel 
				//setting
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
 * In file: AuthenticatorBase.java
	line 563	
	
	the problem is that the condition "&& authRequired" can
	never fail because any execution that sets authRequired to false
	automatically leaves the loop before reaching this check. 
 */
	public void bar(Object[] constraints) {
		boolean authRequired;
		authRequired = true;
		for (int i = 0; i < constraints.length && authRequired; i++) {
			if (!getAuthConstraint(constraints[i])) {
				authRequired = false;
				break;
			} 
		}
	}

	private boolean getAuthConstraint(Object o) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	//GF2nPolynomialField
	//line 537
	/*
	 * the loop can only be left through the return
	 * statement. On any other path through the loop
	 * done remains false.
	 */
    private boolean testRandom()
    {
        int l;
        boolean done = false;

        l = 0;
        while (!done)
        {
            l++;
            if (isIrreducible())
            {
                done = true;
                return done;
            }
        }

        return done;
    }


	private boolean isIrreducible() {
		// TODO Auto-generated method stub
		return false;
	}		
	
	
    private boolean buffered;
	public void fp05(PrintWriter writer) {
        // Log this message
        try {
            synchronized(this) {
                if (writer != null) {
                    writeTo(writer);
                    writer.println("");
                    if (!buffered) {
                        writer.flush();
                    }
                }
            }
        } catch (IOException ioe) {
        	System.err.println(); // this is in fact
        	//unreachabel because the synchronized block
        	//catches Any exception.
        }

	}

	private void writeTo(PrintWriter writer) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	
	
    public URL getResource(String path) throws MalformedURLException {

        if (!path.startsWith("/"))
            throw new MalformedURLException("Path '" + path +
                                            "' does not start with '/'");
        URL url = new URL( path.substring(1));
        try (InputStream is = url.openStream()) {
        } catch (Throwable t) {
        	//the catch block is unreachable
        	//because the try with resource wraps everything into
        	//a catch-Any block, so Throwable cannot be caught here.
            handleThrowable(t);
            url = null;
        }
        return url;
    }

	private void handleThrowable(Throwable t) {
		// TODO Auto-generated method stub	
	}
	
	
}
