

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class FalsePositive18 {

	

	public void resultantMultiThread(int N)
    {
        

        // upper bound for resultant(f, g) = ||f, 2||^deg(g) * ||g, 2||^deg(f) = squaresum(f)^(N/2) * 2^(deg(f)/2) because g(x)=x^N-1
        // see http://jondalon.mathematik.uni-osnabrueck.de/staff/phpages/brunsw/CompAlg.pdf chapter 3
        BigInteger max = squareSum().pow((N + 1) / 2);
        max = max.multiply(BigInteger.valueOf(2).pow((degree() + 1) / 2));
//        BigInteger max2 = max.multiply(BigInteger.valueOf(2));

        // compute resultants modulo prime numbers
//        BigInteger prime = BigInteger.valueOf(10000);
//        BigInteger pProd = Constants.BIGINT_ONE;
        LinkedBlockingQueue<Object> resultantTasks = new LinkedBlockingQueue<Object>();
//        Iterator<BigInteger> primes = BIGINT_PRIMES.iterator();
//        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        // Combine modular resultants to obtain the resultant.
        // For efficiency, first combine all pairs of small resultants to bigger resultants,
        // then combine pairs of those, etc. until only one is left.
        Object overallResultant = null;
        while (!resultantTasks.isEmpty())
        {
            try
            {
                Object modRes1 = resultantTasks.take();
                Object modRes2 = resultantTasks.poll();
                if (modRes2 == null)
                {
                    // modRes1 is the only one left
                    overallResultant = modRes1;
                    break;
                }
                Object newTask = foo();
                resultantTasks.add(newTask);
            }
            catch (Exception e)
            {
                throw new IllegalStateException(e.toString());
            }
        }
 

    }

	private Object foo() {
		// TODO Auto-generated method stub
		return null;
	}

	private BigInteger squareSum() {
		// TODO Auto-generated method stub
		return null;
	}

	private int degree() {
		// TODO Auto-generated method stub
		return 0;
	}

	//------------------------------------------------
	
    public boolean analyzePublicMethod(Object methodClass, Object callingClass){
           
        boolean insidePackageAccess = isCallSamePackage(callingClass, methodClass);
        boolean subClassAccess = isCallClassSubClass(callingClass, methodClass);
        boolean sameClassAccess = isCallClassMethodClass(callingClass, methodClass);
                
        if (!insidePackageAccess && !subClassAccess){
//            methodResultsMap.put(sm, new Integer(RESULT_PUBLIC));
            return true;
        }
        else if (!insidePackageAccess && subClassAccess) {
//            updateToProtected(sm);
            return false;
        }
        else if (insidePackageAccess && !sameClassAccess) {
//            updateToPackage(sm);
            return false;
        }
        else {
//            updateToPrivate(sm);
            return false;
        }
                
    }

	private boolean isCallClassMethodClass(Object callingClass,
			Object methodClass) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isCallClassSubClass(Object callingClass, Object methodClass) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isCallSamePackage(Object callingClass, Object methodClass) {
		// TODO Auto-generated method stub
		return false;
	}
	

	
	public void fp03(Iterator it) {
		
	    catchBodyLoop:
		while (it.hasNext()) {
			Object as =  it.next();

		    Iterator pit = iterator();
		    while (pit.hasNext()) {
			Object pas = pit.next();

			if (contains( pas) == false) {
//			    entryPoint = as;
			    break catchBodyLoop;
			}
		    }
		}

	}

	private boolean contains(Object pas) {
		// TODO Auto-generated method stub
		return false;
	}

	private Iterator iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//----------------------------------------

	/*
	 * In file: ClassResolver.java
	line 430
	line 436
	 */
//	public void fp04(FalsePositive18 aNew) {
//        if (aNew.qualifier() != null){// && (!(aNew.qualifier() instanceof polyglot.ast.Special && ((polyglot.ast.Special)aNew.qualifier()).kind() == polyglot.ast.Special.THIS)) ){
//        //if (aNew.qualifier() != null ) {
//            // add qualifier ref - do this first to get right order
//            addQualifierRefToInit(aNew.qualifier().type());
//            src.hasQualifier(true);
//        }
//        if (info != null && !info.inStaticMethod()){
//            if (!InitialResolver.v().isAnonInCCall(aNew.anonType())){
//                addOuterClassThisRefToInit(aNew.anonType().outer());
//                addOuterClassThisRefField(aNew.anonType().outer());
//                src.thisOuterType(Util.getSootType(aNew.anonType().outer()));
//                src.hasOuterRef(true);
//            }
//        }
//        src.polyglotType((polyglot.types.ClassType)aNew.anonType().superType());
//        src.anonType(aNew.anonType()); 
//        src.inStaticMethod(info.inStaticMethod());
// 		
//	}

	private Object qualifier() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Socket pstmtPool, connection;
	
	private URL myResourceBaseURL;
	private Object logWriter;

    public void close() throws SQLException {
        
        boolean isClosed = true;
        try {
            if (pstmtPool != null) {
                try {
                    pstmtPool.close();
                } finally {
                    pstmtPool = null;
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new SQLException("Cannot close connection (return to pool failed)", e);
        } finally {
            try {
                close();
            } finally {
                connection = null;
            }
        }
    }
	
	
	//-------------------------------------------------
	public void fp06() throws SQLException {
        boolean success = false;
        try {
            FalsePositive18 dataSource = createDataSourceInstance();
            dataSource.setLogWriter(logWriter);
            success = true;
        } catch (SQLException se) {
            throw se;
        } catch (RuntimeException rte) {
            throw rte;
        } catch (Exception ex) {
            throw new SQLException("Error creating datasource", ex);
        } finally {
            if (!success) {
                closeConnectionPool();
            }
        }
		
	}

	private void setLogWriter(Object logWriter2) {
		// TODO Auto-generated method stub
		
	}

	private FalsePositive18 createDataSourceInstance() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	private void closeConnectionPool() {
		// TODO Auto-generated method stub
		
	}
	
}
