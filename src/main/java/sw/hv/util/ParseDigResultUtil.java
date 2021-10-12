package sw.hv.util;

import java.util.List;

public class ParseDigResultUtil {
    public final static String DIG_INDICATOR = "<<>> DiG";
    public final static String SERVER_1 = "c.nsset.be";
    public final static String SERVER_2 = "y.nsset.be";
    public final static String SERVER_3 = "z.nsset.be";

    public final static String QUERY_TIME = "Query time";
    public final static String TIMESTAMP = "WHEN:";

    public static double getQueryTime (List<String> extractedLst) {
        return Double.parseDouble(extractedLst.get(0));
    }
}
