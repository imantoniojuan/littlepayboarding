import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    final static ZoneId DEFAULT_ZONE_ID = ZoneId.of("UTC");
    final static String DEFAULT_DATE_FORMATTER_STRING = "dd-MM-yyyy HH:mm:ss";
    final static String DEFAULT_PRICES_PATH = "prices.csv";
    final static String DEFAULT_TAPS_PATH = "taps.csv";
    final static String DEFAULT_TRIPS_PATH = "trips.csv";
    final static Integer MAX_BUS_RIDE_HOURS = 5;
    
    /**
    * main application which allows for input parameters for dictionary and input files as well as system console input
    */
    public static void main(String[] args) throws Exception {
        String pricesFilePath = DEFAULT_PRICES_PATH;
        String tapsFilePath = DEFAULT_TAPS_PATH;
        String tripsFilePath = DEFAULT_TRIPS_PATH;
        for(int i=0; i<args.length; i++){
            if(args[i].equals("-prices") || args[i].equals("-p")){
                i++;
                pricesFilePath = args[i];
            }
            else if(args[i].equals("-taps") || args[i].equals("-i")){
                i++;
                tapsFilePath = args[i];
            }
            else if(args[i].equals("-trips") || args[i].equals("-o")){
                i++;
                tripsFilePath = args[i];
            }
            else if(args[i].equals("-help") || args[i].equals("-h")){
                String help = "usage: [-p prices.csv] [-i taps.csv] [-o trips.csv]";
                System.out.println(help);
            }
        }

        App app = new App(pricesFilePath, tapsFilePath, tripsFilePath);
        app.processTaps();
        app.outputTapsResultToFile();
    }

    private List<Taps> taps;
    private Prices prices;
    private String tripsFilePath;
    private List<Trips> trips;

    App(String pricesFilePath, String tapsFilePath, String tripsFilePath){
        this.taps = Utilities.readTapsListFromFile(tapsFilePath);
        // System.out.println(taps);
        this.prices = new Prices(Utilities.readStringListFromFile(pricesFilePath));
        // System.out.println(prices.getPrice(1, 2));
        this.tripsFilePath = tripsFilePath;
        this.trips = new ArrayList<Trips>();
    }

    public void processTaps(){
        Map<String,List<Taps>> panToTapsMap = new HashMap<String,List<Taps>>();
        for(Taps tap:taps){
            List<Taps> tapsList = panToTapsMap.get(tap.getPan());
            if(tapsList == null)
                tapsList = new ArrayList<Taps>();
            tapsList.add(tap);
            Collections.sort(tapsList, new SortTapsByDateTimeUTC());
            panToTapsMap.put(tap.getPan(), tapsList);
        }
        // System.out.println(panToTapsMap);
        for(String pan:panToTapsMap.keySet()){
            System.out.println("Processing PAN: " + pan);
            List<Taps> tapsList = panToTapsMap.get(pan);
            int tapCounter = 0;
            Taps previousTap = tapsList.get(tapCounter);
            for(;tapCounter<tapsList.size();tapCounter++){
                Trips trip = null;
                Taps currentTap = tapsList.get(tapCounter);
                if(tapCounter == 0){
                    // Initial data start with a Tap OFF
                    if(currentTap.getTapType().toUpperCase().equals("OFF")){
                        trip = new Trips(null,currentTap.getZonedDateTimeUTC(),null,
                        null,currentTap.getStopId(),prices.getMaxPrice(currentTap.getStopIdNum()),
                        currentTap.getCompanyId(),currentTap.getBusID(),pan,"INCOMPLETE");
                        trips.add(trip);
                    }
                }
                else{
                    // Current Tap OFF
                    if(currentTap.getTapType().toUpperCase().equals("OFF")){
                        // Previous Tap ON
                        if(previousTap.getTapType().toUpperCase().equals("ON")){
                            // Cancelled if end up in same stop
                            if(currentTap.getStopId().equals(previousTap.getStopId())){
                                trip = new Trips(previousTap.getZonedDateTimeUTC(),currentTap.getZonedDateTimeUTC(),
                                currentTap.getZonedDateTimeUTC().toEpochSecond()-previousTap.getZonedDateTimeUTC().toEpochSecond(),
                                previousTap.getStopId(),currentTap.getStopId(),prices.getPrice(previousTap.getStopIdNum(), currentTap.getStopIdNum()),
                                currentTap.getCompanyId(),currentTap.getBusID(),pan,"CANCELLED");
                                trips.add(trip);
                            }
                            // Validate if same trip
                            else if(currentTap.getBusID().equals(previousTap.getBusID()) && currentTap.getCompanyId().equals(previousTap.getCompanyId())){
                                // If current tap exceeds Max Bus Ride Hours, could mean
                                // Rider forgets to Tap OFF on Trip A then forgets to TAP ON on Trip B on the SAME Bus
                                // this will result 2 incomplete trips
                                // MAX_BUS_RIDE_HOURS is assumed to be 5 hours which could be either
                                // - The next day
                                // - Max time taken for a full round trip for the bus such that the same rider could ride the same bus again
                                if((currentTap.getZonedDateTimeUTC().toEpochSecond()-previousTap.getZonedDateTimeUTC().toEpochSecond()) > (3600*MAX_BUS_RIDE_HOURS)){
                                    // Incomplete previous trip
                                    trip = new Trips(previousTap.getZonedDateTimeUTC(),null,null,
                                    previousTap.getStopId(),null,prices.getMaxPrice(previousTap.getStopIdNum()),
                                    previousTap.getCompanyId(),previousTap.getBusID(),pan,"INCOMPLETE");
                                    trips.add(trip);

                                    // Incomplete current trip
                                    trip = new Trips(null,currentTap.getZonedDateTimeUTC(),null,
                                    null,currentTap.getStopId(),prices.getMaxPrice(currentTap.getStopIdNum()),
                                    currentTap.getCompanyId(),currentTap.getBusID(),pan,"INCOMPLETE");
                                    trips.add(trip);
                                }
                                else{
                                    trip = new Trips(previousTap.getZonedDateTimeUTC(),currentTap.getZonedDateTimeUTC(),
                                    currentTap.getZonedDateTimeUTC().toEpochSecond()-previousTap.getZonedDateTimeUTC().toEpochSecond(),
                                    previousTap.getStopId(),currentTap.getStopId(),prices.getPrice(previousTap.getStopIdNum(), currentTap.getStopIdNum()),
                                    currentTap.getCompanyId(),currentTap.getBusID(),pan,"COMPLETED");
                                    trips.add(trip);
                                }
                            }
                            else{
                                // Rider forgets to Tap OFF on Trip A then forgets to TAP ON on Trip B on the DIFFERENT Bus
                                // this will result 2 incomplete trips

                                // Incomplete previous trip
                                trip = new Trips(previousTap.getZonedDateTimeUTC(),null,null,
                                previousTap.getStopId(),null,prices.getMaxPrice(previousTap.getStopIdNum()),
                                previousTap.getCompanyId(),previousTap.getBusID(),pan,"INCOMPLETE");
                                trips.add(trip);

                                // Incomplete current trip
                                trip = new Trips(null,currentTap.getZonedDateTimeUTC(),null,
                                null,currentTap.getStopId(),prices.getMaxPrice(currentTap.getStopIdNum()),
                                currentTap.getCompanyId(),currentTap.getBusID(),pan,"INCOMPLETE");
                                trips.add(trip);
                            }
                        }

                        // Previous Tap OFF
                        if(previousTap.getTapType().toUpperCase().equals("OFF")){
                            // Rider Tap OFF on Trip A then forgets to TAP ON on Trip B but remembers to TAP ON Trip B
                            // this will result 1 incomplete trip

                            // Incomplete current trip
                            trip = new Trips(null,currentTap.getZonedDateTimeUTC(),null,
                            null,currentTap.getStopId(),prices.getMaxPrice(currentTap.getStopIdNum()),
                            currentTap.getCompanyId(),currentTap.getBusID(),pan,"INCOMPLETE");
                            trips.add(trip);
                        }
                    }

                     // Current Tap ON
                    if(currentTap.getTapType().toUpperCase().equals("ON")){
                        // Previous Tap ON
                        if(previousTap.getTapType().toUpperCase().equals("ON")){
                            if(!currentTap.getStopId().equals(previousTap.getStopId())){
                                // Rider Tap ON on Trip A then forgets to TAP OFF on Trip B but remembers to TAP ON Trip B
                                // this will result 1 incomplete trip

                                // Incomplete previous trip
                                trip = new Trips(previousTap.getZonedDateTimeUTC(),null,null,
                                previousTap.getStopId(),null,prices.getMaxPrice(previousTap.getStopIdNum()),
                                previousTap.getCompanyId(),previousTap.getBusID(),pan,"INCOMPLETE");
                                trips.add(trip);
                            }
                        }
                        // Previous Tap OFF
                        if(previousTap.getTapType().toUpperCase().equals("OFF")){
                            if(tapCounter+1>=tapsList.size()){
                                // Since its last record, and the trip ends with TAP ON
                                // this will result 1 incomplete trip

                                // Incomplete current trip
                                trip = new Trips(null,currentTap.getZonedDateTimeUTC(),null,
                                null,currentTap.getStopId(),prices.getMaxPrice(currentTap.getStopIdNum()),
                                currentTap.getCompanyId(),currentTap.getBusID(),pan,"INCOMPLETE");
                                trips.add(trip);
                            }
                            else{
                                // No logic as the next step will process the record.
                            }
                        }
                    }
                }
                previousTap = currentTap;
            }
        }
        Collections.sort(trips, new SortTripsByDateTimeUTC());
    }

    void outputTapsResultToFile(){
        List<String> results = new ArrayList<String>();
        results.add(Trips.getHeadersCSVString());
        for(Trips trip:trips){
            results.add(trip.toCSVString());
        }
        Utilities.writeToFileFromStringList(tripsFilePath,results);
    }
}
