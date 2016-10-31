package ru.air.parser.vo;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
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

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 31.10.2016.
 */
public class VoLoader extends BaseLoader {

    private String URL = "http://vvo.aero";
    private String outputTimePattern = "yyyy-MM-d HH:mm:ss";

    public VoLoader(AirportEnum airportEnum) {
        super(airportEnum);
    }

    public Flight load() {
        Flight flight = new Flight();
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

        Document doc = Jsoup.parse(strBody);
        Elements parts = doc.select("#arrival > table > tbody");
        String scripts = doc.select("#arrival > script").html();

        scripts = scripts.replace("jQuery.extend(table_addition,", "");
        scripts = scripts.substring(0, scripts.length()-2);

        String ss = new String(scripts.getBytes(Charset.forName("UTF-8")));
        // ss.split("},")
        for(int index=0; index < 3; index++) {
            Elements rows = parts.get(index).select("tr");

            for(Element row : rows){
                String id = row.id();
                Elements tds = row.select("td");




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