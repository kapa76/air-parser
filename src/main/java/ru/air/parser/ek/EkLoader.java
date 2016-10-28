package ru.air.parser.ek;

import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.*;

public class EkLoader extends BaseLoader {

    private static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36";

    private static TimeZone EKATERINBURG_TZ = CommonDateUtil.getTimeZone("Ekaterinburg");

    public EkLoader(AirportEnum airport) {
        super(airport);
    }

    public Set<FlightTr> load() {
        try {
            test();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        HtmlPage today = getHtmlPage(2000);
//        Set<FlightTr> todayFlight = parse(today);
//        HtmlPage yesterday = getHtmlPageAfterClick(today);
//        Set<FlightTr> yesterdayFlight = parse(yesterday);
//        todayFlight.addAll(yesterdayFlight);
//        return todayFlight;
        return null;
    }

    /*
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
*/
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


    private String test() throws UnsupportedEncodingException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = null;

        try {
            HttpGet get = new HttpGet("http://www.koltsovo.ru/ru/onlayn_tablo");
            get.addHeader("Content-Type","application/x-www-form-urlencoded; charset=windows-1251");
            get.addHeader("Accept", "*/*");
            get.addHeader("Accept-Encoding", "gzip, deflate");
            get.addHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4,fr;q=0.2");
            get.addHeader("Connection", "keep-alive");
            client.execute(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpPost post = new HttpPost("http://www.koltsovo.ru/1linetablo.ajax.5.19.php?arrive24");
        post.addHeader("Accept", "*/*");
        post.addHeader("Accept-Encoding", "gzip, deflate");
        post.addHeader("Connection", "keep-alive");
        post.addHeader("Expired", "Fri, 1 Mar 2014 01:01:01 GMT");
        post.addHeader(new BasicHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8"));
        post.addHeader("Cookie", "_ym_uid=1476857168363842848; _ga=GA1.2.651286294.1476857168; _ym_isad=2; __utmt=1; __utma=158817440.651286294.1476857168.1477296892.1477305365.12; __utmb=158817440.1.10.1477305365; __utmc=158817440; __utmz=158817440.1476857168.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); rbsbbx=1; s=3eb9e41099bbe8e680a6d2c4005980bb");
        post.addHeader("Host","www.koltsovo.ru");
        post.addHeader("Origin","http://www.koltsovo.ru");
        post.addHeader("Referer","http://www.koltsovo.ru/ru/onlayn_tablo");
        post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36");

        List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("arrive", "1"));
        postParameters.add(new BasicNameValuePair("uag", USER_AGENT));
        postParameters.add(new BasicNameValuePair("rip", "91.246.100.79"));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParameters);
        post.setEntity(entity);

        String body = "";
        try {
            response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();

            for (Header h : response.getAllHeaders()) {
                System.out.println(h.getName() + ": " + h.getValue());
            }

            if (statusCode == 200) {
                body = EntityUtils.toString(response.getEntity());
            }
        } catch (IOException exception) {
            System.out.println("PageLoader: can't load page: " + exception.getMessage());
        }

        System.out.println("Body size: " + body.length());

        return body;
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
