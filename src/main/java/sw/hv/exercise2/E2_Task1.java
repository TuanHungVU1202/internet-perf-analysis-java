package sw.hv.exercise2;

import sw.hv.util.GeneralHelper;
import sw.hv.util.ParseDigResultUtil;
import sw.hv.util.ParsePingResultUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class E2_Task1 {
    public void parseFile () throws Exception {

//            String filePath = GeneralHelper.readInput();
//            boolean isValid = GeneralHelper.isValidFile(filePath);
//        try {
//            if (isValid) {
////                processPingResult(filePath);
////                processDigResult (filePath);
////                processCurlResult(filePath);
//            }
//        } catch (Exception e){
//            throw new Exception(e);
//        }
        processPingResult("Test");
//        processDigResult ("test");
//        processCurlResult("Test");
    }

    // Parse ping result
    private void processPingResult (String filePath) throws Exception {
        String testPath = "/Users/hungvu/Desktop/E7130/e2/data_43hours/out/iperf_sgp1_ping";
        // for Ex4 task 4
        String pathEx4 = "/Users/hungvu/Desktop/E7130/e4/out/ping_iperf_out/reserver_pna_ping";
        File file = new File(pathEx4);
        ArrayList<Double> pingRttArrLst = new ArrayList<>();
        int totalPingRequest = 0;
        int totalReceivedPacket = 0;

        try{
            Scanner scanner = new Scanner(file);
            double totalDelay = 0;
            boolean sameServerFlag = false;
            int testIndex = 0;
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();

                //TODO: CHANGE SERVER HERE
                if (line.contains(ParsePingResultUtil.SERVER_PNA)){
                    sameServerFlag = true;
                }
                // Get all information of successful requests
                if (line.contains(ParsePingResultUtil.SUCCESSFUL_PACKET) && sameServerFlag){
                    List<String> extractedLst = GeneralHelper.extractNumberFromString(line);
//                    long timestamp = ParsePingResultUtil.getTimeStamp(extractedLst);
                    testIndex++;
                    String ip = ParsePingResultUtil.getIp(extractedLst);
                    double rtt = ParsePingResultUtil.getRtt(extractedLst);
                    totalDelay += rtt;
                    pingRttArrLst.add(rtt);
                }

                // Get number of total requests
                if (line.contains(ParsePingResultUtil.TOTAL_PACKET_STAT) && sameServerFlag){
                    List<String> totalStatLst = GeneralHelper.extractNumberFromString(line);
                    int transmittedPacket = ParsePingResultUtil.getTransmittedPacket(totalStatLst);
                    int receivedPacket = ParsePingResultUtil.getReceivedPacket(totalStatLst);
                    totalPingRequest += transmittedPacket;
                    totalReceivedPacket += receivedPacket;
                    sameServerFlag = false;
                }
            }
            // Sort array
            if (!pingRttArrLst.isEmpty()) {
                Collections.sort(pingRttArrLst);
                System.out.println("PING - Median delay= " + calMedianDelay(pingRttArrLst, totalPingRequest));
                System.out.println("PING -Mean delay= " + calMeanDelay(totalDelay, totalReceivedPacket));
                System.out.println("PING -Lost ratio= " + calLosttRatio(totalReceivedPacket, totalPingRequest));
                System.out.println("PING -Delay spread= " + calDelaySpread(pingRttArrLst, totalReceivedPacket));
                System.out.println("PING - Total delay time= " + totalDelay);
            }
        } catch (Exception e){
            throw new Exception(e);
        }
    }

    // Parse Dig result
    private void processDigResult (String filePath) throws Exception {
        String testPath = "/Users/hungvu/Desktop/E7130/e2/data_43hours/out/nameserver_dig";
        File file = new File(testPath);
        ArrayList<Double> queryTimeArrLst = new ArrayList<>();

        try{
            Scanner scanner = new Scanner(file);
            double totalDelay = 0;
            boolean sameServerFlag = false;
            int totalRequest = 0;
            int totalSucessfulRequest = 0;

            while (scanner.hasNextLine()){
                String line = scanner.nextLine();

                // TODO: CHANGE SERVER HERE
                if (line.contains(ParseDigResultUtil.SERVER_Z) && line.contains(ParseDigResultUtil.DIG_INDICATOR)){
                    sameServerFlag = true;
                    totalRequest++;
                }

                if (line.contains(ParseDigResultUtil.QUERY_TIME) && sameServerFlag){
                   totalSucessfulRequest++;
                   List<String> extractedList = GeneralHelper.extractNumberFromString(line);
                   double queryTime = ParseDigResultUtil.getQueryTime(extractedList);
                   queryTimeArrLst.add(queryTime);
                   totalDelay += queryTime;
                   sameServerFlag = false;
                }
            }

            if (!queryTimeArrLst.isEmpty()) {
                Collections.sort(queryTimeArrLst);
                System.out.println("DIG - Median delay= " + calMedianDelay(queryTimeArrLst, totalRequest));
                System.out.println("DIG -Mean delay= " + calMeanDelay(totalDelay, totalSucessfulRequest));
                System.out.println("DIG -Lost ratio= " + calLosttRatio(totalSucessfulRequest, totalRequest));
                System.out.println("DIG -Delay spread= " + calDelaySpread(queryTimeArrLst, totalSucessfulRequest));
            }

        } catch (Exception e){
            throw new Exception(e);
        }
    }

    // Parse curl result
    public void processCurlResult (String filePath) throws Exception{
        //TODO: CHANGE SERVER HERE
//        String testPath = "/Users/hungvu/Desktop/E7130/e2/data_43hours/out/iperf_ok1_curl";
//        String testPath = "/Users/hungvu/Desktop/E7130/e2/data_43hours/out/iperf_sgp1_curl";
        File file = new File(filePath);
        ArrayList<Double> totalTimeArrLst = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(file);
            double totalDelay = 0;
            double totalSpeed = 0;
            int totalRequest = 0;

            while (scanner.hasNextLine()){
                String line = scanner.nextLine();

                // Delay Time for TASK 1
                String[] totalDelayArr = line.split(",");
                double delayTime = Double.parseDouble(totalDelayArr[0]);
                totalTimeArrLst.add(delayTime);
                totalDelay += delayTime;

                totalRequest++;
            }

            // Calculate delay time for TASK 1
            if (!totalTimeArrLst.isEmpty()) {
                Collections.sort(totalTimeArrLst);
                System.out.println("CURL - Median delay= " + calMedianDelay(totalTimeArrLst, totalRequest));
                System.out.println("CURL -Mean delay= " + calMeanDelay(totalDelay, totalRequest));
                System.out.println("CURL -Lost ratio= " + calLosttRatio(totalRequest, totalRequest));
                System.out.println("CURL -Delay spread= " + calDelaySpread(totalTimeArrLst, totalRequest));
            }

        } catch (Exception e){
            throw new Exception(e);
        }
    }

    // median delay WITH lost packets
    private Double calMedianDelay(List<Double> delayArrLst, int totalRequestIncludedLost){
        if (totalRequestIncludedLost %2 != 0){
            return delayArrLst.get(totalRequestIncludedLost/2);
        } else {
            double tmp1 = delayArrLst.get((totalRequestIncludedLost/2)-1);
            double tmp2 = delayArrLst.get((totalRequestIncludedLost/2));
            return (tmp1 + tmp2)/2;
        }
    }

    // mean delay WITHOUT lost packets
    private double calMeanDelay(Double totalDelay, int totalSuccessfulPacket){
        return totalDelay/totalSuccessfulPacket;
    }

    private double calLosttRatio(int totalSuccessfulPacket, int totalRequestIncludedLost){
//        System.out.println("---re " + totalSuccessfulPacket);
//        System.out.println("---total " + totalRequestIncludedLost);
        return 1 - totalSuccessfulPacket/totalRequestIncludedLost;
    }

    // Delay spread of 75th and 25th percentiles WITH lost packets
    private double calDelaySpread(List<Double> delayArrLst, int totalSuccessfulPacket){
        double percentile_25 = ParsePingResultUtil.percentile(delayArrLst, 25, totalSuccessfulPacket);
        double percentile_75 = ParsePingResultUtil.percentile(delayArrLst, 75, totalSuccessfulPacket);
        return Math.abs(percentile_75 - percentile_25);
    }
}
