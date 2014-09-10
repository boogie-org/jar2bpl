

import java.sql.Connection;
import java.sql.SQLException;

public class Demo {

	public static void main(String[] args)  {
		Demo d = new Demo();
		try {
			d.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//	private boolean isClosed;
    private Connection connection = null;
	private Connection pstmtPool;
	private boolean isClosed;
	
    public void close() throws SQLException {
        assertOpen();
        isClosed = true;    	
        try {
            if (pstmtPool != null) {
                try {
                    close2();
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
                connection.close();
            } finally {
                connection = null;
            }
        }
    }
    
    private void close2() {}

    private void assertOpen() throws SQLException {
        if (isClosed) {
            throw new SQLException("");
        }
    }
	
	
}
