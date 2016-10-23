package ru.air.parser.kr;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLIElement;
import ru.air.common.AirportEnum;
import ru.air.parser.BaseLoader;
import ru.air.parser.ek.entity.FlightTr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Set;

public class KrLoader extends BaseLoader {

    public KrLoader(AirportEnum airportEnum) {
        super(airportEnum);
    }

    private HtmlPage getHtmlPageAfterClickArrival(HtmlPage page) {
        HtmlPage result = null;
        try {
            HTMLLIElement li = (HTMLLIElement)page.getByXPath( "/li[class='tabs__item arrive tabs__item_active']");
            li.click();

        } catch (ScriptException scriptException) {
            System.out.println("Loader: " + scriptException.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Set<FlightTr> load() {
        getHtmlPageAfterClickArrival(getHtmlPage());


//        Set<FlightTr> todayFlight = parse(today);
//
//        HtmlPage yesterday = getHtmlPageAfterClick(today);
//        Set<FlightTr> yesterdayFlight = parse(yesterday);
//
//        todayFlight.addAll(yesterdayFlight);
//        return todayFlight;
        return null;
    }
}
