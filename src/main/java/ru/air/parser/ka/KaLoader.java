package ru.air.parser.ka;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 06.11.2016.
 */
public class KaLoader extends BaseLoader {

    private String url = "http://www.kazan.aero/reloading_ru.php";
    private String outputTimePattern = "yyyy-MM-d HH:mm:ss";

    public KaLoader(AirportEnum airport) {
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

        value.addAll(parse(PageLoader.LoaderPost(url)));

        return value;
    }

    private String getDate(String inputPattern, String dt) {
        DateFormat df = new SimpleDateFormat(inputPattern);
        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(df.parse(dt));
        } catch (ParseException e) {
            return "";
        }

        DateFormat output = new SimpleDateFormat(outputTimePattern);
        return output.format(cal.getTime());
    }

    private List<FlightDetail> parse(String strBody) {
        List<FlightDetail> detailList = new ArrayList<>();

        Document doc = Jsoup.parse(strBody);
        Elements trs = doc.select("table.arrivals_table").select("table").get(0).select("tbody > tr");
        for (int i = 1; i < trs.size(); i++) {
            Elements tdList = trs.get(i).select("td");

            FlightDetail detail = new FlightDetail();

            detail.setFlightNumber(tdList.get(2).text());
            String schedulerDateTime = tdList.get(6).text(); //schedul //01:10 05.11.2016 01-05
            String actualDateTime = tdList.get(7).text(); //fact //(05.11.2016 00:50), (HH:mm)
            if (schedulerDateTime.length() > 0) {
                String dt = "";
                if ((dt = getDate("HH:mm dd.MM.yyyy HH-mm", schedulerDateTime)).length() > 0) {
                    detail.setScheduled(dt);
                } else if ((dt = getDate("HH:mm dd.MM.yyyy", schedulerDateTime)).length() > 0) {
                    detail.setScheduled(dt);
                }
            }

            if (actualDateTime.length() > 0) {// (01:20)
                String dt = "";
                if ((dt = getDate("(HH:mm)", actualDateTime)).length() > 0) {
                    detail.setActual(dt);
                } else if ((dt = getDate("(dd.MM.yyyy HH:mm)", actualDateTime)).length() > 0) {
                    detail.setActual(dt);
                }
            }

            String status = tdList.get(1).text();
            if (status.equals("прибыл")) {
                detail.setStatus(ArrivalStatus.LANDED);
            } else if (status.equals("отменен")) {
                detail.setStatus(ArrivalStatus.CANCELLED);
            } else if (status.equals("выдача багажа")) {
                detail.setStatus(ArrivalStatus.LANDED);
            }

            detailList.add(detail);
        }

        return detailList;
    }
}
