package ru.air.parser.europe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.air.common.AirportEnum;
import ru.air.common.CityJsonStruct;
import ru.air.common.GroupItem;
import ru.air.common.Item;
import ru.air.entity.FlightAD;
import ru.air.entity.FlightDetail;
import ru.air.loader.BaseLoader;
import ru.air.loader.PageLoader;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CharlesDeGaulleLoader extends BaseLoader {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private String arrivalUrl = "http://www.parisaeroport.fr/en/passengers/flights/arrival-flights/information-flight-arrivals";
    private String departureUrl = "http://www.parisaeroport.fr/en/passengers/flights/flight-departures/informations-flights-departures";

    private String jsonCityUrl = "http://www.parisaeroport.fr/restapi/vols/countrieswithcities/en?format=json";

    private List<String> cityValues;

    public CharlesDeGaulleLoader(AirportEnum airportEnum) {
        super(airportEnum, true);
        try {
            String cityJson = PageLoader.Loader(jsonCityUrl);
            cityValues = getCityValues(objectMapper.readValue(cityJson, CityJsonStruct.class));
        } catch (Exception exeption) {

        }
    }

    @Override
    public FlightAD load() {
        FlightAD flight = new FlightAD();
        flight.setAirportId(getAirport().getAirportId());

        flight.setArrivals(loadArrival(arrivalUrl));
//        flight.setDeparture(loadDeparture(departureUrl));
        return flight;
    }

    private List<String> getCityValues(CityJsonStruct city) {
        List<String> vList = new ArrayList<>();
        for (Item item : city.getItems()) {
            for (GroupItem groupItem : item.getGroup_items()) {
                vList.add(groupItem.getName());
            }
        }
        return vList;
    }

    private List<FlightDetail> loadDeparture(String url) {
        return null;
    }

    private List<FlightDetail> loadArrival(String url) {
        List<FlightDetail> fdl = new ArrayList<>();
        try {
            HtmlPage page = getWebClient().getPage(url);

            for (String city : cityValues) {
                HtmlTextInput select = (HtmlTextInput) page.getElementById("txtDestination");
                select.setText(city);

                HtmlSubmitInput submit = (HtmlSubmitInput) page.getByXPath("//input[@type='submit' and @value='Find']").get(0);
                page = submit.click();
                String body = page.asXml();

                if (existRecord(body, city)) {

                    fdl.addAll(parseArrivalPage(body));

                }


                /*
                * <input name="ctl00$ctl00$CPHMain$CPHContent$C007$ctl00$ctl00$Search" type="submit" id="Search" class="Validate Validate--orange" value="Find">
                * */

            }

        } catch (Exception exception) {

        }

        return null;
    }

    private Collection<? extends FlightDetail> parseArrivalPage(String body) {
        List<FlightDetail> fdl = new ArrayList<>();
        Document doc = Jsoup.parse(body);
        Elements rows = doc.select("div.flightboard-table").select("div.flightboard-tr").select("flightboard-tr-top");

        for (int i = 0; i < rows.size(); i++) {
            Elements tds = rows.get(i).select("div.flipboard");

            FlightDetail fd = new FlightDetail();
            fd.setFlightNumber(tds.get(0).text());

            String href = "http://www.parisaeroport.fr/en/passengers/flights/arrival-flights/" + tds.get(6).select("a").attr("href");
            fillFlightDetailArrive(fd, href);


/*
<div data-flipboard-size="6" class="flipboard"><span>ZI222</span></div>
<div data-flipboard-size="4" class="flipboard"><span>26/03</span></div>
<div data-flipboard-size="4" class="flipboard"><span>10:10</span></div>
<div data-flipboard-size="12" class="flipboard"><span>ALGIERS</span></div>
<div data-flipboard-size="5" class="flipboard"><span>AIGLE AZUR</span></div>
<div data-flipboard-size="4" class="flipboard"><span>Paris-Orly</span></div>
<div data-flipboard-size="12" class="flipboard flipboard--white"><a href="A_OS?ckey=20170326OAZI 222  20170326ALG" class="flipboard__link"><span
class="flipboard__link flipboard__link--label">Flight details</span><span>&gt;</span></a></div>
*/


        }
        return fdl;
    }

    private void fillFlightDetailArrive(FlightDetail fd, String href) {
        String body = PageLoader.Loader(href);
        Document doc = Jsoup.parse(body);
        String status = doc.select("div.grid__item.one-half.vol-header-right").select("div.vol-date.bold").text();

        // Arrival on 26/03/2017 at 18h40

        String actualDate = doc.select("div.ontime").text();
    }

    private boolean existRecord(String body, String city) {
        Document doc = Jsoup.parse(body);
        String resultTotal = doc.select("span.results-total__highlight").text();
        System.out.println("City: " + city + ", result: " + resultTotal);

        if (!resultTotal.contains("0")) {
            return true;
        }
        return false;
    }


}
