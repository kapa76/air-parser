package ru.air.parser.russia;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.air.common.AirportEnum;
import ru.air.common.ArrivalStatus;
import ru.air.entity.FlightAD;
import ru.air.entity.FlightDetail;
import ru.air.loader.PageLoader;
import ru.air.parser.BaseLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 17.03.2017.
 */
public class AnapaLoader extends BaseLoader {

    private String URL = "http://basel.aero/anapa/passengers/online-schedule/";
    private String outputTimePattern = "yyyy-MM-d HH:mm";

    public AnapaLoader(AirportEnum anapa) {
        super(anapa);
    }

    public FlightAD load() {
        FlightAD flight = new FlightAD();
        flight.setAirportId(getAirport().getAirportId());
        flight.setArrivals(loadArrival());
        flight.setDeparture(loadDeparture());
        return flight;
    }

    private List<FlightDetail> loadDeparture() {
        List<FlightDetail> value = new ArrayList<>();
        value.addAll(parseDeparture(PageLoader.Loader(URL)));
        return value;
    }

    private List<FlightDetail> loadArrival() {
        List<FlightDetail> value = new ArrayList<>();
        value.addAll(parse(PageLoader.Loader(URL)));
        return value;
    }

    private List<FlightDetail> parse(String strBody) {
        List<FlightDetail> detailList = new ArrayList<>();

        org.jsoup.nodes.Document doc = Jsoup.parse(strBody);
        Elements trs = doc.select("div.tabs__content.tab-anim-flip");
        for (int i = 0; i < 3; i++) {
            //for each part
            Elements els = trs.get(i).select("div.tabs__data.arrive").first().select("div.table__body");
            Elements rows = els.first().select("div.js-table__row");

            //3 части, 1 часть вчера, сегодня и 3 - часть завтра.
            for (Element oneRow : rows) {
                Elements tdsList = oneRow.select("div.table__cell");
                FlightDetail detail = new FlightDetail();
                detail.setFlightNumber(tdsList.get(2).text());
                String status = tdsList.get(6).text();
                String time = tdsList.get(1).text();
                String dt = getDateTime(i - 1, time);
                if (status.equals("прибыл")) {
                    detail.setStatus(ArrivalStatus.LANDED);
                } else if (status.equals("по расписанию")) {
                    detail.setStatus(ArrivalStatus.SCHEDULED);
                } else if (status.contains("вылет задержан до")) {
                    detail.setStatus(ArrivalStatus.DELAYED);
                } else if (status.equals("отменен")) {
                    detail.setStatus(ArrivalStatus.CANCELLED);
                }

                detail.setActual(dt);
                detail.setScheduled("");
                detail.setEstimated("");
                detailList.add(detail);
            }
        }

        return detailList;
    }

    private List<FlightDetail> parseDeparture(String strBody) {
        List<FlightDetail> detailList = new ArrayList<>();

        org.jsoup.nodes.Document doc = Jsoup.parse(strBody);
        Elements trs = doc.select("div.tabs__content.tab-anim-flip");
        for (int i = 0; i < 3; i++) {
            //for each part
            Elements els = trs.get(i).select("div.tabs__data.depart").first().select("div.table__body");
            Elements rows = els.first().select("div.js-table__row");

            //3 части, 1 часть вчера, сегодня и 3 - часть завтра.
            for (Element oneRow : rows) {
                Elements tdsList = oneRow.select("div.table__cell");
                FlightDetail detail = new FlightDetail();
                detail.setFlightNumber(tdsList.get(2).text());
                String status = tdsList.get(6).text();
                String time = tdsList.get(1).text();
                String schedulDt = getDateTime(i - 1, time);
                String actualTime = getTimeFromStatus(status);
                String actualDt = "";

                if(actualTime.length() > 0) {
                    actualDt = getDateTime(i - 1, actualTime);
                }

                if (status.contains("по расписанию")) {
                    detail.setStatus(ArrivalStatus.SCHEDULED);
                } else if (status.contains("ВЫЛЕТЕЛ в")){
                    detail.setStatus(ArrivalStatus.SCHEDULED);
                } else if (status.contains("вылет задержан до")) {
                    detail.setStatus(ArrivalStatus.DELAYED);
                } else if (status.equals("отменен")) {
                    detail.setStatus(ArrivalStatus.CANCELLED);
                }

                if(!time.equals(actualTime)) {
                    detail.setActual(actualDt);
                }
                detail.setScheduled(schedulDt);
                detail.setEstimated("");
                detailList.add(detail);
            }
        }

        return detailList;
    }

    private String getTimeFromStatus(String st) {
        if(st.length() > 10 && st.contains("ВЫЛЕТЕЛ в ")){
            return st.replace("ВЫЛЕТЕЛ в ", "");
        }
        return "";
    }

    private String getDateTime(int i, String time) {

        String[] tt = time.split(":");
        DateTime dt = new DateTime()
                .withHourOfDay(Integer.parseInt(tt[0]))
                .withMinuteOfHour(Integer.parseInt(tt[1]));

        //1900 + dtemp.getYear(), dtemp.getMonth(), dtemp.getDate(), Integer.parseInt(tt[0]), Integer.parseInt(tt[1]));
        dt = dt.plusDays(i);

        DateTimeFormatter fmt = DateTimeFormat.forPattern(outputTimePattern);

        return fmt.print(dt) + ":00";
    }

}
