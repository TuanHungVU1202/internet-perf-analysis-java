package sw.hv.exercise1.model;

public class Task3Iperf {
    private long eachRunTimestamp;
    private String dateOfStart;
    private String timeOfStart;
    private long totalBytesTransferred;
    private double bitrate;
//    private int numberOfRetransmission;

    public long getEachRunTimestamp() {
        return eachRunTimestamp;
    }

    public void setEachRunTimestamp(long eachRunTimestamp) {
        this.eachRunTimestamp = eachRunTimestamp;
    }

    public String getDateOfStart() {
        return dateOfStart;
    }

    public void setDateOfStart(String dateOfStart) {
        this.dateOfStart = dateOfStart;
    }

    public String getTimeOfStart() {
        return timeOfStart;
    }

    public void setTimeOfStart(String timeOfStart) {
        this.timeOfStart = timeOfStart;
    }

    public long getTotalBytesTransferred() {
        return totalBytesTransferred;
    }

    public void setTotalBytesTransferred(long totalBytesTransferred) {
        this.totalBytesTransferred = totalBytesTransferred;
    }

    public double getBitrate() {
        return bitrate;
    }

    public void setBitrate(double bitrate) {
        this.bitrate = bitrate;
    }

//    public int getNumberOfRetransmission() {
//        return numberOfRetransmission;
//    }
//
//    public void setNumberOfRetransmission(int numberOfRetransmission) {
//        this.numberOfRetransmission = numberOfRetransmission;
//    }
}
