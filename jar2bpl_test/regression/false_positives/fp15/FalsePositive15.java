

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FalsePositive15 {
    protected static String currentDate = null;
    protected static long currentDateGenerated = 0L;
    protected static final SimpleDateFormat format =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);

    /**
     * Get the current date in HTTP format.
     * 
     * Tomcat
     * In file: FastHttpDateFormat.java
	 * line 120
     */
    public static final String getCurrentDate() {

        long now = System.currentTimeMillis();
        if ((now - currentDateGenerated) > 1000) {
            synchronized (format) {
                if ((now - currentDateGenerated) > 1000) {
                    currentDateGenerated = now;
                    currentDate = format.format(new Date(now));
                }
            }
        }
        return currentDate;

    }

}
