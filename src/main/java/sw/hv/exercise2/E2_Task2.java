package sw.hv.exercise2;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import org.jfree.ui.RefineryUtilities;
import sw.hv.exercise1.CombinedBitrateRetransmissionPlot;
import sw.hv.exercise1.model.E1_Task3Iperf;
import sw.hv.util.ThroughputUtil;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class E2_Task2 {
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
        processIperf("Test");
    }

    private void processIperf(String filePath) throws Exception {
        String testPath = "/Users/hungvu/Desktop/E7130/e2/data_43hours/out/t2iperf_reverse2.json";
        try {
            ArrayList<Double> bitrateArrList = new ArrayList<>();
            InputStream is = new FileInputStream(testPath);
            Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            JsonStreamParser parser = new JsonStreamParser(reader);
            // index for total run count (number of intervals)
            int totalIndex = 1;
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd ");
            SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");

            // declare obj we need to get
            List<E1_Task3Iperf> lstOfDataRow = new ArrayList<E1_Task3Iperf>();

            while (parser.hasNext()) {
                JsonElement element = parser.next();

                if (element.isJsonObject()) {
                    JsonObject obj = element.getAsJsonObject();

                    // getting "intervals" array
                    JsonArray intervalArr = obj.get("intervals").getAsJsonArray();

                    if (!intervalArr.isEmpty()){
                        // index for each run in 1 interval
                        int intervalIndex = 1;
                        // getting very first Start time of each interval in Epoch timestamp
                        double timeOfStart = obj.get("start").getAsJsonObject()
                                .get("timestamp").getAsJsonObject()
                                .get("timesecs").getAsDouble();


                        // getting sum_sent obj
                        JsonObject sumObj = obj.get("end").getAsJsonObject().get("sum_sent").getAsJsonObject();

                        // format date time for each run
                        Date eachRunTime = new Date((long)(timeOfStart * 1000));
                        String formattedDate = dateFormatter.format(eachRunTime);
                        String formattedTime = timeFormatter.format(eachRunTime);

                        // bitrate
                        double bitrate = sumObj.get("bits_per_second").getAsDouble();
                        bitrateArrList.add(bitrate);

                        System.out.println("Date time: " + formattedDate + " " + formattedTime + " " + "Bitrate: " + bitrate);

                        //                    System.out.println("*****" + totalIndex);
                        totalIndex++;
                    }
                }
            }


            HashMap<String, Double> resultMap = ThroughputUtil.bitrateCalculation(bitrateArrList);
            System.out.println(resultMap);

            // Plot bitrate grpah
            final CombinedBitrateRetransmissionPlot bitratePlot = new CombinedBitrateRetransmissionPlot("BitratePlot");
            bitratePlot.setListData(lstOfDataRow);
            bitratePlot.plotBitrateChart();
            bitratePlot.pack();
            RefineryUtilities.centerFrameOnScreen(bitratePlot);
            bitratePlot.setVisible(true);

        } catch (Exception e){
            throw new Exception(e);
        }
    }
}
