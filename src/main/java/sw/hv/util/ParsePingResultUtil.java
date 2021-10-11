package sw.hv.util;

import java.util.List;

public class ParsePingResultUtil {

    public final static String SUCCESSFUL_PACKET = "time=";
    public final static String TOTAL_PACKET_STAT = "packets";

    // example [1633731901.864601, 64, ., ., ., 195.148.124.36, 1, 60, 0.377]
    // Get timestamp of each request
    public static long getTimeStamp(List<String> extractedLst){
        return (long) Double.parseDouble(extractedLst.get(0));
    }

    // Get IP of each request
    public static String getIp(List<String> extractedLst){
        return extractedLst.get(5);
    }

    public static double getRtt (List<String> extractedLst) {
        return Double.parseDouble(extractedLst.get(extractedLst.size() - 1));
    }

    // Get packet transmitted from Total stat
    public static int getTransmittedPacket (List<String> totalStatLst){
        return Integer.parseInt(totalStatLst.get(0));
    }

    // Get packet transmitted from Total stat
    public static int getReceivedPacket (List<String> totalStatLst){
        return Integer.parseInt(totalStatLst.get(1));
    }

    // Get packet transmitted from Total stat
    public static int getPercentOfLostPacket (List<String> totalStatLst){
        return Integer.parseInt(totalStatLst.get(2));
    }

    public static Double percentile(List<Double> latencies, double percentile, int totalRequest) {
        int index = (int) Math.ceil(percentile / 100.0 * totalRequest);
        return latencies.get(index-1);
    }
}
