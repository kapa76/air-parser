package ru.air.loader.js;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import com.gargoylesoftware.htmlunit.javascript.host.Iterator;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDivElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableElement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.air.common.AirportEnum;

import javax.swing.text.TableView;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

/**
 * Created by Admin on 20.10.2016.
 */
public class SimpleLoader {

    private WebClient webClient;
    private AirportEnum airport;

    public SimpleLoader(AirportEnum airport) {
        this.airport = airport;
        this.webClient = new WebClient();
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setThrowExceptionOnScriptError(false);
    }

    private HtmlPage LoadTodayData() {
        return getPage();
    }

    public void load() {
        String str = "";

        HtmlPage today = LoadTodayData();
        parse(today);

        HtmlPage yesterday = LoadYesterdayData(today);
        str = yesterday.asXml();
    }

    private void parse(HtmlPage today) {
        DomElement dom = today.getFirstByXPath("//table[@class='online']");

        List<DomElement> trs = (List<DomElement>) dom.getByXPath("//tr");

        for (DomElement elem : trs) {
            String source = elem.asXml();
            if(source.contains("<TR onclick=")) {
                // elem.getByXPath("TD");

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


    private HtmlPage getPage() {
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

    private HtmlPage LoadYesterdayData(HtmlPage page) {
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
