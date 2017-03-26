package ru.air.parser.usa;

import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.air.common.AirportEnum;
import ru.air.entity.FlightAD;
import ru.air.entity.FlightDetail;
import ru.air.loader.BaseLoader;

import java.util.*;

/**
 * Created by Admin on 23.03.2017.
 */
public class AtlantaLoader extends BaseLoader {

    private String outputTimePattern = "yyyy-MM-d HH:mm";
    private String formatterUrlDate = "yyyy-MM-d";

    private String url = "http://apps.atl.com/Passenger/FlightInfo/Search.aspx?FIDSType=A&SearchAirline=&SearchFlight=&SearchCity=";

    private List<String> timePeriod = new ArrayList<>();
    private List<String> airLineList = new ArrayList<>();


    public AtlantaLoader(AirportEnum airport) {
        super(airport, true);
    }

    public FlightAD load() {
        FlightAD flight = new FlightAD();
        flight.setAirportId(getAirport().getAirportId());

        timePeriod.addAll(getTimePeriod());
        airLineList.addAll(getAirlineList());

        loadParseArrival(flight);
        loadParseDeparture(flight);
        return flight;
    }

    private void loadParseDeparture(FlightAD flights) {
        int currentPage = 1;
        try {
            HtmlPage page = getWebClient().getPage(url);

            for (String period : timePeriod) {
                for (String airLine : airLineList) {

                    while (true) {

                        HtmlSelect changeAirline = (HtmlSelect) page.getElementById("bodySection_dplAirlineID1");
                        HtmlOption air = changeAirline.getOptionByValue(airLine);
                        page = changeAirline.setSelectedAttribute(air, true);

                        HtmlSelect changeTimePeriod = (HtmlSelect) page.getElementById("bodySection_dplFlightTime");
                        HtmlOption time = changeTimePeriod.getOptionByValue(period);
                        page = changeTimePeriod.setSelectedAttribute(time, true);

                        HtmlSubmitInput submitButton = (HtmlSubmitInput) page.getElementById("bodySection_btngetflights");
                        page = submitButton.click();
                        String body = page.asXml();

                        flights.getArrivals().addAll(parseDeparture(body));
                        String nextUrl = getNextHref(body, currentPage);

                        if (nextUrl.length() > 0) {
                            HtmlAnchor htmlAnchor = page.getAnchorByHref(nextUrl);
                            page = htmlAnchor.click();
                            currentPage++;
                        } else {
                            break;
                        }
                    }
                }
            }
        } catch (Exception exception) {

        }
    }

    private void loadParseArrival(FlightAD flights) {
        int currentPage = 1;
        try {
            HtmlPage page = getWebClient().getPage(url);

            for (String period : timePeriod) {
                for (String airLine : airLineList) {

                    while (true) {

                        HtmlSelect changeAirline = (HtmlSelect) page.getElementById("bodySection_dplAirlineID1");
                        HtmlOption air = changeAirline.getOptionByValue(airLine);
                        page = changeAirline.setSelectedAttribute(air, true);

                        HtmlSelect changeTimePeriod = (HtmlSelect) page.getElementById("bodySection_dplFlightTime");
                        HtmlOption time = changeTimePeriod.getOptionByValue(period);
                        page = changeTimePeriod.setSelectedAttribute(time, true);

                        HtmlSubmitInput submitButton = (HtmlSubmitInput) page.getElementById("bodySection_btngetflights");
                        page = submitButton.click();
                        String body = page.asXml();

                        flights.getArrivals().addAll(parseArrival(body));
//                flights.getDeparture().addAll(parseDeparture(body));

                        String nextUrl = getNextHref(body, currentPage);
                        if (nextUrl.length() > 0) {
                            HtmlAnchor htmlAnchor = page.getAnchorByHref(nextUrl);
                            page = htmlAnchor.click();
                            currentPage++;
                        } else {
                            break;
                        }
                    }
                }
            }
        } catch (Exception exception) {

        }

    }

    private String getNextHref(String body, int currentPage) {
        String href = "";
        Document doc = Jsoup.parse(body);
        Elements rows = doc.select("table#GridClassArrival").select("tbody").select("tr");
        Element elem = rows.get(rows.size() - 1);
        Elements r = elem.select("td");

        if (r.size() == 1) {
            Element td = r.select("table").select("tbody").select("tr").get(0);
            Elements next = td.select("td");

            if (next.size() > currentPage) {
                href = next.get(currentPage).select("a").attr("href");
            } else {
                return "";
            }
        } else {
            return "";
        }

        return href;
    }

    private Collection<? extends FlightDetail> parseArrival(String body) {
        List<FlightDetail> fdl = new ArrayList<>();

        Document doc = Jsoup.parse(body);
        Elements rows = doc.select("table.GridClassArrival").select("tbody").select("tr");
        for (int i = 0; i < rows.size(); i++) {
            Elements tds = rows.get(i).select("td");
            if (tds.size() > 1 && !tds.get(0).text().contains("There are currently") ) {
                FlightDetail fd = new FlightDetail();

                String flightNumber = tds.get(0).text();
                String shedulerTime = tds.get(2).text();
                String actualTime = tds.get(3).text();
                String status = tds.get(4).text();

            }
        }

        return null;


    }

    private Collection<? extends FlightDetail> parseDeparture(String body) {
        List<FlightDetail> fdl = new ArrayList<>();

        Document doc = Jsoup.parse(body);
        Elements rows = doc.select("table.GridClassDeparture").select("tbody").select("tr");
        for (int i = 0; i < rows.size(); i++) {
            Elements tds = rows.get(i).select("td");
            if (tds.size() > 1 && !tds.get(0).text().contains("There are currently") ) {
                FlightDetail fd = new FlightDetail();

                String flightNumber = tds.get(0).text();
                String shedulerTime = tds.get(2).text();
                String actualTime = tds.get(3).text();
                String status = tds.get(4).text();

            }
        }

        return null;


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
