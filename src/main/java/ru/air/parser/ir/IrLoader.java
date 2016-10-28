package ru.air.parser.ir;

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
import java.util.*;

/**
 * Created by kapa on 28.10.16.
 */
public class IrLoader extends BaseLoader {

    private String yesterdayUrl = "http://iktport.ru/component/option,com_tarchive/arc,0/task,prilet/";
    private String todayUrl = "http://iktport.ru/component/option,com_tarchive/arc,1/task,prilet/";
    private String tomorrowUrl = "http://iktport.ru/component/option,com_tarchive/arc,2/task,prilet/";

    private String outputTimePattern = "yyyy-MM-d HH:mm:ss";

    public IrLoader(AirportEnum airport) {
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

    private String convertDateWithUpdate(String inputPattern, String strDate) {
        DateFormat df = new SimpleDateFormat(inputPattern);
        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(df.parse(strDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        cal.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));//, Calendar.getInstance().get(Calendar.DATE) + days, hours, minutes);

        DateFormat output = new SimpleDateFormat(outputTimePattern);
        return output.format(cal.getTime());

    }

    private String convertDateWithUpdateA(String inputPattern, String strDate, String localDateTime) {
        DateFormat df = new SimpleDateFormat(inputPattern);
        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime((new SimpleDateFormat(outputTimePattern).parse(localDateTime)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] data = strDate.split(":");
        int hours = Integer.parseInt(data[0]);
        int minutes = Integer.parseInt(data[1]);

        Date date = cal.getTime();
        date.setHours(hours);
        date.setMinutes(minutes);
        cal.setTime(date);
        DateFormat output = new SimpleDateFormat(outputTimePattern);
        String value = output.format(cal.getTime());
        return value;

    }

    private List<FlightDetail> parse(String strBody, int days) {
        List<FlightDetail> detailList = new ArrayList<>();

        Document doc = Jsoup.parse(strBody);
        Elements trs = doc.select("#tblh").select("table").get(0).select("tbody > tr");
        for (int i = 1; i < trs.size(); i++) {
            Elements tdList = trs.get(i).select("td");

            FlightDetail detail = new FlightDetail();
            detail.setStatus(ArrivalStatus.SCHEDULED);
            detail.setEstimated("");
            detail.setFlightNumber(tdList.get(0).text());

            if (tdList.get(3).text().length() > 0) {
                detail.setScheduled(convertDateWithUpdate("HH:mm (dd-MMM)", tdList.get(3).text()));
            } else {
                detail.setScheduled("");
            }

            if (tdList.get(4).text().length() > 0) {
                detail.setActual(convertDateWithUpdateA("HH:mm", tdList.get(5).text(), detail.getScheduled()));
            } else {
                detail.setActual("");
            }

            String status = tdList.get(4).text();
            if (status.equals("Прибыл")) {
                detail.setStatus(ArrivalStatus.LANDED);
            } else if (status.equals("Отменен")) {
                detail.setStatus(ArrivalStatus.CANCELLED);
            } else if (status.equals("По расписанию")) {
                detail.setStatus(ArrivalStatus.SCHEDULED);
            } else if (status.equals("Прибывает")) {
                detail.setStatus(ArrivalStatus.SCHEDULED);
            }

            detailList.add(detail);
        }

        return detailList;
    }
}