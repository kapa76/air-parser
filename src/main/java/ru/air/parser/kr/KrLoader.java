package ru.air.parser.kr;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.entity.FlightDetail;
import ru.air.loader.PageLoader;
import ru.air.parser.BaseLoader;
import ru.air.parser.ek.entity.FlightTr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class KrLoader extends BaseLoader {

    private String URL = "http://basel.aero/krasnodar/passengers/online-schedule/";

    public KrLoader(AirportEnum airportEnum) {
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
//        value.addAll(parse(PageLoader.Loader(todayUrl), 0));
//        value.addAll(parse(PageLoader.Loader(tomorrowUrl), 1));

        return value;
    }

    private List<FlightDetail> parse(String strBody) {
        List<FlightDetail> detailList = new ArrayList<>();

        org.jsoup.nodes.Document doc = Jsoup.parse(strBody);
        Elements trs = doc.select("div.tabs__content.tab-anim-flip");
        for(Element el : trs){
            //for each part
            Elements els = el.select("div.tabs__data.arrive");


        }

        return null;
    }

}
