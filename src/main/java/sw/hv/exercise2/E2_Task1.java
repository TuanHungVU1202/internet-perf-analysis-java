package sw.hv.exercise2;

import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;
import sw.hv.util.GeneralHelper;
import sw.hv.util.ParsePingResultUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class E2_Task1 {

    ArrayList<Double> rttArrLst = new ArrayList<>();
    int totalNameServerPingRequest = 0;
    int totalNameServerReceivedPacket = 0;

    public void parseFile () throws Exception {
//        try {
//            String filePath = GeneralHelper.readInput();
//            boolean isValid = GeneralHelper.isValidFile(filePath);
//            if (isValid) {
//                ParsePingResultUtil.parsePingResult(filePath);
//            }
//        } catch (Exception e){
//            throw new Exception(e);
//        }
        processPingResult("Test");
    }

    private void processPingResult (String filePath) throws Exception {
        String testPath = "/Users/hungvu/Desktop/E7130/e2/data_43hours/out/iperf_sgp1_ping";
        File file = new File(testPath);

        try{
            Scanner scanner = new Scanner(file);
            double totalDelay = 0;

            while (scanner.hasNextLine()){
                String line = scanner.nextLine();

                // Get all information of successful requests
                if (line.contains(ParsePingResultUtil.SUCCESSFUL_PACKET)){
                    List<String> extractedLst = GeneralHelper.extractNumberFromString(line);
//                    long timestamp = ParsePingResultUtil.getTimeStamp(extractedLst);
                    String ip = ParsePingResultUtil.getIp(extractedLst);
                    double rtt = ParsePingResultUtil.getRtt(extractedLst);
                    totalDelay += rtt;
                    rttArrLst.add(rtt);
//                    System.out.println(ip + " - " + rtt);
                }

                // Get number of total requests
                if (line.contains(ParsePingResultUtil.TOTAL_PACKET_STAT)){
                    List<String> totalStatLst = GeneralHelper.extractNumberFromString(line);
                    int transmittedPacket = ParsePingResultUtil.getTransmittedPacket(totalStatLst);
                    int receivedPacket = ParsePingResultUtil.getReceivedPacket(totalStatLst);
                    totalNameServerPingRequest += transmittedPacket;
                    totalNameServerReceivedPacket += receivedPacket;
//                    System.out.println(transmittedPacket + " - " + receivedPacket + " - " + totalRequest);
                }
            }
            // Sort array
            Collections.sort(rttArrLst);
            System.out.println("Median delay= " + calMedianDelay());
            System.out.println("Mean delay= " + calMeanDelay(totalDelay));
            System.out.println("Lost ratio= " + calLosttRatio());
            System.out.println("Delay spread= " + calDelaySpread());
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    // median delay WITH lost packets
    private Double calMedianDelay(){
        if (totalNameServerPingRequest %2 != 0){
            return rttArrLst.get(totalNameServerPingRequest/2);
        } else {
            double tmp1 = rttArrLst.get((totalNameServerPingRequest/2)-1);
            double tmp2 = rttArrLst.get((totalNameServerPingRequest/2));
            return (tmp1 + tmp2)/2;
        }
    }

    // mean delay WITHOUT lost packets
    private double calMeanDelay(Double totalDelay){
        return totalDelay/totalNameServerReceivedPacket;
    }

    private double calLosttRatio(){
        return 1 - totalNameServerReceivedPacket/totalNameServerPingRequest;
    }

    // Delay spread of 75th and 25th percentiles WITH lost packets
    private double calDelaySpread(){
        double percentile_25 = ParsePingResultUtil.percentile(rttArrLst, 25, totalNameServerPingRequest);
        double percentile_75 = ParsePingResultUtil.percentile(rttArrLst, 75, totalNameServerPingRequest);
        return Math.abs(percentile_75 - percentile_25);
    }
}
