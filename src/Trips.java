import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class Trips {
    private ZonedDateTime startedTimeUTC;
    private ZonedDateTime finishedTimeUTC;
    private Long durationSecs;
    private String fromStopId;
    private String toStopId;
    private Double chargeAmount;
    private String companyId;
    private String busID;
    private String pan;
    private String status;

    Trips(ZonedDateTime startedTimeUTC, ZonedDateTime finishedTimeUTC, Long durationSecs,
        String fromStopId, String toStopId, Double chargeAmount,
        String companyId, String busID, String pan, String status){
            this.startedTimeUTC = startedTimeUTC;
            this.finishedTimeUTC = finishedTimeUTC;
            this.durationSecs = durationSecs;
            this.fromStopId = fromStopId;
            this.toStopId = toStopId;
            this.chargeAmount = chargeAmount;
            this.companyId = companyId;
            this.busID = busID;
            this.pan = pan;
            this.status = status;
    }
    
    public ZonedDateTime getStartedTimeUTC() {
        return startedTimeUTC;
    }

    public void setStartedTimeUTC(ZonedDateTime startedTimeUTC) {
        this.startedTimeUTC = startedTimeUTC;
    }

    public ZonedDateTime getFinishedTimeUTC() {
        return finishedTimeUTC;
    }

    public void setFinishedTimeUTC(ZonedDateTime finishedTimeUTC) {
        this.finishedTimeUTC = finishedTimeUTC;
    }

    public Long getDurationSecs() {
        return durationSecs;
    }

    public void setDurationSecs(Long durationSecs) {
        this.durationSecs = durationSecs;
    }

    public String getFromStopId() {
        return fromStopId;
    }

    public void setFromStopId(String fromStopId) {
        this.fromStopId = fromStopId;
    }

    public String getToStopId() {
        return toStopId;
    }

    public void setToStopId(String toStopId) {
        this.toStopId = toStopId;
    }

    public Double getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(Double chargeAmount) {
        this.chargeAmount = chargeAmount;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static String getHeadersCSVString(){
        return "Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status";
    }
    public String toCSVString() {
        String csvString = "";
        if(startedTimeUTC != null)
            csvString += startedTimeUTC.format(DateTimeFormatter.ofPattern(App.DEFAULT_DATE_FORMATTER_STRING));
        csvString += ",";
        if(finishedTimeUTC != null)
            csvString += finishedTimeUTC.format(DateTimeFormatter.ofPattern(App.DEFAULT_DATE_FORMATTER_STRING));
        csvString += ",";
        if(durationSecs != null)
            csvString += durationSecs.intValue();
        csvString += ",";
        if(fromStopId != null)
            csvString += fromStopId;
        csvString += ",";
        if(toStopId != null)
            csvString += toStopId;
        csvString += ",";
        if(chargeAmount != null)
            csvString += chargeAmount;
        csvString += ",";
        if(companyId != null)
            csvString += companyId;
        csvString += ",";
        if(busID != null)
            csvString += busID;
        csvString += ",";
        if(pan != null)
            csvString += pan;
        csvString += ",";
        if(status != null)
            csvString += status;
        return  csvString;
    }

    @Override
    public String toString() {
        return "{\"startedTimeUTC\":\"" + startedTimeUTC + "\", \"finishedTimeUTC\":\"" + finishedTimeUTC
                + "\", \"durationSecs\":\"" + durationSecs + "\", \"fromStopId\":\"" + fromStopId
                + "\", \"toStopId\":\"" + toStopId + "\", \"chargeAmount\":\"" + chargeAmount + "\", \"companyId\":\""
                + companyId + "\", \"busID\":\"" + busID + "\", \"pan\":\"" + pan + "\", \"status\":\"" + status
                + "\"}";
    }
}

class SortTripsByDateTimeUTC implements Comparator<Trips>
{

    // Used for sorting the order of trips
    public int compare(Trips a, Trips b)
    {    
        System.out.println(a.toString() + "\t" + b.toString());
        if(a.getStartedTimeUTC() != null && b.getStartedTimeUTC() != null){
            return a.getStartedTimeUTC().compareTo(b.getStartedTimeUTC());
        }
        else if(a.getFinishedTimeUTC() != null && b.getFinishedTimeUTC() != null){
            return a.getFinishedTimeUTC().compareTo(b.getFinishedTimeUTC());
        }
        else{
            return a.getBusID().compareTo(b.getBusID());
        }
    }
}