package sw.hv.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ParseCurlUtil {

    public static HashMap<String, Double> statCalculation (ArrayList<Double> dataArrLst, String nameOfData) {
        HashMap<String, Double> resultMap = new HashMap<>();
        double sum = 0;
        double median = 0;
        double aveDeviation = 0;
        for(double element : dataArrLst) {
            sum += element;
        }

        resultMap.put("Sum " + nameOfData, sum);
        double mean = sum/dataArrLst.size();
        resultMap.put("Mean " + nameOfData, mean);

        Collections.sort(dataArrLst);
        double minBitrate = dataArrLst.get(0);
        double maxBitrate = dataArrLst.get(dataArrLst.size() - 1);
        resultMap.put("Min " + nameOfData, minBitrate);
        resultMap.put("Max " + nameOfData, maxBitrate);

        if (dataArrLst.size() %2 != 0){
            median = dataArrLst.get(dataArrLst.size()/2);
            resultMap.put("Median " + nameOfData, median);
        } else {
            double tmp1 = dataArrLst.get((dataArrLst.size()/2)-1);
            double tmp2 = dataArrLst.get((dataArrLst.size()/2));
            median = (tmp1 + tmp2)/2;
            resultMap.put("Median " + nameOfData, median);
        }

        // calculate average deviation
        for(double element : dataArrLst) {
            aveDeviation += Math.abs(element-mean)/dataArrLst.size();
        }

        resultMap.put("Average deviation", aveDeviation);
        return resultMap;
    }

    public static double getDownloadSpeed (String[] splittedArr){
        // currently download speed is at the end of the line
        return Double.parseDouble(splittedArr[splittedArr.length - 1]);
    }

    public static double getDelayTime (String[] splittedArr){
        // currently delay at index 0
        return Double.parseDouble(splittedArr[0]);
    }

    public static ArrayList<Double> getDlSpeedAtSpecificTime (int atIndex, ArrayList<Double> totalDlSpeedArrLst){
        // Get every value at specific minute
        // Here is at minute 15 every hour
        ArrayList<Double> dlSpeedAtSpecificTimeArrLst = new ArrayList<>();
        while (atIndex <= totalDlSpeedArrLst.size()){
            dlSpeedAtSpecificTimeArrLst.add(totalDlSpeedArrLst.get(atIndex));
            // run once an hour, get batch of 6 because test starts from minute 5
            atIndex = atIndex + 6;
        }
        return dlSpeedAtSpecificTimeArrLst;
    }
}
