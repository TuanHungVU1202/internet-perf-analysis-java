package sw.hv.exercise2;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import org.jfree.ui.RefineryUtilities;
import sw.hv.exercise1.CombinedBitrateRetransmissionPlot;
import sw.hv.exercise1.model.E1_Task3Iperf;
import sw.hv.util.ParseCurlUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class E2_Task2 {
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd ");
    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
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
//        processIperf("Test");
        processCurl("");
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
                        totalIndex++;
                    }
                }
            }


            HashMap<String, Double> resultMap = ParseCurlUtil.bitrateCalculation(bitrateArrList);
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

    private void processCurl (String filePath) throws Exception {
        // current file created timestamp = 1633730710
//        Date dateAndTime = new Date((timeOfStart * 1000));
//        String formattedDate = dateFormatter.format(dateAndTime);
//        String formattedTime = timeFormatter.format(dateAndTime);
        //TODO: CHANGE SERVER HERE
//        String testPath = "/Users/hungvu/Desktop/E7130/e2/data_43hours/out/iperf_ok1_curl";
//        String testPath = "/Users/hungvu/Desktop/E7130/e2/data_43hours/out/iperf_sgp1_curl";
        String testPath = "/Users/hungvu/Desktop/E7130/e2/data_e2/out/t2iperf_sgp1_curl";
        File file = new File(testPath);
        ArrayList<Double> totalDlSpeedArrLst = new ArrayList<>();

        // Initial index to get the value at minute 15 since the test start at minute 5, run every 10 minutes
        int index = 1;
        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()){
                String line = scanner.nextLine();

                // Data array splitting by ,
                String[] dataArr = line.split(",");

                // Download Speed for TASK 2
                double downloadSpeed = ParseCurlUtil.getDownloadSpeed(dataArr);
                // Convert from Bytes per second to Megabits per seconds
                totalDlSpeedArrLst.add(downloadSpeed*8 * Math.pow(10, -6));
            }

//            ArrayList<Double> dlSpeedAtSpecificTimeArrLst = ParseCurlUtil.getDlSpeedAtSpecificTime(index, totalDlSpeedArrLst);

            // Calculate download speed for TASK 2
            if (!totalDlSpeedArrLst.isEmpty()) {
                Collections.sort(totalDlSpeedArrLst);
//                System.out.println("Calculating DOWNLOAD Speed, run once an hour starts at " + formattedDate + " " + formattedTime);
                System.out.println(ParseCurlUtil.bitrateCalculation(totalDlSpeedArrLst));
            }

        } catch (Exception e){
            throw new Exception(e);
        }
    }
}
