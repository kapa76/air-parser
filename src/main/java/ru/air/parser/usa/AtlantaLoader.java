package ru.air.parser.usa;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.air.common.AirportEnum;
import ru.air.entity.FlightAD;
import ru.air.entity.FlightDetail;
import ru.air.loader.PageLoader;
import ru.air.loader.BaseLoader;

import java.util.*;

/**
 * Created by Admin on 23.03.2017.
 */
public class AtlantaLoader extends BaseLoader {

    private String outputTimePattern = "yyyy-MM-d HH:mm";
    private String formatterUrlDate = "yyyy-MM-d";

    private String temporaryUrl = "http://apps.atl.com/Passenger/FlightInfo/Search.aspx?FIDSType=A&SearchAirline=&SearchFlight=&SearchCity=";

    private List<String> timePeriod = new ArrayList<>();
    private List<String> airLineList = new ArrayList<>();


    public AtlantaLoader(AirportEnum airport) {
        super(airport);
    }

    public FlightAD load() {
        FlightAD flight = new FlightAD();
        flight.setAirportId(getAirport().getAirportId());

        timePeriod.addAll(getTimePeriod());
        airLineList.addAll(getAirlineList());

        loadParse(flight);
        //flight.setDeparture(loadParse(departuresUrl, false));
        return flight;
    }

    private void loadParse(FlightAD flights) {
        for (String period : timePeriod) {
            for (String airLine : airLineList) {
                Map<String, String> params = new HashMap<>();
                params.put("ctl00$bodySection$dplAirlineID1", airLine);
                params.put("ctl00$bodySection$dplFlightTime", period);

                params.put("ctl00$bodySection$btngetflights", "FILTER RESULTS");
                params.put("__ASYNCPOST", "false");
                params.put("ctl00$tbSearchText", "");
                params.put("ctl00$bodySection$wucFlightInfo$txtAirline", "");
                params.put("ctl00$bodySection$wucFlightInfo$txtAirlineCode", "");
                params.put("ctl00$bodySection$wucFlightInfo$txtFlightID", "");
                params.put("ctl00$bodySection$wucFlightInfo$MaskedEditExtender_FlightNo_ClientState", "");
                params.put("ctl00$bodySection$wucFlightInfo$txtFlightCity", "");
                params.put("ctl00$bodySection$rblArchieved", "1");
                params.put("ctl00$bodySection$dplSortedBy", "F_City");
                params.put("ctl00$bodySection$dplCity1", "");
                params.put("ctl00$bodySection$dplFlightNumber1", "0");
                params.put("ctl00$ScriptManager", "ctl00$ScriptManager|ctl00$bodySection$btngetflights");
                params.put("bodySection_TabContainer_Flights_ClientState", "{\"ActiveTabIndex\":1,\"TabEnabledState\":[true,true],\"TabWasLoadedOnceState\":[true,false]}");
                params.put("__EVENTTARGET", "ctl00$bodySection$TabContainer_Flights$tabArrival$gridArrivalFlights");

                String body = PageLoader.LoaderPostFilterAtlanta(temporaryUrl, params);
                flights.getArrivals().addAll(parseArrival(body));
//                flights.getDeparture().addAll(parseDeparture(body));
            }
        }
    }

    private Collection<? extends FlightDetail> parseArrival(String body) {
        String buildBody = buildBodyFromStrings(body);

        Document doc = Jsoup.parse(buildBody);
        Elements trs1 = doc.select("table.GridClassArrival").select("tr");
        Elements trs2 = doc.select("table.GridClassDeparture").select("tr");

        return null;
    }

    private String buildBodyFromStrings(String body) {
        String[] strArray = body.split("\r\n");
        String value = "";
        for (String temp : strArray) {
            if (!temp.contains("|")) {
                value += temp;
            }

        }
        return value;
    }

    private List<String> getAirlineList() {
        return Arrays.asList("AC", "AF", "AS", "AA", "BA", "DL", "F9", "KL", "KE", "LH", "QR", "WN", "NK", "TK", "UA", "VS");
    }

    private List<String> getTimePeriod() {
        return Arrays.asList(
                "5", "6", "7", "8", "9", "10", "11", "12",
                "13", "14", "15", "16", "17", "18", "19", "20",
                "21", "22", "23");
    }


}
