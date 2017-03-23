package ru.air.parser.europe;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.air.common.AirportEnum;
import ru.air.entity.FlightAD;
import ru.air.entity.FlightDetail;
import ru.air.loader.PageLoader;
import ru.air.loader.BaseLoader;

import java.util.Collection;
import java.util.List;

/**
 * Created by Admin on 24.03.2017.
 */
public class BarselonaLoader extends BaseLoader {

    private String arrivalUrl = "http://www.aena.es/csee/Satellite/infovuelos/en/";
    private String departuresUrl = "http://www.aena.es/csee/Satellite/infovuelos/en/";

    private String outputTimePattern = "yyyy-MM-d HH:mm";
    private String formatterUrlDate = "yyyy-MM-d";

    public BarselonaLoader(AirportEnum airport) {
        super(airport);
    }

    public FlightAD load() {
        FlightAD flight = new FlightAD();
        flight.setAirportId(getAirport().getAirportId());

        loadArrival(flight.getArrivals());
        //flight.setDeparture(loadParse(departuresUrl, false));
        return flight;
    }

    private void loadArrival(List<FlightDetail> arrivals) {
        int counterPage = 1;
        String body = PageLoader.PostBarselonaArrival(arrivalUrl);

        while(getSummaryPage(body)){
           arrivals.addAll(parsePage(body));

           body = PageLoader.PostBarselonaArrival(arrivalUrl);
        }
    }

    private Collection<? extends FlightDetail> parsePage(String body) {
        Document doc = Jsoup.parse(body);
        doc.select("div.paginado");



        return null;
    }

    private boolean getSummaryPage(String body) {
        Document doc = Jsoup.parse(body);
        if( doc.select("div.paginado").select("a.pagSig") != null )
            return true;
        else return false;
    }
}
