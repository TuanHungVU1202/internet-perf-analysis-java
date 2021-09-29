package sw.hv.exercise1;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.omg.Messaging.SyncScopeHelper;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import sw.hv.exercise1.model.Task3Iperf;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Task_3<K, V> {
    JSONObject jsoObject = new JSONObject();

    public void processFile() throws IOException {
        InputStream is = new FileInputStream("/Users/hungvu/Desktop/E7130/e1/output/t3_normal.json");
        Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
        JsonStreamParser parser = new JsonStreamParser(reader);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd ");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss.SSS");

        // declare obj we need to get
        int numberOfRetransmission = 0;

        double secondsDiffEachRun = 0D;
//        double previousRoundSecondsDiff = 0D;
        List<Task3Iperf> lstOfDataRow = new ArrayList<Task3Iperf>();

        while (parser.hasNext()) {
            JsonElement element = parser.next();

            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();

                // getting "intervals" array
                JsonArray intervalArr = obj.get("intervals").getAsJsonArray();

                if (!intervalArr.isEmpty()){
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
                        // e.g {"start":0,"end":1.001465,"seconds":1.0014649629592896,"bytes":45108896,"bits_per_second":360343278.4444499,"omitted":false,"sender":true}
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
//                        System.out.println(totalByteTransferred);

                        // bitrate
                        double bitrate = sumObj.get("bits_per_second").getAsDouble();
                        task3Model.setBitrate(bitrate);
//                        System.out.println(bitrate * Math.pow(10,-6));
//                        System.out.printf("%.8f%n", bitrate);


                        // TCP retransmission
//                        if (sumObj.get("sender").getAsBoolean()) {
//                            numberOfRetransmission = sumObj.get("retransmits").getAsInt();
//                        }

                        // Add to list for each run˚
                        lstOfDataRow.add(task3Model);
                    }
                }
            }
        }

        // Write to csv
        String csvPath = "/Users/hungvu/Desktop/E7130/e1/output/t3_data.csv" ;
        writeCSV(csvPath,lstOfDataRow);
    }

    private void writeCSV (String csvFileName, List<Task3Iperf> lstOfDataRow){
        ICsvBeanWriter beanWriter = null;
        CellProcessor[] processors = new CellProcessor[] {
                new ParseLong(), //eachRunTimestamp
                new NotNull(), // dateOfStart
                new NotNull(), // timeOfStart
                new ParseLong(), // totalBytesTransferred
                new ParseDouble(), // bitrate
                //new ParseInt() // numberOfRetransmission
        };

        try {
            beanWriter = new CsvBeanWriter(new FileWriter(csvFileName),
                    CsvPreference.STANDARD_PREFERENCE);
            String[] header = {"eachRunTimestamp", "dateOfStart", "timeOfStart", "totalBytesTransferred", "bitrate"};
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
}
