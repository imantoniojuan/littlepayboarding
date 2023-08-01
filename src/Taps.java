import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class Taps {
    private String id;
    private String dateTimeUTC;
    private ZonedDateTime zonedDateTimeUTC;
    private String tapType;
    private String stopId;
    private Integer stopIdNum;
    private String companyId;
    private String busID;
    private String pan;

    Taps(){
    }

    Taps(String tapsStr){
        String[] tapsArr = tapsStr.split(",");
        this.id  = tapsArr[0].trim();
        this.dateTimeUTC = tapsArr[1].trim();
        if(this.dateTimeUTC==null || this.dateTimeUTC .isEmpty())
            this.zonedDateTimeUTC = ZonedDateTime.now();
        else
            this.zonedDateTimeUTC = ZonedDateTime.parse(this.dateTimeUTC,DateTimeFormatter.ofPattern(App.DEFAULT_DATE_FORMATTER_STRING).withZone(App.DEFAULT_ZONE_ID));
        this.tapType = tapsArr[2].trim();
        this.stopId = tapsArr[3].trim();
        this.stopIdNum = Integer.parseInt(this.stopId.replaceFirst("Stop","").trim());
        this.companyId = tapsArr[4].trim();
        this.busID = tapsArr[5].trim();
        this.pan = tapsArr[6].trim();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateTimeUTC() {
        return dateTimeUTC;
    }

    public void setDateTimeUTC(String dateTimeUTC) {
        this.dateTimeUTC = dateTimeUTC;
    }

    public ZonedDateTime getZonedDateTimeUTC() {
        return zonedDateTimeUTC;
    }

    public void setZonedDateTimeUTC(ZonedDateTime zonedDateTimeUTC) {
        this.zonedDateTimeUTC = zonedDateTimeUTC;
    }

    public String getTapType() {
        return tapType;
    }

    public void setTapType(String tapType) {
        this.tapType = tapType;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public Integer getStopIdNum() {
        return stopIdNum;
    }

    public void setStopIdNum(Integer stopIdNum) {
        this.stopIdNum = stopIdNum;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getBusID() {
        return busID;
    }

    public void setBusID(String busID) {
        this.busID = busID;
    }
    
    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\", \"dateTimeUTC\":\"" + dateTimeUTC + "\", \"zonedDateTimeUTC\":\""
                + zonedDateTimeUTC + "\", \"tapType\":\"" + tapType + "\", \"stopId\":\"" + stopId
                + "\", \"stopIdNum\":\"" + stopIdNum + "\", \"companyId\":\"" + companyId + "\", \"busID\":\"" + busID
                + "\", \"pan\":\"" + pan + "\"}";
    }
}

class SortTapsByDateTimeUTC implements Comparator<Taps>
{
    // Used for sorting in ascending order of tap datetime
    public int compare(Taps a, Taps b)
    {
        return a.getZonedDateTimeUTC().compareTo(b.getZonedDateTimeUTC());
    }
}