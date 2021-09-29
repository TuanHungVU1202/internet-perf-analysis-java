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
import sw.hv.exercise1.model.Task3Iperf;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Task_3<K, V> {
    JSONObject jsoObject = new JSONObject();

    public void processFile() throws IOException {

        // 1st method - using org.json
//        try (InputStream fis = new FileInputStream(""))
//        {
//            JsonFactory jf = new JsonFactory();
//            JsonParser jp = jf.createParser(fis);
//            jp.setCodec(new ObjectMapper());
//            jp.nextToken();
//            while (jp.hasCurrentToken()) {
//                System.out.println(jp.getCurrentName() + ": " + jp.getValueAsString());
//                jp.nextToken();
//            }
//        }


        // 2nd method - using Gson
        InputStream is = new FileInputStream("");
        Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
//        Gson gson = new GsonBuilder().create();
        JsonStreamParser parser = new JsonStreamParser(reader);
//        Map m = null;
        // index for total run count (number of intervals)
        int totalIndex = 1;
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss.SSS");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd ");

        // declare obj we need to get
        int numberOfRetransmission = 0;
        Task3Iperf task3Model = new Task3Iperf();
        double secondsDiffEachRun = 0D;
//        double previousRoundSecondsDiff = 0D;

        while (parser.hasNext()) {
            JsonElement element = parser.next();

            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();

                // getting "intervals" array
                JsonArray intervalArr = obj.get("intervals").getAsJsonArray();

                if (!intervalArr.isEmpty()){
                    // index for each run in 1 interval
                    int testIndex = 1;
                    double previousRoundSecondsDiff = 0D;

                    // getting very first Start time of each interval in Epoch timestamp
                    double timeOfStart = obj.get("start").getAsJsonObject()
                            .get("timestamp").getAsJsonObject()
                            .get("timesecs").getAsDouble();

                    // format timestamp
                    Date timeDateOfStart = new Date((long)(timeOfStart * 1000));

                    for (JsonElement intervalArrElement : intervalArr){
                        // getting sum obj
                        // e.g {"start":0,"end":1.001465,"seconds":1.0014649629592896,"bytes":45108896,"bits_per_second":360343278.4444499,"omitted":false,"sender":true}
                        JsonObject sumObj = intervalArrElement.getAsJsonObject().get("sum").getAsJsonObject();

                        // timestamp and time of start each run
                        timeOfStart += previousRoundSecondsDiff;
                        System.out.printf("%.15f%n", timeOfStart);
                        System.out.printf("%.15f%n", previousRoundSecondsDiff);
                        task3Model.setTimeOfStart((long)timeOfStart);
                        // format date time for each run
                        Date eachRunTime = new Date((long)(timeOfStart * 1000));
                        String formattedDate = dateFormatter.format(eachRunTime);
                        task3Model.setDateOfStart(formattedDate.toString());

                        secondsDiffEachRun = sumObj.get("seconds").getAsDouble();
                        previousRoundSecondsDiff = secondsDiffEachRun;

                        // total bytes transferred each run
                        long totalByteTransferred = sumObj.get("bytes").getAsLong();
//                        System.out.println(totalByteTransferred);

                        // bitrate
                        double bitrate = sumObj.get("bits_per_second").getAsDouble();
//                        System.out.println(bitrate * Math.pow(10,-6));
//                        System.out.printf("%.8f%n", bitrate);

                        // TCP retransmission
//                        if (sumObj.get("sender").getAsBoolean()) {
//                            numberOfRetransmission = sumObj.get("retransmits").getAsInt();
//                        }

                        //TODO: add it to some kind of object to write to CSV
                        testIndex++;
                    }
                    System.out.println("*****" + totalIndex);
                    totalIndex++;
                }
            }

            /* handle other JSON data structures */
        }
    }
}
