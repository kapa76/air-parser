package ru.air.parser.russia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.air.common.AirportEnum;
import ru.air.common.ArrivalStatus;
import ru.air.entity.Flight;
import ru.air.entity.FlightDetail;
import ru.air.loader.PageLoader;
import ru.air.parser.BaseLoader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by kapa on 28.10.16.
 */
public class DuLoader extends BaseLoader {

    private String yesterdayUrl = "http://airport.tj/index.php/ru/tablo?date=yesterday";
    private String todayUrl = "http://airport.tj/index.php/ru/tablo?date=today";
    private String tomorrowUrl = "http://airport.tj/index.php/ru/tablo?date=tomorrow";

    private String outputTimePattern = "yyyy-MM-d HH:mm:ss";


    public DuLoader(AirportEnum airport) {
        super(airport);
    }

    public Flight load() {
        Flight flight = new Flight();
        flight.setAirportId(getAirport().getAirportId());
        flight.setArrivals(loadDataFromSite());
        return flight;
    }

    private List<FlightDetail> loadDataFromSite() {
        List<FlightDetail> value = new ArrayList<>();

        value.addAll(parse(PageLoader.Loader(yesterdayUrl), -1));
        value.addAll(parse(PageLoader.Loader(todayUrl), 0));
        value.addAll(parse(PageLoader.Loader(tomorrowUrl), 1));

        return value;
    }

    private String convertDateWithUpdate(String inputPattern, String strDate, int days) {
        DateFormat df = new SimpleDateFormat(inputPattern);
        Calendar cal = Calendar.getInstance();

        String[] data = strDate.split(":");
        int hours = Integer.parseInt(data[0]);
        int minutes = Integer.parseInt(data[1]);
        cal.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE) + days, hours, minutes);

        LocalDateTime tt = LocalDateTime.of(Calendar.YEAR, Calendar.MONTH, Calendar.DATE + days, hours, minutes, 0, 0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(outputTimePattern);

        return tt.format(formatter);

//        DateFormat output = new SimpleDateFormat(outputTimePattern);
//        return output.format(cal.getTime());

    }

    private List<FlightDetail> parse(String strBody, int days) {
        List<FlightDetail> detailList = new ArrayList<>();

        Document doc = Jsoup.parse(strBody);
        Elements trs = doc.select("div.table-responsive").get(0).select("tbody > tr");

        for (Element tr : trs) {
            Elements tdList = tr.select("td");

            FlightDetail detail = new FlightDetail();
            detail.setStatus(ArrivalStatus.UNKNOWN);

            detail.setFlightNumber(tdList.get(0).text());
            detail.setEstimated("");

            if (tdList.get(3).text().length() > 0) {
                detail.setScheduled(convertDateWithUpdate("HH:mm", tdList.get(3).text(), days));
            } else {
                detail.setScheduled("");
            }

            if (tdList.get(4).text().length() > 0) {
                detail.setActual(convertDateWithUpdate("HH:mm", tdList.get(4).text(), days));
            } else {
                detail.setActual("");
            }

            String status = tdList.get(5).text();
            if (status.equals("Прибыл")) {
                detail.setStatus(ArrivalStatus.LANDED);
            } else if (status.equals("Отменен")) {
                detail.setStatus(ArrivalStatus.CANCELLED);
            } else if (status.equals("По расписанию")) {
                detail.setStatus(ArrivalStatus.SCHEDULED);
            }

            detailList.add(detail);
        }

        return detailList;
    }

}
