package sw.hv.exercise1;

import sw.hv.util.Utils;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Task_2 {
    String START_LOG = "Start";
    String TRANSMITTED_PACKET = "transmitted";
    String SUCCESSFUL_ROUNDTRIP = "time=";
    int hourNumer = 1;

    public void parseFile () throws Exception {
        String filePath = Utils.readInput();
//        boolean isValid = Utils.isValidFile(filePath);
        processFile(filePath);
//        if (isValid) {
//            processFile(filePath);
//        }
    }

    private void processFile (String filePath) throws Exception {
        File file = new File(filePath);
        try {
            Scanner scanner = new Scanner(file);
            int lineNum = 0;
            int counterBatch = 0;
            int counterSuccessful = 0;
            double rttInOneHour = 0;
            boolean firstStartFlag = false;

            HashMap<Integer, Double> percentLostPacketMap = new HashMap<Integer, Double>();
            HashMap<Integer, Double> averageRttMap = new HashMap<Integer, Double>();
            HashMap<Integer, Double> medianRttMap = new HashMap<Integer, Double>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;

                // let say 60 minutes = 1 hour => let counter increase 6 times
                if (line.contains(START_LOG) && firstStartFlag){
                    System.out.println("********************counterBatch 1:  " + counterBatch);
                    counterBatch++;
                }

                if (counterBatch < 6) {
                    // Check all successful round-trip-time
                    if (line.contains(SUCCESSFUL_ROUNDTRIP)){

                        counterSuccessful++;
                        List<String> tmpList = Utils.extractNumberFromString(line);
                        rttInOneHour += Double.parseDouble(tmpList.get(4));

//                        System.out.println("2.2:  " + tmpList);
                        System.out.println("2.2 rtt total:  " + rttInOneHour);
                        System.out.println("Counting:  " + counterSuccessful);
                        System.out.println("counterBatch 2:  " + counterBatch);

                        // Check done first round
                        firstStartFlag = true;
                    }
                }


                // After EACH 1 HOUR
                // calculate and reset holding variables
                if (counterBatch == 6){
                    System.out.println("--------------------------------------------HOUR:       " + hourNumer);
                    percentOfLostPacket(hourNumer, percentLostPacketMap, counterSuccessful);
                    averageRtt(hourNumer, averageRttMap, rttInOneHour);
                    hourNumer++;
                    counterBatch = 0;
                    counterSuccessful = 0;
                    rttInOneHour = 0;
                }
            }


            System.out.println("Result of task 2.1 in form of {Number of hour - Percentage of lost packets}: " + percentLostPacketMap);
            System.out.println("Result of task 2.2 in form of {Number of hour - Average of Successful RTT}: " + averageRttMap);
            System.out.println("Result of task 2.3 in form of {Number of hour - Median of RTT}: " + medianRttMap);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // Task 2.1
    // Count the number of successful requests, then divide by total number of request sent and be substracted by 1 => percentage of lost packets
    private void percentOfLostPacket (int hourNumber, HashMap<Integer, Double> percentLostPacketMap, int counterSuccessful) {
        percentLostPacketMap.put(hourNumber, (1D - counterSuccessful/(20*6)));
    }

    // Task 2.2
    // Add up rtt in each batch of 60 minutes, then divide by total request sent => 20 requests/ each 10th minutes => 120 requests
    private void averageRtt(int hourNumber, HashMap<Integer, Double> averageRttMap, double rttTotal){
        averageRttMap.put(hourNumber, rttTotal/(20*6));
    }

    // Task 2.3, given that the total message sent is an even number
    private void medianRtt(){

    }
}
