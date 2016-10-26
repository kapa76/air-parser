package ru.air.parser.er;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.air.common.AirportEnum;
import ru.air.common.ArrivalStatus;
import ru.air.entity.Flight;
import ru.air.entity.FlightDetail;
import ru.air.loader.PageLoader;
import ru.air.parser.BaseLoader;
import ru.air.parser.ek.entity.FlightTr;
import ru.air.parser.ek.entity.Routing;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kapa on 26.10.16.
 */

//http://zvartnots.am/new/get_data.php?minus_hour=-10&plus_hour=12&dir=arr&lg=ru&tp=hour&_=1477471632547

public class ErLoader extends BaseLoader {


    public ErLoader(AirportEnum airport) {
        super(airport);
    }

    public Flight load() {
        List<FlightDetail> flightDetailList = new ArrayList<FlightDetail>();

        String body = loadDataFromSite();

        String[] flightString = body.split("@@@@2");
        for (String flightStr : flightString) {

            String[] flightArray = flightStr.split("###");


            String flightNumber = flightArray[3];

            String scheduled = flightArray[7];      // 26.10.2016 13:45 дата/время приземления по расписанию по местному времени аэропорта в формате YYYY-mm-dd HH:MM:SS
            String estimated;      //прогнозируемые дата/время приземления (если есть) по местному времени аэропорта в формате YYYY-mm-dd HH:MM:SS
            String actual;         //фактические дата/время приземления (если есть) по местному времени аэропорта в формате YYYY-mm-dd HH:MM:SS

//            Date planeDate = new Date(); //flightArray[7];
//            Date factDate = new Date(); //flightArray[11] 11 parse ?? "{imgarr}26, 11:30"

            ArrivalStatus status;

            if (flightArray[10].equals("Прибыл")) {
                status = ArrivalStatus.LANDED;
            } else if (flightArray[10].equals("Опоздание")) {


                //status = ArrivalStatus.
            } else if (flightArray[10].equals("Вовремя")) {
                status = ArrivalStatus.SCHEDULED;
            }

            /*
            private String scheduled;      // дата/время приземления по расписанию по местному времени аэропорта в формате YYYY-mm-dd HH:MM:SS
            private String estimated;      //прогнозируемые дата/время приземления (если есть) по местному времени аэропорта в формате YYYY-mm-dd HH:MM:SS
            private String actual;         //фактические дата/время приземления (если есть) по местному времени аэропорта в формате YYYY-mm-dd HH:MM:SS
            private ArrivalStatus status;
            */

            //public FlightDetail(String flightNumber, Date scheduled, Date estimated, Date actual, ArrivalStatus status) {

            FlightDetail detail = new FlightDetail();
            flightDetailList.add(detail);
        }

        Flight flight = new Flight();
        flight.setAirportId(getAirport().getAirportId());
        flight.setArrivals(flightDetailList);
        return flight;
    }

    private String loadDataFromSite() {
        String url = "http://zvartnots.am/new/get_data.php?minus_hour=" + getMinusHour() + "&plus_hour=" + getPlusHour() + "&dir=arr&lg=ru&tp=hour&_=" + System.currentTimeMillis() % 1000;
        String body = PageLoader.Loader(url);

        if (body.contains("2###")) {
            body = body.substring(1, body.length());
        }

        return body;
    }


}