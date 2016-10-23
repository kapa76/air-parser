package ru.air.parser.ek;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.air.common.AirportEnum;
import ru.air.common.CommonDateUtil;
import ru.air.parser.ek.entity.FlightTr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

/**
 * Created by Admin on 20.10.2016.
 */
public class EkLoader {

    private static TimeZone EKATERINBURG_TZ = CommonDateUtil.getTimeZone("Ekaterinburg");

    private WebClient webClient;
    private AirportEnum airport;

    public EkLoader(AirportEnum airport) {
        this.airport = airport;
        this.webClient = new WebClient();
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setThrowExceptionOnScriptError(false);
    }

    public void load() {
        String str = "";

        HtmlPage today = getHtmlPage();
        parse(today);

        HtmlPage yesterday = getHtmlPageAfterClick(today);
        str = yesterday.asXml();
    }

    private Set<FlightTr> parse(HtmlPage today) {
        DomElement dom = today.getFirstByXPath("//table[@class='online']");

        List<DomElement> trs = (List<DomElement>) dom.getByXPath("//tr");


        Set<FlightTr> allFlight = new LinkedHashSet<FlightTr>();

        for (DomElement elem : trs) {
            String source = elem.asXml();
            if (source.contains("<TR onclick=")) {
                List<DomElement> tds = (List<DomElement>) elem.getByXPath("TD");

                allFlight.add(fillFlightRecordByTR(tds));

                ScriptResult scriptResult = today.executeJavaScript(elem.getAttribute("onclick"));
                HtmlPage page2 = (HtmlPage) scriptResult.getNewPage();
                String doc = page2.asXml();
                Document docSource = Jsoup.parse(doc);

                String airName = docSource.select("h3").first().text();
                //полный исходник
                Elements es = (docSource.select("div.grayblock.yellowblock").first()).select("div");

                //таблица маршрутов
                Element path = (docSource.select("div.grayblock.yellowblock").first()).select("table").first();


            }
        }

        System.out.println("Найдено: " + allFlight.size() + ", рейсов.");
        return allFlight;
    }

    private FlightTr fillFlightRecordByTR(List<DomElement> tds) {
        //заполняем строку одного рейса и забираем ее детали после этого.
        String flightNumber = tds.get(1).getFirstElementChild().getTextContent();
        String directionFrom = tds.get(2).asText();
        String typeBC = tds.get(3).asText();

        //варианты 23 okt 14:55 или 14:50
        Date planeDate = CommonDateUtil.convertDDMonHHMin(tds.get(4).asText(), EKATERINBURG_TZ); //плановое время

        //варианты 23 okt 14:55 или 14:50
        Date factDate = CommonDateUtil.convertDDMonHHMinORHHMin(tds.get(5).asText(), EKATERINBURG_TZ); //ожидаемое / фактическое время

        String status = tds.get(7).asText();  //статус
        String description = tds.get(8).asText();
        ; //примечание

        return new FlightTr(flightNumber, directionFrom, typeBC, planeDate, factDate, status, description);
    }


    private HtmlPage getHtmlPage() {
        HtmlPage page = null;
        try {
            page = (HtmlPage) webClient.getPage(airport.getUrl());
            webClient.waitForBackgroundJavaScript(2 * 1000);
        } catch (ScriptException scriptException) {
            System.out.println("Loader: " + scriptException.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return page;
    }

    private HtmlPage getHtmlPageAfterClick(HtmlPage page) {
        HtmlPage result = null;
        try {
            HtmlAnchor link = page.getAnchorByText("за прошлые сутки");
            result = link.click();

        } catch (ScriptException scriptException) {
            System.out.println("Loader: " + scriptException.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
