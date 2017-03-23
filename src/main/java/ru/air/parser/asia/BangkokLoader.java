package ru.air.parser.asia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import java.util.Collection;
import java.util.List;

/**
 * Created by Admin on 19.03.2017.
 */
public class BangkokLoader extends BaseLoader {

    private String arrivalUrl = "http://www.suvarnabhumiairport.com/en/3-passenger-arrivals";
    private String departuresUrl = "http://www.suvarnabhumiairport.com/en/4-passenger-departures";
    private String outputTimePattern = "yyyy-MM-d HH:mm";
    private String formatterUrlDate = "yyyy-MM-d";

    public BangkokLoader(AirportEnum airport) {
        super(airport);
    }

    public FlightAD load() {
        FlightAD flight = new FlightAD();
        flight.setAirportId(getAirport().getAirportId());
        flight.setArrivals(loadParse(arrivalUrl, true));
        flight.setDeparture(loadParse(departuresUrl, false));
        return flight;
    }

    private List<FlightDetail> loadParse(String requestUrl, boolean b) {
        List<FlightDetail> fdList = new ArrayList<>();
        List<String> paramsDate = new ArrayList<>();
        LocalDateTime ldt = LocalDateTime.now();

        paramsDate.add(generateStringFromDate(ldt.minusDays(1)));
        paramsDate.add(generateStringFromDate(ldt));
        paramsDate.add(generateStringFromDate(ldt.plusDays(1)));

        for (String date : paramsDate) {
            fdList.addAll(parser(date, requestUrl, b));
        }

        return fdList;
    }

    private Collection<? extends FlightDetail> parser(String date, String requestUrl, boolean b) {
        List<FlightDetail> fds = new ArrayList<>();
        String url = requestUrl + "?after_search=1&page=&date=" + date + "&airport=0&flight_no=&start_time=00%3A00&end_time=23%3A59&airline=0";
        String body = PageLoader.Loader(url);
        Document doc = Jsoup.parse(body);
        Elements rows = null;

        if (b) {
            rows = doc.select("table.table-flight-tbl.table.table-striped.flightstatus").select("tbody").select("tr");
        } else {
            rows = doc.select("table.table-flight-tbl.table.table-striped.flightstatus").select("tbody").select("tr");
        }

        for (int i = 0; i < rows.size(); i++) {
            Element row = rows.get(i);
            String attr = row.attr("id");
            if (attr.length() > 0) {
                Elements tds = row.select("td");
                FlightDetail fd = new FlightDetail();

                String flightNumber = tds.get(1).text();
                String schedulerTime = tds.get(2).text();
                String actualTime = tds.get(3).text();
                String status = tds.get(6).text();

                fd.setFlightNumber(flightNumber);
                fd.setScheduled(getDateTime(date, schedulerTime));
                if (actualTime.length() >= 5) {
                    fd.setActual(getDateTime(date, actualTime));
                }

                if (status.contains("Landed")) {
                    fd.setStatus(ArrivalStatus.LANDED);
                } else if (status.contains("Canceled")) {
                    fd.setStatus(ArrivalStatus.CANCELLED);
                } else if (status.contains("Confirmed")) {
                    fd.setStatus(ArrivalStatus.DELAYED);
                } else if (status.contains("Departed")) {
                    fd.setStatus(ArrivalStatus.DEPARTED);
                } else {
                    if (!b) {
                        fd.setStatus(ArrivalStatus.SCHEDULED);
                    } else {
                        fd.setStatus(ArrivalStatus.SCHEDULED);
                    }
                }

                fds.add(fd);
            }
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



