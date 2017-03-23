package ru.air.parser.russia;

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
import java.util.*;

/**
 * Created by Admin on 18.03.2017.
 */
public class ChelyabinksLoader extends BaseLoader {

    private String URL = "http://cekport.ru/passengers/information/timetable/";
    private String outputTimePattern = "yyyy-MM-d HH:mm";

    public ChelyabinksLoader(AirportEnum chelyabinks) {
        super(chelyabinks);
    }

    public FlightAD load() {
        FlightAD flight = new FlightAD();
        flight.setAirportId(getAirport().getAirportId());

        List<String> paramsLoader = new ArrayList<>();
        paramsLoader.add("yesterday");
        paramsLoader.add("today");
        paramsLoader.add("tomorrow");
        flight = load(flight, paramsLoader);

        return flight;
    }

    private FlightAD load(FlightAD flight, List<String> paramsLoader) {
        for (String key : paramsLoader) {
            String htmlPage = PageLoader.LoaderChelyabinksPost("http://cekport.ru/ajax/ttable.php", "day", key);

            flight.getDeparture().addAll(parseDeparture(htmlPage, key));
            flight.getArrivals().addAll(parseArrival(htmlPage, key));

        }

        return flight;
    }

    private Collection<? extends FlightDetail> parseDeparture(String strBody, String keyValue) {
        List<FlightDetail> detailList = new ArrayList<>();
        org.jsoup.nodes.Document doc = Jsoup.parse(strBody);

        Elements rows = doc.select("div.col.fl").get(0).select("div.inner").select("div.fi-header.cf");
        for (int i = 0; i < rows.size(); i++) {
            Element row = rows.get(i);
            FlightDetail detail = new FlightDetail();

            detail.setFlightNumber(row.select("span.tth-flight").text());
            String status = row.select("span.tth-status.ih-status-boarded").text();

            String schedulerTime = row.select("span.tth-time").text();
            String actualTime = row.select("span.tth-time-count").text();

            if (status.equals("По расписанию")) {
                detail.setStatus(ArrivalStatus.SCHEDULED);
            } else if (status.contains("вылет задержан до")) {
                detail.setStatus(ArrivalStatus.DELAYED);
            } else if (status.equals("отменен")) {
                detail.setStatus(ArrivalStatus.CANCELLED);
            } else if (status.equals("Вылетел")) {
                detail.setStatus(ArrivalStatus.DEPARTED);
            }

            if (!schedulerTime.equals(actualTime)) {
                detail.setActual(getDateTime(keyValue, actualTime));
            }

            detail.setScheduled(getDateTime(keyValue, schedulerTime));
            detail.setEstimated("");
            detailList.add(detail);

        }

        return detailList;
    }

    private Collection<? extends FlightDetail> parseArrival(String strBody, String keyValue) {
        List<FlightDetail> detailList = new ArrayList<>();
        org.jsoup.nodes.Document doc = Jsoup.parse(strBody);

        Elements rows = doc.select("div.col.fl").get(1).select("div.inner").select("div.fi-header.cf");
        for (int i = 0; i < rows.size(); i++) {
            Element row = rows.get(i);
            FlightDetail detail = new FlightDetail();

            detail.setFlightNumber(row.select("span.tth-flight").text());
            String status = row.select("span.tth-status.ih-status-boarded").text();

            String schedulerTime = row.select("span.tth-time").text();
            String actualTime = row.select("span.tth-time-count").text();

            if (status.equals("Рейс прибыл")) {
                detail.setStatus(ArrivalStatus.LANDED);
            } else if (status.equals("По расписанию")) {
                detail.setStatus(ArrivalStatus.SCHEDULED);
            } else if (status.contains("вылет задержан до")) {
                detail.setStatus(ArrivalStatus.DELAYED);
            } else if (status.equals("отменен")) {
                detail.setStatus(ArrivalStatus.CANCELLED);
            }

            if (!schedulerTime.equals(actualTime)) {
                detail.setActual(getDateTime(keyValue, actualTime));
            }

            detail.setScheduled(getDateTime(keyValue, schedulerTime));
            detail.setEstimated("");
            detailList.add(detail);

        }

        return detailList;
    }


    private String getDateTime(String status, String time) {
        String[] times = time.split(":");
        LocalDateTime ldt = LocalDateTime.now();

        ldt = ldt.withHour(Integer.parseInt(times[0]));
        ldt = ldt.withMinute(Integer.parseInt(times[1]));

        switch (status) {
            case "yesterday": {
                ldt = ldt.minusDays(1);
                break;
            }
            case "tomorrow": {
                ldt = ldt.plusDays(1);
                break;
            }
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(outputTimePattern);
        return ldt.format(fmt) + ":00";
    }

}
