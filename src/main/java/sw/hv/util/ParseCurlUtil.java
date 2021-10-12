package sw.hv.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ParseCurlUtil {

    public static HashMap<String, Double> bitrateCalculation (ArrayList<Double> bitrateArrList) {
        HashMap<String, Double> bitrateMap = new HashMap<>();
        double sum = 0;
        double median = 0;
        double aveDeviation = 0;
        for(double element : bitrateArrList) {
            sum += element;
        }
        double mean = sum/bitrateArrList.size();
        bitrateMap.put("Mean bitrate", mean);

        Collections.sort(bitrateArrList);
        double minBitrate = bitrateArrList.get(0);
        double maxBitrate = bitrateArrList.get(bitrateArrList.size() - 1);
        bitrateMap.put("Min bitrate", minBitrate);
        bitrateMap.put("Max bitrate", maxBitrate);

        if (bitrateArrList.size() %2 != 0){
            median = bitrateArrList.get(bitrateArrList.size()/2);
            bitrateMap.put("Median bitrate", median);
        } else {
            double tmp1 = bitrateArrList.get((bitrateArrList.size()/2)-1);
            double tmp2 = bitrateArrList.get((bitrateArrList.size()/2));
            median = (tmp1 + tmp2)/2;
            bitrateMap.put("Median bitrate", median);
        }

        // calculate average deviation
        for(double element : bitrateArrList) {
            aveDeviation += Math.abs(element-mean)/bitrateArrList.size();
        }

        bitrateMap.put("Average deviation", aveDeviation);
        return bitrateMap;
    }

    public static double getDownloadSpeed (String[] splittedArr){
        // currently download speed is at the end of the line
        return Double.parseDouble(splittedArr[splittedArr.length - 1]);
    }

    public static double getDelayTime (String[] splittedArr){
        // currently delay at index 0
        return Double.parseDouble(splittedArr[0]);
    }
}
