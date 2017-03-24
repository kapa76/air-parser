package ru.air.parser.russia;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.air.common.AirportEnum;
import ru.air.common.ArrivalStatus;
import ru.air.entity.Flight;
import ru.air.entity.FlightAD;
import ru.air.entity.FlightDetail;
import ru.air.loader.PageLoader;
import ru.air.loader.BaseLoader;

import java.util.ArrayList;
import java.util.List;

public class KrLoader extends BaseLoader {

    private String URL = "http://basel.aero/krasnodar/passengers/online-schedule/";
    private String outputTimePattern = "yyyy-MM-d HH:mm:ss";

    public KrLoader(AirportEnum airportEnum) {
        super(airportEnum);
    }

    public FlightAD load() {
        FlightAD flight = new FlightAD();
        flight.setAirportId(getAirport().getAirportId());
        flight.setArrivals(loadDataFromSite());
        return flight;
    }

    private List<FlightDetail> loadDataFromSite() {
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

                if(status.equals("прибыл")){
                    detail.setStatus(ArrivalStatus.LANDED);
                } else if(status.equals("по расписанию")){
                    detail.setStatus(ArrivalStatus.SCHEDULED);
                } else if(status.contains("вылет задержан до")){
                    detail.setStatus(ArrivalStatus.DELAYED);
                } else if(status.equals("отменен")){
                    detail.setStatus(ArrivalStatus.CANCELLED);
                }

                detail.setActual(dt);
                detail.setScheduled("");
                detail.setEstimated("");
                /*
                0 = {Element@2901} "<div class="table__cell table__cell_w-5"> \n <span class="table__arrow"></span> \n</div>"
                1 = {Element@2902} "<div class="table__cell table__cell_w-8"> \n <span class="time">00:05</span> \n</div>"
                2 = {Element@2903} "<div class="table__cell table__cell_w-9"> \n <span class="flight">QH915</span>\n</div>"
                3 = {Element@2904} "<div class="table__cell table__cell_w-23"> \n <span class="direction">БИШКЕК</span> \n</div>"
                4 = {Element@2905} "<div class="table__cell table__cell_w-22"> \n <a href="/krasnodar/passengers/airlines/kyrgyzstan/" class="aerocomp"> <span class="i-ico-wrap"> <img src="/upload/resize_cache/iblock/050/24_10_1/kyrgystan.png" alt="КЫРГЫЗСТАН" /> </span> КЫРГЫЗСТАН </a> \n</div>"
                5 = {Element@2906} "<div class="table__cell table__cell_w-13"> \n <span class="type">Б737-5</span> \n</div>"
                6 = {Element@2907} "<div class="table__cell table__cell_w-20"> \n <span class="status">ПРИБЫЛ</span> \n</div>"
                        */

                detailList.add(detail);
            }
        }

        return detailList;
    }

    private String getDateTime(int i, String time) {

        String[] tt = time.split(":");
        DateTime dt = new DateTime()
                .withHourOfDay(Integer.parseInt(tt[0]))
                .withMinuteOfHour(Integer.parseInt(tt[1]));

        //1900 + dtemp.getYear(), dtemp.getMonth(), dtemp.getDate(), Integer.parseInt(tt[0]), Integer.parseInt(tt[1]));
        dt = dt.plusDays(i);

        DateTimeFormatter fmt = DateTimeFormat.forPattern(outputTimePattern);

        return fmt.print(dt);
    }

}
