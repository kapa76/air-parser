package ru.air.parser.europe;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.air.common.AirportEnum;
import ru.air.common.ArrivalStatus;
import ru.air.entity.FlightAD;
import ru.air.entity.FlightDetail;
import ru.air.loader.PageLoader;
import ru.air.loader.BaseLoader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BarselonaLoader extends BaseLoader {

    private final String baseSite = "http://www.aena.es";
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
        loadDeparture(flight.getDeparture());
        return flight;
    }

    private void loadDeparture(List<FlightDetail> departure) {
        try {
            HtmlPage page = getWebClient().getPage(arrivalUrl);

            HtmlSelect select = (HtmlSelect) page.getElementById("origin_ac");
            HtmlOption option = select.getOptionByValue("BCN");
            page = select.setSelectedAttribute(option, true);

            HtmlSubmitInput submit = (HtmlSubmitInput) page.getByXPath("//input[@type='submit' and @value='Search']").get(2);
            page = submit.click();
            String body = page.asXml();

            while (body.length() > 0) {
                departure.addAll(parsePageDeparture(body));
                try {
                    HtmlAnchor next = page.getAnchorByText("Next");
                    page = next.click();
                    body = page.asXml();
                } catch (ElementNotFoundException exception) {
                    break;
                }
            }

        } catch (Exception exception) {

        }
    }

    private Collection<? extends FlightDetail> parsePageDeparture(String body) {
        List<FlightDetail> fdl = new ArrayList<>();
        Document doc = Jsoup.parse(body);

        Elements tables = doc.select("div#flightResults").select("table");
        for (int ti = 0; ti < tables.size(); ti++) {
            Elements rows = tables.get(ti).select("tbody").select("tr");

            for (int i = 0; i < rows.size(); i++) {
                Elements tds = rows.get(i).select("td");
                String flightNumber = tds.get(1).select("a").text();

                String url = baseSite + tds.get(1).select("a").attr("href");
                FlightDetail detail = new FlightDetail();
                detail.setFlightNumber(flightNumber);
                parseDetailDeparture(detail, PageLoader.Loader(url), ti);
                fdl.add(detail);
            }
        }
        return fdl;
    }

    private void parseDetailDeparture(FlightDetail detail, String body, int ti) {
        Document doc = Jsoup.parse(body);
        Elements tbody = doc.select("div#principalContentFicha_1024").select("table").select("tbody");
        int index = 0;
        if (tbody.size() > 1) {
            index = tbody.size() - 1;
        }
        Elements tds = tbody.get(index).select("tr").get(0).select("td");

        String status = tds.get(5).text();
        String schedulerTime = tds.get(1).text();
        String schedulerDate = tds.get(0).text();

        detail.setActual(getFromString(status, schedulerDate));
        detail.setScheduled(getDateFromString(schedulerTime));

        if (status.contains("Departure expected at")) {
            detail.setStatus(ArrivalStatus.EXPECTED);
        } else if (status.contains("The flight landed at ")) {
            detail.setStatus(ArrivalStatus.LANDED);
        }

        if (ti == 0) {
//            detail.setScheduled();
//            detail.setActual();
            //вчерашние рейсы
        } else {

        }
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
                arrivals.addAll(parsePageArrival(body));
                try {
                    HtmlAnchor next = page.getAnchorByText("Next");
                    page = next.click();
                    body = page.asXml();
                } catch (ElementNotFoundException exception) {
                    break;
                }
            }

        } catch (Exception exception) {

        }

    }

    private Collection<? extends FlightDetail> parsePageArrival(String body) {
        List<FlightDetail> fdl = new ArrayList<>();
        Document doc = Jsoup.parse(body);

        Elements tables = doc.select("div#flightResults").select("table");
        for (int ti = 0; ti < tables.size(); ti++) {
            Elements rows = tables.get(ti).select("tbody").select("tr");

            for (int i = 0; i < rows.size(); i++) {
                Elements tds = rows.get(i).select("td");
                String flightNumber = tds.get(1).select("a").text();

                String url = baseSite + tds.get(1).select("a").attr("href");
                FlightDetail detail = new FlightDetail();
                detail.setFlightNumber(flightNumber);
                parseDetailArrival(detail, PageLoader.Loader(url), ti);
                fdl.add(detail);
            }
        }
        return fdl;
    }

    private void parseDetailArrival(FlightDetail detail, String body, int ti) {
        Document doc = Jsoup.parse(body);
        Elements tbody = doc.select("div#principalContentFicha_1024").select("table").select("tbody");
        int index = 0;
        if (tbody.size() > 1) {
            index = tbody.size() - 1;
        }
        Elements tds = tbody.get(index).select("tr").get(0).select("td");

        String status = tds.get(5).text();
        String schedulerTime = tds.get(1).text();
        String schedulerDate = tds.get(0).text();

        detail.setActual(getFromString(status, schedulerDate));
        detail.setScheduled(getDateFromString(schedulerTime));

        if (status.contains("Arrival expected at ")) {
            detail.setStatus(ArrivalStatus.EXPECTED);
        } else if (status.contains("The flight landed at ")) {
            detail.setStatus(ArrivalStatus.LANDED);
        }

        if (ti == 0) {
//            detail.setScheduled();
//            detail.setActual();
            //вчерашние рейсы
        } else {

        }

    }

    private String getDateFromString(String time) {
        String[] times = time.split(":");
        LocalDateTime ldt = LocalDateTime.now();

        ldt = ldt.withHour(Integer.parseInt(times[0]));
        ldt = ldt.withMinute(Integer.parseInt(times[1]));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(outputTimePattern);
        return ldt.format(fmt) + ":00";
    }

    private String getFromString(String status, String strDate) {
        // The flight landed at 03:18
        String time = status.substring(status.length() - 5, status.length());
        return getDateFromString(time);
    }


}
