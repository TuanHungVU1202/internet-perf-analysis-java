package sw.hv.util;

import java.util.List;

public class ParseDigResultUtil {
    public final static String DIG_INDICATOR = "<<>> DiG";
    public final static String SERVER_C = "c.nsset.be";
    public final static String SERVER_Y = "y.nsset.be";
    public final static String SERVER_Z = "z.nsset.be";

    public final static String QUERY_TIME = "Query time";
    public final static String TIMESTAMP = "WHEN:";

    public static double getQueryTime (List<String> extractedLst) {
        return Double.parseDouble(extractedLst.get(0));
    }
}
