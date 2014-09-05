
import java.io.IOException;

public class FalsePositive12 {
	
	public class TlsFatalAlert extends IOException {}
	
    public int receive(byte[] buf, int off, int len, int waitMillis)
            throws IOException
        {
            try
            {
                return receive2(buf, off, len, waitMillis);
            }
            catch (TlsFatalAlert fatalAlert)
            {                
                throw fatalAlert;
            }
            catch (IOException e)
            {                
                throw e;
            }
            catch (RuntimeException e)
            {                
                throw new TlsFatalAlert();
            }
        }

	private int receive2(byte[] buf, int off, int len, int waitMillis) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}	

}
