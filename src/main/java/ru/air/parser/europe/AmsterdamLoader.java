package ru.air.parser.europe;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.air.common.AirportEnum;
import ru.air.common.ArrivalStatus;
import ru.air.entity.FlightAD;
import ru.air.entity.FlightDetail;
import ru.air.loader.PageLoader;
import ru.air.loader.BaseLoader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Admin on 18.03.2017.
 */
public class AmsterdamLoader extends BaseLoader {

    private String arrivalUrl = "https://www.schiphol.nl/en/arrivals/list/?datetime=";
    private String departuresUrl = "https://www.schiphol.nl/en/departures/list/?datetime=";
    private String outputTimePattern = "yyyy-MM-d HH:mm";
    private List<String> params = Arrays.asList("00%3A00%3A00", "04%3A00%3A00", "08%3A00%3A00", "12%3A00%3A00", "16%3A00%3A00", "20%3A00%3A00");

    private String formatterUrlDate = "yyyy-MM-d";

    public AmsterdamLoader(AirportEnum airport) {
        super(airport);
    }

    public FlightAD load() {
        FlightAD flight = new FlightAD();
        flight.setAirportId(getAirport().getAirportId());
        flight.setArrivals(loadParse(arrivalUrl, true));
        flight.setDeparture(loadParse(departuresUrl, false));
        return flight;
    }

    private List<FlightDetail> loadParse(String url, boolean b) {
        List<FlightDetail> fdList = new ArrayList<>();
        List<String> paramsDate = new ArrayList<>();
        LocalDateTime ldt = LocalDateTime.now();

        paramsDate.add(generateStringFromDate(ldt.minusDays(1)));
        paramsDate.add(generateStringFromDate(ldt));
        paramsDate.add(generateStringFromDate(ldt.plusDays(1)));

        for (String date : paramsDate) {
            for (String valueHours : params) {
                fdList.addAll(parser(date, valueHours, url, b));
            }
        }

        return fdList;
    }

    private Collection<? extends FlightDetail> parser(String date, String valueHours, String url, boolean b) {
        List<FlightDetail> fds = new ArrayList<>();
        String requestUrl = url + date + "+" + valueHours;
        String body = PageLoader.Loader(requestUrl);
        org.jsoup.nodes.Document doc = Jsoup.parse(body);
        Elements rows = null;

        if(b) {
            rows = doc.select("table.table-flights.table-flights--xl.table-flights--arrival").select("tbody");
        } else {
            rows = doc.select("table.table-flights.table-flights--xl.table-flights--departure").select("tbody");
        }
        for (int i = 0; i < rows.size(); i++) {
            FlightDetail fd = new FlightDetail();
            String flightNumber = rows.get(i).select("tr").get(0).select("th.table-flights__number").text();
            String status = rows.get(i).select("tr").get(0).select("td.table-flights__message").text();

            Element elem = rows.get(i).select("tr").get(0).select("th.table-flights__time").select("div.flight-times").first();

            String schedulerTime = "";
            String actualTime = "";

            schedulerTime = elem.select("del").select("time").text();
            actualTime = elem.select("ins").select("time").text();
            if (schedulerTime.length() == 0) {
                schedulerTime = elem.select("time").text();
            }

            fd.setFlightNumber(flightNumber);
            fd.setScheduled(getDateTime(date, schedulerTime));
            if (actualTime.length() >= 5) {
                fd.setActual(getDateTime(date, actualTime));
            }

            if (status.contains("Landed")) {
                fd.setStatus(ArrivalStatus.LANDED);
            } else if (status.contains("Gate changed")) {
                fd.setStatus(ArrivalStatus.TRANSFERED);
            } else if (status.contains("Departed")) {
                fd.setStatus(ArrivalStatus.DEPARTED);
            } else if (status.equals("On schedule")) {
                fd.setStatus(ArrivalStatus.SCHEDULED);
            } else if (status.contains("Delayed")) {
                fd.setStatus(ArrivalStatus.DELAYED);
            } else if (status.equals("Cancelled")) {
                fd.setStatus(ArrivalStatus.CANCELLED);
            } else if (status.contains("Arrives early")) {
                fd.setStatus(ArrivalStatus.LANDED);
            } else if (status.contains("Wait in lounge")) {
                fd.setStatus(ArrivalStatus.EXPECTED);
            }

            fds.add(fd);
        }


        return fds;
    }

    private String getDateTime(String formatDate, String time) {
        String[] times = time.split(":");

        String[] ddt = formatDate.split("-");
        LocalDateTime ldt = LocalDateTime.of(Integer.parseInt(ddt[0]), Integer.parseInt(ddt[1]), Integer.parseInt(ddt[2]),
                Integer.parseInt(times[0]), Integer.parseInt(times[1]));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(outputTimePattern);
        return ldt.format(fmt) + ":00";
    }

    private String generateStringFromDate(LocalDateTime dt) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(formatterUrlDate);
        return dt.format(fmt);
    }


}
