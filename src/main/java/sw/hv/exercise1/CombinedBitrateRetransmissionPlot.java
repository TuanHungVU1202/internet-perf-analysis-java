package sw.hv.exercise1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import sw.hv.exercise1.model.Task3Iperf;

public class CombinedBitrateRetransmissionPlot extends ApplicationFrame {
    private List<Task3Iperf> listData;
    SimpleDateFormat timeFormatter = new SimpleDateFormat("HH.mm");

    public CombinedBitrateRetransmissionPlot(String title) {
        super(title);
    }

    public void plotBitrateChart () throws ParseException {
        final JFreeChart bitrateChart = createBitrateChart();
        final ChartPanel panel1 = new ChartPanel(bitrateChart, true, true, true, true, true);
        panel1.setPreferredSize(new java.awt.Dimension(1500, 1000));
        setContentPane(panel1);
    }

    public void plotRetrChart () throws ParseException {
        final JFreeChart retrChart = createRetrChart();
        final ChartPanel panel2 = new ChartPanel(retrChart, true, true, true, true, true);
        panel2.setPreferredSize(new java.awt.Dimension(1500, 1000));
        setContentPane(panel2);
    }

     private JFreeChart createBitrateChart() throws ParseException {
        XYDataset dataset = createBitrateDataset();
        // Create chart
        return ChartFactory.createScatterPlot("Bitrate at certain time of Normal and Reverse Direction",
                "Time (At X O'clock)", "Bitrate (Mbps)", dataset, PlotOrientation.VERTICAL, true, true, false);
    }

    private JFreeChart createRetrChart() throws ParseException {
        XYDataset dataset = createRetrDataset();
        // Create chart
        return ChartFactory.createScatterPlot("Retransmission at certain time of Normal and Reverse Direction",
                "Time (At X O'clock)", "Number of retransmission", dataset, PlotOrientation.VERTICAL, true, true, false);
    }

    private XYDataset createBitrateDataset() throws ParseException {
        // create dataset 1...
        final XYSeries sBitrateNormal = new XYSeries("Bitrate-Normal Direction");
        final XYSeries sBitrateReverse = new XYSeries("Bitrate-Reverse Direction");

        for (int i=0; i < listData.size()/2; i++){
            Date timestamp = new Date((listData.get(i).getEachRunTimestamp() * 1000));
            String formattedTime = timeFormatter.format(timestamp);
            sBitrateNormal.add(Double.parseDouble(formattedTime), listData.get(i).getBitrate()*Math.pow(10,-6));
//            System.out.println("----: "+ formattedTime + " " + listData.get(i).getBitrate()*Math.pow(10,-6));
        }

        for (int i= listData.size()/2; i < listData.size(); i++){
            Date timestamp = new Date((listData.get(i).getEachRunTimestamp() * 1000));
            String formattedTime = timeFormatter.format(timestamp);
            sBitrateReverse.add(Double.parseDouble(formattedTime), listData.get(i).getBitrate()*Math.pow(10,-6));
//            System.out.println("----: "+ formattedTime + " " + listData.get(i).getBitrate());
        }

//        System.out.println(listData.get(listData.size()/2 -1).getBitrate());

        final XYSeriesCollection collection = new XYSeriesCollection();
        collection.addSeries(sBitrateNormal);
        collection.addSeries(sBitrateReverse);
        return collection;

    }

    private XYDataset createRetrDataset() {

        // create dataset 2...
        final XYSeries sRetrNormal = new XYSeries("Retransmission-Normal Direction");
        final XYSeries sRetrReverse = new XYSeries("Retransmission-Reverse Direction");

        for (int i=0; i < listData.size()/2; i++){
            Date timestamp = new Date((listData.get(i).getEachRunTimestamp() * 1000));
            String formattedTime = timeFormatter.format(timestamp);
            sRetrNormal.add(Double.parseDouble(formattedTime), listData.get(i).getNumberOfRetransmission());
//            System.out.println("----: "+ formattedTime + " " + listData.get(i).getNumberOfRetransmission());
        }

        for (int i= listData.size()/2; i < listData.size(); i++){
            Date timestamp = new Date((listData.get(i).getEachRunTimestamp() * 1000));
            String formattedTime = timeFormatter.format(timestamp);
            sRetrReverse.add(Double.parseDouble(formattedTime), listData.get(i).getNumberOfRetransmission());
//            System.out.println("----: "+ formattedTime + " "+ listData.get(i).getNumberOfRetransmission());
        }

        final XYSeriesCollection collection = new XYSeriesCollection();
        collection.addSeries(sRetrNormal);
        collection.addSeries(sRetrReverse);
        return collection;

    }

    public List<Task3Iperf> getListData() {
        return listData;
    }

    public void setListData(List<Task3Iperf> listData) {
        this.listData = listData;
    }
}
