package ru.air.parser.asia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.air.common.AirportEnum;
import ru.air.common.ArrivalStatus;
import ru.air.entity.FlightAD;
import ru.air.entity.FlightDetail;
import ru.air.loader.BaseLoader;
import ru.air.loader.PageLoader;
import ru.air.parser.xml.OnlineArrive;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GuangzhouLoader extends BaseLoader {

    private String urlArrive = "http://www.gbiac.net/en/hbxx/flightQuery?isArrival=true&isAll=true";
    private String urlDepart = "http://www.gbiac.net/en/hbxx/flightQuery?isArrival=false&isAll=true";

    private Integer FLIGHT_ARRIVAL = 5;
    private Integer FLIGHT_DEPARTURE = 4;

    public GuangzhouLoader(AirportEnum airportEnum) {
        super(airportEnum);
    }

    @Override
    public FlightAD load() {
        FlightAD flight = new FlightAD();
        flight.setAirportId(getAirport().getAirportId());
        flight.setArrivals(loadParseArrival());

        long bytes = PageLoader.getBytesTransferred();
        long times = PageLoader.getTimeExecutingSecs();

        flight.setDeparture(loadParseDeparture());

        bytes = PageLoader.getBytesTransferred();
        times = PageLoader.getTimeExecutingSecs();

        return flight;
    }

    private List<FlightDetail> loadParseDeparture() {
        List<FlightDetail> fdl = new ArrayList<>();
        fdl.addAll(parseDeparture(FLIGHT_DEPARTURE, getDateFrom(-1, 0), getDateFrom(-1, 1)));
        fdl.addAll(parseDeparture(FLIGHT_DEPARTURE, getDateFrom(0, 0), getDateFrom(0, 1)));
        fdl.addAll(parseDeparture(FLIGHT_DEPARTURE, getDateFrom(1, 0), getDateFrom(1, 1)));
        return fdl;
    }

    private Collection<? extends FlightDetail> parseDeparture(Integer flightType, String dateFrom, String dateTo) {
        List<FlightDetail> fdl = new ArrayList<>();
        int pageNumber = 0;

        String body = PageLoader.LoaderPostGuandjou(urlDepart, pageNumber, flightType, dateFrom, dateTo);
        while (true) {
            fdl.addAll(parseArrivalPage(body));
            pageNumber++;

            String nextUrl = getNextPage(body);
            if (nextUrl.length() > 0) {
                body = PageLoader.LoaderPostGuandjou(urlDepart, pageNumber, flightType, dateFrom, dateTo);
            } else {
                break;
            }
        }


        return fdl;
    }

    private List<FlightDetail> loadParseArrival() {
        List<FlightDetail> fdl = new ArrayList<>();
        fdl.addAll(parseArrival(FLIGHT_ARRIVAL, getDateFrom(-1, 0), getDateFrom(-1, 1)));
        fdl.addAll(parseArrival(FLIGHT_ARRIVAL, getDateFrom(0, 0), getDateFrom(0, 1)));
        fdl.addAll(parseArrival(FLIGHT_ARRIVAL, getDateFrom(1, 0), getDateFrom(1, 1)));
        return fdl;
    }

    private Collection<? extends FlightDetail> parseArrival(Integer flightType, String dateFrom, String dateTo) {
        List<FlightDetail> fdl = new ArrayList<>();
        int pageNumber = 0;

        String body = PageLoader.LoaderPostGuandjou(urlArrive, pageNumber, flightType, dateFrom, dateTo);
        while (true) {
            fdl.addAll(parseArrivalPage(body));
            pageNumber++;
            String nextUrl = getNextPage(body);
            if (nextUrl.length() > 0) {
                body = PageLoader.LoaderPostGuandjou(urlArrive, pageNumber, flightType, dateFrom, dateTo);
            } else {
                break;
            }
        }
        long bytes = PageLoader.getBytesTransferred();
        long times = PageLoader.getTimeExecutingSecs();
        System.out.println("Size: " + fdl.size() + ", bytes: " + bytes + ", time: " + times);
        return fdl;
    }

    private String getNextPage(String body) {
        Document doc = Jsoup.parse(body);
        String url = "";
        Element a = doc.select("a.last").first();
        if (a != null) {
            url = a.attr("href");
        }
        return url;
    }

    private Collection<? extends FlightDetail> parseArrivalPage(String body) {
        List<FlightDetail> fdl = new ArrayList<>();
        Document doc = Jsoup.parse(body);
        Elements rows = doc.select("table.table").select("tbody").select("tr");
        for (int i = 1; i < rows.size(); i++) {
            Elements tds = rows.get(i).select("td");

            FlightDetail fd = new FlightDetail();
            String urlHrefDetail = tds.get(7).select("a").attr("href");
            fillFilghtDetail(fd, urlHrefDetail);
            fdl.add(fd);
        }

        return fdl;
    }

    private void fillFilghtDetail(FlightDetail fd, String urlHrefDetail) {
        String body = PageLoader.Loader(urlHrefDetail);
        Document doc = Jsoup.parse(body);

        String flightNumber = doc.select("li.flightNum").select("span").text();

        Elements elems = doc.select("ul.stl-flightinfodetail.mb20").select("li");
        String status = elems.get(3).select("p").text();

        Elements elems1 = doc.select("table.table-gray.mb30").first().select("tbody").select("tr");
        Elements tds = elems1.get(1).select("td");
        String schedulerTime = tds.get(3).text() + ":00";
        String actualTime = tds.get(5).text() + ":00";

        fd.setFlightNumber(flightNumber);
        fd.setScheduled(schedulerTime);
        fd.setActual(actualTime);

        if (status.contains("last bag") || status.contains("arrived") || status.contains("first bag")) {
            fd.setStatus(ArrivalStatus.LANDED);
        } else if (status.contains("cancel")) {
            fd.setStatus(ArrivalStatus.CANCELLED);
        } else if (status.contains("arriving")) {
            fd.setStatus(ArrivalStatus.EXPECTED);
        } else if (status.contains("delay")) {
            fd.setStatus(ArrivalStatus.DELAYED);
        } else if (status.contains("depart")) {
            fd.setStatus(ArrivalStatus.DEPARTED);
        } else if (status.contains("gate close")) {
            fd.setStatus(ArrivalStatus.DEPARTED);
        } else if (status.length() == 0) {
            fd.setStatus(ArrivalStatus.SCHEDULED);
        }
    }

    private String getDateFrom(int dateFrom, int dateEnd) {
        LocalDateTime ldt = LocalDateTime.now();
        String value = "";

        if (dateFrom < 0) {
            ldt = ldt.minusDays(dateFrom * (-1));
        } else {
            ldt = ldt.plusDays(dateFrom);
        }
        String localTimePattern = "yyyy-MM-d";
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(localTimePattern);
        if (dateEnd == 0) {
            value = ldt.format(fmt) + " 00:00:00";
        } else {
            value = ldt.format(fmt) + " 23:59:00";
        }

        return value;
    }
}
