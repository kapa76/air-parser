package ru.air.parser.europe;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.air.common.AirportEnum;
import ru.air.entity.FlightAD;
import ru.air.entity.FlightDetail;
import ru.air.loader.PageLoader;
import ru.air.loader.BaseLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BarselonaLoader extends BaseLoader {

    private String arrivalUrl = "http://www.aena.es/csee/Satellite/infovuelos/en/";
    private String departuresUrl = "http://www.aena.es/csee/Satellite/infovuelos/en/";

    private String outputTimePattern = "yyyy-MM-d HH:mm";
    private String formatterUrlDate = "yyyy-MM-d";

    public BarselonaLoader(AirportEnum airport) {
        super(airport, true);
    }

    public FlightAD load() {
        FlightAD flight = new FlightAD();
        flight.setAirportId(getAirport().getAirportId());

        loadArrival(flight.getArrivals());
        //flight.setDeparture(loadParse(departuresUrl, false));
        return flight;
    }

    private void loadArrival(List<FlightDetail> arrivals) {
        try {
            HtmlPage page = getWebClient().getPage(arrivalUrl);
            HtmlAnchor htmlAnchor = page.getAnchorByHref("/csee/Satellite/infovuelos/en/?mov=L");
            page = htmlAnchor.click();

            HtmlSelect select = (HtmlSelect) page.getElementById("origin_ac");
            HtmlOption option = select.getOptionByValue("BCN");
            page = select.setSelectedAttribute(option, true);

            HtmlSubmitInput submit = (HtmlSubmitInput) page.getByXPath("//input[@type='submit' and @value='Search']").get(2);
            page = submit.click();
            String body = page.asXml();

            while (body.length() > 0) {
                arrivals.addAll(parsePage(body));
                HtmlAnchor next = page.getAnchorByText("Next");
                if (next != null) {
                    page = next.click();
                    body = page.asXml();
                } else {
                    break;
                }
            }

        } catch (Exception exception) {

        }

    }

    private Collection<? extends FlightDetail> parsePage(String body) {
        List<FlightDetail> fdl = new ArrayList<>();
        Document doc = Jsoup.parse(body);
        Elements rows = doc.select("div#flightResults").select("tbody").select("tr");

        for (int i = 0; i < rows.size(); i++) {
            Elements tds = rows.get(i).select("td");
            String url = "";
            //get link
            FlightDetail detail = new FlightDetail();
            parseDetail(detail, PageLoader.Loader(""));
            fdl.add(detail);
        }
        return fdl;
    }

    private void parseDetail(FlightDetail detail, String loader) {

    }


}
