package sw.hv.exercise1;

import com.google.gson.*;
import org.jfree.ui.RefineryUtilities;
import org.json.JSONObject;

import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import sun.lwawt.macosx.CSystemTray;
import sw.hv.exercise1.model.Task3Iperf;
import sw.hv.util.Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class Task_3 {
    JSONObject jsoObject = new JSONObject();
    // object for calculation
    double totalBitrate = 0;
    ArrayList<Double> bitrateArrList = new ArrayList<>();

//    public void parseFile () throws Exception {
//        String filePath = Utils.readInput();
//        boolean isValid = Utils.isValidFile(filePath);
//        if (isValid) {
//            processFileForInterval(filePath);
//        }
//    }

    public void processFileForInterval(String filePath) throws Exception {
        try {
            InputStream is = new FileInputStream(filePath);
            Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            JsonStreamParser parser = new JsonStreamParser(reader);
            // index for total run count (number of intervals)
            int totalIndex = 1;
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd ");
            SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");

            // declare obj we need to get
            int numberOfRetransmission = 0;
            List<Task3Iperf> lstOfDataRow = new ArrayList<Task3Iperf>();

            while (parser.hasNext()) {
                JsonElement element = parser.next();

                if (element.isJsonObject()) {
                    JsonObject obj = element.getAsJsonObject();

                    // getting "intervals" array
                    JsonArray intervalArr = obj.get("intervals").getAsJsonArray();

                    if (!intervalArr.isEmpty()){
                        // index for each run in 1 interval
                        int intervalIndex = 1;
                        Task3Iperf task3Model = new Task3Iperf();
                        // getting very first Start time of each interval in Epoch timestamp
                        double timeOfStart = obj.get("start").getAsJsonObject()
                                .get("timestamp").getAsJsonObject()
                                .get("timesecs").getAsDouble();

                        // timestamp and time of start each run
                        task3Model.setEachRunTimestamp((long)timeOfStart);

                        // getting sum_sent obj
                        JsonObject sumObj = obj.get("end").getAsJsonObject().get("sum_sent").getAsJsonObject();

                        // format date time for each run
                        Date eachRunTime = new Date((long)(timeOfStart * 1000));
                        String formattedDate = dateFormatter.format(eachRunTime);
                        String formattedTime = timeFormatter.format(eachRunTime);
                        task3Model.setDateOfStart(formattedDate);
                        task3Model.setTimeOfStart(formattedTime);

                        // total bytes transferred each run
                        long totalByteTransferred = sumObj.get("bytes").getAsLong();
                        task3Model.setTotalBytesTransferred(totalByteTransferred);

                        // bitrate
                        double bitrate = sumObj.get("bits_per_second").getAsDouble();
                        task3Model.setBitrate(bitrate);
                        // Add up to get total bitrate
                        totalBitrate += bitrate;
                        bitrateArrList.add(bitrate);

                        // TCP retransmission
                        numberOfRetransmission = sumObj.get("retransmits").getAsInt();
                        task3Model.setNumberOfRetransmission(numberOfRetransmission);
                        lstOfDataRow.add(task3Model);

    //                    System.out.println("*****" + totalIndex);
                        totalIndex++;
                    }
                }
            }

            // Write to csv
            String home = System.getProperty("user.home");
            String csvPath = home + "/t3_data.csv" ;
            writeCSV(csvPath,lstOfDataRow);
            bitrateCalculation();

            // Plot bitrate grpah
            final CombinedBitrateRetransmissionPlot bitratePlot = new CombinedBitrateRetransmissionPlot("BitratePlot");
            bitratePlot.setListData(lstOfDataRow);
            bitratePlot.plotBitrateChart();
            bitratePlot.pack();
            RefineryUtilities.centerFrameOnScreen(bitratePlot);
            bitratePlot.setVisible(true);

            // Plot retransmission grpah
            final CombinedBitrateRetransmissionPlot retransmissionPlot = new CombinedBitrateRetransmissionPlot("RetransmissionPlot");
            retransmissionPlot.setListData(lstOfDataRow);
            retransmissionPlot.plotRetrChart();
            retransmissionPlot.pack();
            RefineryUtilities.centerFrameOnScreen(retransmissionPlot);
            retransmissionPlot.setVisible(true);

    } catch (Exception e){
            throw new Exception(e);
        }
    }

    // This is what I misunderstand the requirements, calculate with data of every seconds (1 interval has 10 seconds for the test)
    private void processFileForEverySecond(String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
        JsonStreamParser parser = new JsonStreamParser(reader);
        // index for total run count (number of intervals)
        int totalIndex = 1;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd ");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");

        // declare obj we need to get
        int numberOfRetransmission = 0;
        double secondsDiffEachRun = 0D;
        List<Task3Iperf> lstOfDataRow = new ArrayList<Task3Iperf>();



        while (parser.hasNext()) {
            JsonElement element = parser.next();

            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();

                // getting "intervals" array
                JsonArray intervalArr = obj.get("intervals").getAsJsonArray();

                if (!intervalArr.isEmpty()){
                    // index for each run in 1 interval
                    int intervalIndex = 1;

                    // put here to reset after each interval
                    double previousRoundSecondsDiff = 0D;

                    // getting very first Start time of each interval in Epoch timestamp
                    double timeOfStart = obj.get("start").getAsJsonObject()
                            .get("timestamp").getAsJsonObject()
                            .get("timesecs").getAsDouble();

                    // format timestamp
                    Date timeDateOfStart = new Date((long)(timeOfStart * 1000));

                    for (JsonElement intervalArrElement : intervalArr){
                        Task3Iperf task3Model = new Task3Iperf();
                        // getting sum obj
                        JsonObject sumObj = intervalArrElement.getAsJsonObject().get("sum").getAsJsonObject();

                        // timestamp and time of start each run
                        timeOfStart += previousRoundSecondsDiff;
                        task3Model.setEachRunTimestamp((long)timeOfStart);
                        // format date time for each run
                        Date eachRunTime = new Date((long)(timeOfStart * 1000));
                        String formattedDate = dateFormatter.format(eachRunTime);
                        String formattedTime = timeFormatter.format(eachRunTime);
                        task3Model.setDateOfStart(formattedDate);
                        task3Model.setTimeOfStart(formattedTime);

                        secondsDiffEachRun = sumObj.get("seconds").getAsDouble();
                        previousRoundSecondsDiff = secondsDiffEachRun;

                        // total bytes transferred each run
                        long totalByteTransferred = sumObj.get("bytes").getAsLong();
                        task3Model.setTotalBytesTransferred(totalByteTransferred);

                        // bitrate
                        double bitrate = sumObj.get("bits_per_second").getAsDouble();
                        task3Model.setBitrate(bitrate);
                        // Add up to get total bitrate
                        totalBitrate += bitrate;
                        bitrateArrList.add(bitrate);

                        // TCP retransmission
                        if (sumObj.get("sender").getAsBoolean()) {
                            numberOfRetransmission = sumObj.get("retransmits").getAsInt();
                            task3Model.setNumberOfRetransmission(numberOfRetransmission);
                        }

                        lstOfDataRow.add(task3Model);
//                        System.out.println("-----" + intervalIndex);
                        intervalIndex++;

                    }
//                    System.out.println("*****" + totalIndex);
                    totalIndex++;
                }
            }
        }

        // Write to csv
        String csvPath = "/Users/hungvu/Desktop/E7130/e1/from_linux/t3_data.csv" ;
        writeCSV(csvPath,lstOfDataRow);
        bitrateCalculation();
    }

    private void writeCSV (String csvFileName, List<Task3Iperf> lstOfDataRow){
        ICsvBeanWriter beanWriter = null;
        CellProcessor[] processors = new CellProcessor[] {
                new ParseLong(), //eachRunTimestamp
                new NotNull(), // dateOfStart
                new NotNull(), // timeOfStart
                new ParseLong(), // totalBytesTransferred
                new ParseDouble(), // bitrate
                new ParseInt() // numberOfRetransmission
        };

        try {
            beanWriter = new CsvBeanWriter(new FileWriter(csvFileName),
                    CsvPreference.STANDARD_PREFERENCE);
            String[] header = {"eachRunTimestamp", "dateOfStart", "timeOfStart", "totalBytesTransferred", "bitrate","numberOfRetransmission"};
            beanWriter.writeHeader(header);

            for (Task3Iperf row : lstOfDataRow) {
                beanWriter.write(row, header, processors);
            }

        } catch (IOException ex) {
            System.err.println("Error writing the CSV file: " + ex);
        } finally {
            if (beanWriter != null) {
                try {
                    beanWriter.close();
                } catch (IOException ex) {
                    System.err.println("Error closing the writer: " + ex);
                }
            }
        }
    }

    // For task 3.1, 3.2
    private void bitrateCalculation () {
        System.out.println("Task 3.1 - Average Bitrate = " + totalBitrate/bitrateArrList.size());
        Collections.sort(bitrateArrList);
        System.out.println("Task 3.2 - Minimum bitrate = " + bitrateArrList.get(0));
        System.out.println("Task 3.2 - Maximum bitrate = " + bitrateArrList.get(bitrateArrList.size() - 1));

        if (bitrateArrList.size() %2 != 0){
            System.out.println("Task 3.2 - Median bitrate = " + bitrateArrList.get(bitrateArrList.size()/2));
        } else {
            double tmp1 = bitrateArrList.get((bitrateArrList.size()/2)-1);
            double tmp2 = bitrateArrList.get((bitrateArrList.size()/2));
            System.out.println("Task 3.2 - Median bitrate = " + (tmp1 + tmp2)/2);
        }
    }
}
