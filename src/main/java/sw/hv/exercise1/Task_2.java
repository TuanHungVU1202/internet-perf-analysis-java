package sw.hv.exercise1;

import sw.hv.util.Utils;

import java.io.File;
import java.sql.Array;
import java.util.*;

public class Task_2 {
    String START_LOG = "Start";
    String SUCCESSFUL_ROUNDTRIP = "time=";
    int hourNumber = 1;
    int REQUEST_PER_ROUND = 20;
    int NUMBER_OF_ROUND = 6;

    public void parseFile () throws Exception {
        String filePath = Utils.readInput();
        boolean isValid = Utils.isValidFile(filePath);
        if (isValid) {
            processFile(filePath);
        }
    }

    private void processFile (String filePath) throws Exception {
        File file = new File(filePath);
        try {
            Scanner scanner = new Scanner(file);
            int lineNum = 0;
            int counterBatch = 0;
            int counterSuccessful = 0;
            double extractedRtt = 0;
            double rttInOneHour = 0;
            ArrayList<Double> rttArrList = new ArrayList<Double>();
            boolean firstStartFlag = false;

            HashMap<Integer, Double> percentLostPacketMap = new HashMap<Integer, Double>();
            HashMap<Integer, Double> averageRttMap = new HashMap<Integer, Double>();
            HashMap<Integer, Double> medianRttMap = new HashMap<Integer, Double>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;

                // let say 60 minutes = 1 hour => let counter increase 6 times
                if (line.contains(START_LOG) && firstStartFlag){
                    counterBatch++;
                }

                if (counterBatch < 6) {
                    // Check all successful round-trip-time
                    if (line.contains(SUCCESSFUL_ROUNDTRIP)){

                        counterSuccessful++;
                        List<String> tmpList = Utils.extractNumberFromString(line);
                        extractedRtt = Double.parseDouble(tmpList.get(4));

                        // add extracted RTT to the array to sort and find median later
                        rttArrList.add(extractedRtt);

                        // add up RTT in 1 HOUR - 120 rounds
                        rttInOneHour += extractedRtt;

                        // Check done first round
                        firstStartFlag = true;
                    }
                }

                // After EACH 1 HOUR
                // calculate and reset holding variables
                if (counterBatch == 6){
                    percentOfLostPacket(hourNumber, percentLostPacketMap, counterSuccessful);
                    averageRtt(hourNumber, averageRttMap, rttInOneHour);
                    medianRtt(hourNumber, medianRttMap, rttArrList);
                    hourNumber++;

                    // reset holding variables for next Hour
                    counterBatch = 0;
                    counterSuccessful = 0;
                    rttArrList.clear();
                    rttInOneHour = 0;
                }
            }


            System.out.println("Task 2.1 in form of {Number of hour - Percentage of lost packets}: " + percentLostPacketMap);
            System.out.println("Task 2.2 in form of {Number of hour - Average of Successful RTT}: " + averageRttMap);
            System.out.println("Task 2.3 in form of {Number of hour - Median of RTT}: " + medianRttMap);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    // Task 2.1
    // Count the number of successful requests, then divide by total number of request sent and be substracted by 1 => percentage of lost packets
    private void percentOfLostPacket (int hourNumber, HashMap<Integer, Double> percentLostPacketMap, int counterSuccessful) {
        percentLostPacketMap.put(hourNumber, (1D - counterSuccessful/(REQUEST_PER_ROUND*NUMBER_OF_ROUND)));
    }

    // Task 2.2
    // Add up rtt in each batch of 60 minutes, then divide by total request sent => 20 requests/ each 10th minutes => 120 requests
    private void averageRtt(int hourNumber, HashMap<Integer, Double> averageRttMap, double rttTotal){
        averageRttMap.put(hourNumber, rttTotal/(REQUEST_PER_ROUND*NUMBER_OF_ROUND));
    }

    // Task 2.3, given that the total message sent is an even number
    private void medianRtt(int hourNumer, HashMap<Integer, Double> medianRttMap, ArrayList<Double> rttArrList){
        Collections.sort(rttArrList);
        // For Odd number of total requests
        if ((REQUEST_PER_ROUND*NUMBER_OF_ROUND) %2 != 0){
            medianRttMap.put(hourNumer, rttArrList.get(REQUEST_PER_ROUND*NUMBER_OF_ROUND/2));
        }
        // Even number of total requests
        // Add 2 values found at the center and divide them by 2 to get median
        else {
            double tempVal = rttArrList.get(((REQUEST_PER_ROUND*NUMBER_OF_ROUND)/2)-1) + rttArrList.get((REQUEST_PER_ROUND*NUMBER_OF_ROUND)/2);
            medianRttMap.put(hourNumer, tempVal / 2.0);
        }
    }
}
