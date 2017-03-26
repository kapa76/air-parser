package ru.air.parser.asia;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.air.common.AirportEnum;
import ru.air.common.ArrivalStatus;
import ru.air.entity.FlightAD;
import ru.air.entity.FlightDetail;
import ru.air.loader.BaseLoader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ChhatrapatiLoader extends BaseLoader {

    private String url = "http://www.csia.in/flightinformation/passenger-flight.aspx";
    private String outputTimePattern = "yyyy-MM-d HH:mm";

    public ChhatrapatiLoader(AirportEnum airportEnum) {
        super(airportEnum, true);
    }

    @Override
    public FlightAD load() {
        FlightAD flight = new FlightAD();
        flight.setAirportId(getAirport().getAirportId());
        flight.setArrivals(loadParseArrival(url));
        flight.setDeparture(loadParseDeparture(url));
        return flight;
    }

    private List<FlightDetail> loadParseDeparture(String url) {
        List<FlightDetail> fd = new ArrayList<>();
        try {
            HtmlPage page = getWebClient().getPage(url);
            HtmlAnchor next = page.getAnchorByText("Departure");
            page = next.click();

            HtmlAnchor domestic = page.getAnchorByText("Domestic");
            page = domestic.click();
            fd.addAll(parseDomestic(page.asXml()));

            HtmlAnchor international = page.getAnchorByText("International");
            page = international.click();
            fd.addAll(parseDomestic(page.asXml()));
        } catch (Exception exception) {

        }
        return fd;
    }

    private Collection<? extends FlightDetail> parseDomestic(String body) {
        List<FlightDetail> fdl = new ArrayList<>();

        Document doc = Jsoup.parse(body);
        Elements rows = doc.select("table#GridViewFlightDetail").select("tbody").select("tr");

        for (int i = 0; i < rows.size(); i++) {
            Elements tds = rows.get(i).select("td");

            FlightDetail fd = new FlightDetail();

            fd.setFlightNumber(tds.get(2).text());
            String schedulerTime = getTime(tds.get(5).text());
            String actualTime = getTime(tds.get(6).text());
            String status = tds.get(7).text();

            fd.setActual(actualTime);
            fd.setScheduled(schedulerTime);

            if (status.contains("Baggage Hall") || status.contains("Landed") ||
                    status.contains("Arrived") || status.contains("Early Arrival")) {
                fd.setStatus(ArrivalStatus.LANDED);
            } else if (status.contains("Estimated")) {
                fd.setStatus(ArrivalStatus.EXPECTED);
            } else if (status.contains("Delayed")) {
                fd.setStatus(ArrivalStatus.DELAYED);
            } else if (status.contains("Non Operational")) {
                fd.setStatus(ArrivalStatus.CANCELLED);
            } else if (status.contains("Scheduled")) {
                fd.setStatus(ArrivalStatus.SCHEDULED);
            } else if (status.contains("Departed")) {
                fd.setStatus(ArrivalStatus.DEPARTED);
            } else if (status.contains("Final Call")) {
                fd.setStatus(ArrivalStatus.DEPARTED);
            } else if (status.contains("Gate Closed")) {
                fd.setStatus(ArrivalStatus.DEPARTED);
            } else if (status.contains("Security")) {
                fd.setStatus(ArrivalStatus.DEPARTED);
            } else if (status.contains("Boarding")) {
                fd.setStatus(ArrivalStatus.DEPARTED);
            }
            fdl.add(fd);
        }

        return fdl;
    }

    private String getTime(String time) {
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(outputTimePattern);

        if (time.length() >= 5) {
            String[] times = time.split(":");

            ldt = ldt.withHour(Integer.parseInt(times[0]));
            ldt = ldt.withMinute(Integer.parseInt(times[1]));

            return ldt.format(fmt) + ":00";
        } else {
            return "";
        }
    }

    private List<FlightDetail> loadParseArrival(String url) {
        List<FlightDetail> fd = new ArrayList<>();
        try {
            HtmlPage page = getWebClient().getPage(url);
            HtmlAnchor next = page.getAnchorByText("Arrival");
            page = next.click();

            HtmlAnchor domestic = page.getAnchorByText("Domestic");
            page = domestic.click();
            fd.addAll(parseDomestic(page.asXml()));

            HtmlAnchor international = page.getAnchorByText("International");
            page = international.click();
            fd.addAll(parseDomestic(page.asXml()));
        } catch (Exception exception) {

        }
        return fd;
    }
}
