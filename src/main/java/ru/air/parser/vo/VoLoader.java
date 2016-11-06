package ru.air.parser.vo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringEscapeUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.air.common.AirportEnum;
import ru.air.common.ArrivalStatus;
import ru.air.entity.Flight;
import ru.air.entity.FlightDetail;
import ru.air.loader.PageLoader;
import ru.air.parser.BaseLoader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Admin on 31.10.2016.
 */
public class VoLoader extends BaseLoader {

    private String URL = "http://vvo.aero";
    private String outputTimePattern = "yyyy-MM-d HH:mm:ss";

    public VoLoader(AirportEnum airportEnum) {
        super(airportEnum);
    }

    public Flight load() {
        Flight flight = new Flight();
        flight.setAirportId(getAirport().getAirportId());
        flight.setArrivals(loadDataFromSite());
        return flight;
    }

    private List<FlightDetail> loadDataFromSite() {
        List<FlightDetail> value = new ArrayList<>();
        value.addAll(parse(PageLoader.Loader(URL)));
        return value;
    }

    private String getDate(String dt, String inputPattern) {
        DateFormat df = new SimpleDateFormat(inputPattern);
        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(df.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat output = new SimpleDateFormat(outputTimePattern);
        return output.format(cal.getTime());
    }

    private List<FlightDetail> parse(String strBody) {
        List<FlightDetail> detailList = new ArrayList<>();

        Document doc = Jsoup.parse(strBody);
        //Elements parts = doc.select("#arrival > table > tbody");
        String scripts = doc.select("#arrival > script").html();

        scripts = scripts.replace("jQuery.extend(table_addition,", "");
        scripts = scripts.substring(0, scripts.length() - 2);
        String ss = new String(scripts.getBytes(Charset.forName("UTF-8")));
        ss = StringEscapeUtils.unescapeJava(ss);

        ss = ss.replace("ООО \"Вельталь-авиа\"", "ООО Вельталь-авиа");

        Map<String, Map> myMap = new HashMap<String, Map>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            myMap = objectMapper.readValue(ss, HashMap.class);
            Set<String> flights = myMap.keySet();

            while (flights.iterator().hasNext()) {
                FlightDetail detail = new FlightDetail();

                String keyFlight = flights.iterator().next();
                Map<String, String> vals = myMap.get(keyFlight);

                detail.setFlightNumber(vals.get("Номер рейса"));
                String status = vals.get("Статус");

                if (status.equals("Прибыл")) {
                    detail.setStatus(ArrivalStatus.LANDED);
                } else if (status.equals("По расписанию")) {
                    detail.setStatus(ArrivalStatus.SCHEDULED);
                } else if (status.contains("Задерживается")) {
                    detail.setStatus(ArrivalStatus.DELAYED);
                }

                String schedulerDateTime = vals.get("Время по расписанию"); //: "07:50,  6 ноя 2016",
                if (schedulerDateTime != null) {
                    detail.setScheduled(getDate(schedulerDateTime, "HH:mm, dd MMM yyyy")); //"yyyy-MM-d HH:mm:ss";
                }
                String actualDateTime = vals.get("Фактическое время"); //: "07:14,  6 ноя 2016",
                if (actualDateTime != null) {
                    detail.setActual(getDate(actualDateTime, "HH:mm, dd MMM yyyy"));
                } else {
                    actualDateTime = vals.get("Расчетное время");
                    if (actualDateTime != null) {
                        detail.setActual(getDate(actualDateTime, "HH:mm, dd MMM yyyy"));
                    }
                }


                flights.remove(keyFlight);

                detailList.add(detail);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return detailList;
    }

    private String getDateTime(int i, String time) {

        String[] tt = time.split(":");
        DateTime dt = new DateTime()
                .withHourOfDay(Integer.parseInt(tt[0]))
                .withMinuteOfHour(Integer.parseInt(tt[1]));

        //1900 + dtemp.getYear(), dtemp.getMonth(), dtemp.getDate(), Integer.parseInt(tt[0]), Integer.parseInt(tt[1]));
        dt = dt.plusDays(i);

        DateTimeFormatter fmt = DateTimeFormat.forPattern(outputTimePattern);

        return fmt.print(dt);
    }

}