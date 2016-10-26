package ru.air.parser.er;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.air.common.AirportEnum;
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

    public Set<FlightTr> load() {
        Set<FlightTr> flightTrSet = new HashSet<FlightTr>();

        String url = "http://zvartnots.am/new/get_data.php?minus_hour=" + getMinusHour() + "&plus_hour=" + getPlusHour() + "&dir=arr&lg=ru&tp=hour&_=" + System.currentTimeMillis() % 1000;
        String body = PageLoader.Loader(url);

        if (body.contains("2###")) {
            body = body.substring(1, body.length());
        }

        String[] flightString = body.split("@@@@2");
        for (String flightStr : flightString) {

            String[] flightArray = flightStr.split("###");


            //public Routing(String airportName, Date currDateTime, Date localDateTime) {

            String flightNumber = flightArray[3];
            String directionFrom = flightArray[4] + " " + flightArray[5];
            String typeBC = "";
            Date planeDate = new Date(); //flightArray[7];
            Date factDate = new Date(); //flightArray[11] 11 parse ?? "{imgarr}26, 11:30"
            String status = flightArray[10];
            String description = "";

//            Routing route = new flightArray(directionFrom, new Date()factDate 11, factDate 11 )
            List<Routing> lr = new ArrayList<Routing>();
            Routing route = new Routing(directionFrom, new Date(), new Date());
            lr.add(route);

            FlightTr flight = new FlightTr(flightNumber, directionFrom, typeBC, planeDate, factDate, status, description);
            flight.setRoute(lr);
            flightTrSet.add(flight);

        }
        return flightTrSet;
    }


}