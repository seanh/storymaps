package storymaps;

import java.text.DateFormat;
import java.util.Date;

/**
 * Miscellaneous static utility methods.
 * 
 * @author seanh
 */
class Util {
        
    /**
     * Return a string representing the current time.
     */
    public static String nowStr() {
        DateFormat longTimeStamp =
                DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL);
        return longTimeStamp.format(new Date());
    }
}