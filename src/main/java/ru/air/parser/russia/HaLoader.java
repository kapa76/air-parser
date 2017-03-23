package ru.air.parser.russia;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import ru.air.common.AirportEnum;
import ru.air.common.ArrivalStatus;
import ru.air.entity.Flight;
import ru.air.entity.FlightDetail;
import ru.air.loader.PageLoader;
import ru.air.loader.BaseLoader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by kapa on 28.10.16.
 */
public class HaLoader extends BaseLoader {

    private String yesterdayUrl = "http://airkhv.ru/components/com_tablo/ajax-worker.php?kolFlights=0&dateVal=-1";
    private String todayUrl = "http://airkhv.ru/components/com_tablo/ajax-worker.php?kolFlights=0&dateVal=0";
    private String tomorrowUrl = "http://airkhv.ru/components/com_tablo/ajax-worker.php?kolFlights=0&dateVal=1";

    private String outputTimePattern = "yyyy-MM-d HH:mm:ss";

    public HaLoader(AirportEnum airport) {
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

    private String convertDateWithUpdate(String inputPattern, String localDateTime) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);

        try {
            cal.setTime((new SimpleDateFormat(inputPattern, Locale.ENGLISH).parse(localDateTime)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Locale locale = new Locale("ru", "RU");
        Calendar cc = Calendar.getInstance(locale);

        Date date = cal.getTime();
        cc.setTime(date);
        cc.set(Calendar.YEAR, 1900 + (new Date()).getYear());

        DateFormat output = new SimpleDateFormat(outputTimePattern);
        String value = output.format(cc.getTime());
        return value;
    }

    private List<FlightDetail> parse(String strBody, int days) {
        List<FlightDetail> detailList = new ArrayList<>();

        org.jsoup.nodes.Document doc = Jsoup.parse(strBody);
        Elements trs = doc.select("#tabloContent_in").get(0).select("tbody > tr");

        for (int i = 1; i < trs.size(); i++) {
            Elements tdList = trs.get(i).select("td");

            FlightDetail detail = new FlightDetail();
            detail.setStatus(ArrivalStatus.SCHEDULED);
            detail.setEstimated("");
            detail.setFlightNumber(tdList.get(1).text());
            String onclickScriptStr = tdList.get(1).html();

            String ss = "&lt;/b&gt;&lt;/td&gt;&lt;td&gt;";
            String[] temp0 = onclickScriptStr.split(ss);

            String plan = temp0[2].substring(0,12);
            String fact = temp0[3].substring(0,12);

            if (plan.length() > 0) {
                detail.setScheduled(convertDateWithUpdate("dd MMM HH:mm", plan));
            } else {
                detail.setScheduled("");
            }

            if (fact.length() > 0 && onclickScriptStr.contains("Фактическое время прибытия")) {
                detail.setActual(convertDateWithUpdate("dd MMM HH:mm", fact));
            } else {
                detail.setActual("");
            }

            String status = tdList.get(5).text();
            if (status.equals("Прилетел")) {
                detail.setStatus(ArrivalStatus.LANDED);
            } else if (status.equals("Отменен")) {
                detail.setStatus(ArrivalStatus.CANCELLED);
            } else if (status.equals("Прилетел с задержкой")) {
                detail.setStatus(ArrivalStatus.SCHEDULED);
            } else if (status.equals("Ожидается")) {
                detail.setStatus(ArrivalStatus.SCHEDULED);
            } else if (status.equals("Задерживается")) {
                detail.setStatus(ArrivalStatus.DELAYED);
            }

            detailList.add(detail);
        }

        return detailList;
    }
}
