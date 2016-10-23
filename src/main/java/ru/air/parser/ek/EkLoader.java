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
import ru.air.common.CommonUtil;
import ru.air.parser.BaseLoader;
import ru.air.parser.ek.entity.FlightTr;
import ru.air.parser.ek.entity.Routing;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

/**
 * Created by Admin on 20.10.2016.
 */
public class EkLoader extends BaseLoader {

    private static TimeZone EKATERINBURG_TZ = CommonDateUtil.getTimeZone("Ekaterinburg");

    public EkLoader(AirportEnum airport) {
        super(airport);
    }

    public Set<FlightTr> load() {
        HtmlPage today = getHtmlPage(2000);
        Set<FlightTr> todayFlight = parse(today);

        HtmlPage yesterday = getHtmlPageAfterClick(today);
        Set<FlightTr> yesterdayFlight = parse(yesterday);

        todayFlight.addAll(yesterdayFlight);
        return todayFlight;
    }

    private Set<FlightTr> parse(HtmlPage today) {
        DomElement dom = today.getFirstByXPath("//table[@class='online']");
        List<DomElement> trs = (List<DomElement>) dom.getByXPath("//tr");
        Set<FlightTr> allFlight = new LinkedHashSet<FlightTr>();

        for (DomElement elem : trs) {
            String source = elem.asXml();
            if (source.contains("<TR onclick=")) {
                List<DomElement> tds = (List<DomElement>) elem.getByXPath("TD");

                ScriptResult scriptResult = today.executeJavaScript(elem.getAttribute("onclick"));
                HtmlPage page2 = (HtmlPage) scriptResult.getNewPage();
                String doc = page2.asXml();
                Document docSource = Jsoup.parse(doc);

//                String airName = docSource.select("h3").first().text();
//                //полный исходник
//                Elements es = (docSource.select("div.grayblock.yellowblock").first()).select("div");

                //таблица маршрутов
                Element path = (docSource.select("div.grayblock.yellowblock").first()).select("table").first();
                Elements trs2 = path.select("tr");

                FlightTr flightTr = fillFlightRecordByTR(tds);
                Routing route = parseDetailRoute(trs2);
                if (route != null) {
                    flightTr.getRoute().add(route);
                }
                allFlight.add(flightTr);

            }
        }

        System.out.println("Найдено: " + allFlight.size() + ", рейсов.");
        return allFlight;
    }

    private Routing parseDetailRoute(Elements rows) {

//        if (rows.size() > 3) {
//            System.out.println("Длинный маршрут");
//        }

        if (rows.size() == 1)
            return null;

        int indexRecordTable = rows.size() - 1;
        Element row = rows.get(indexRecordTable);
        Elements cols = row.select("td");

        String airportName = cols.get(0).text();

        Date currDateTime = null;
        Date localDateTime = null;

        try {
            if (!cols.get(1).text().contains("Отменен")) {
                String result = CommonUtil.parseText(cols.get(1).text());
                String[] tempDates = result.split("\\(");
                String strDate = tempDates[0];
                String strLocalDate = tempDates[1].replaceAll("\\)", "");
                strLocalDate = strLocalDate.trim();

                currDateTime = CommonDateUtil.convertHMdmy(strDate);
                localDateTime = CommonDateUtil.convertHM(strLocalDate);
            }
        } catch (Exception e) {
            ;
        }

        return new Routing(airportName, currDateTime, localDateTime);
    }


    private FlightTr fillFlightRecordByTR(List<DomElement> tds) {
        //Парсим одну строку таблицы на странице из таблицы рейсов

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
