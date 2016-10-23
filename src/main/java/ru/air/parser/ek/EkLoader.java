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
import ru.air.parser.ek.entity.FlightTr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 20.10.2016.
 */
public class EkLoader {

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

    private void parse(HtmlPage today) {
        DomElement dom = today.getFirstByXPath("//table[@class='online']");

        List<DomElement> trs = (List<DomElement>) dom.getByXPath("//tr");

        for (DomElement elem : trs) {
            String source = elem.asXml();
            if(source.contains("<TR onclick=")) {
                // elem.getByXPath("TD");

                //заполняем строку одного рейса и забираем ее детали после этого.
                FlightTr flightTr = new FlightTr();

                String flightNumber; //рейс
                String direction; //направление
                String typeBC; //тип ВС
                Date planeDate; //плановое время
                Date factDate; //ожидаемое / фактическое время
                String status; //статус
                String description; //примечание

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
