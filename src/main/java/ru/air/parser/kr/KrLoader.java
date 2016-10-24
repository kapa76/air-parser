package ru.air.parser.kr;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLIElement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.air.common.AirportEnum;
import ru.air.loader.PageLoader;
import ru.air.parser.BaseLoader;
import ru.air.parser.ek.entity.FlightTr;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Set;

public class KrLoader extends BaseLoader {

    public KrLoader(AirportEnum airportEnum) {
        super(airportEnum);
    }

    public Set<FlightTr> load() {

        HtmlPage aa = getHtmlPage(3000);


        String body = PageLoader.Loader(getUrl());
        Document doc = Jsoup.parse(body);

        Elements masthead = doc.select("div.tabs__content");

        //table_online-tablo-mob
        // masthead.select();


        return null;
    }
}
