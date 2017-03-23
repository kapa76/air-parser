package ru.air.parser.russia;

import ru.air.common.AirportEnum;
import ru.air.common.ArrivalStatus;
import ru.air.entity.Flight;
import ru.air.entity.FlightDetail;
import ru.air.loader.PageLoader;
import ru.air.loader.BaseLoader;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class ErLoader extends BaseLoader {

    public ErLoader(AirportEnum airport) {
        super(airport);
    }

    public static String convertDate(String inputPattern, String outputPattern, String strDate) {
        DateFormat df = new SimpleDateFormat(inputPattern);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT +4:00"));
        try {
            cal.setTime(df.parse(strDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat output = new SimpleDateFormat(outputPattern);
        return output.format(cal.getTime());

    }

    public static String convertDateWithUpdate(String inputPattern, String outputPattern, String strDate) {
        DateFormat df = new SimpleDateFormat(inputPattern);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT +4:00"));


        try {
            cal.setTime(df.parse(strDate));
            cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            cal.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat output = new SimpleDateFormat(outputPattern);
        return output.format(cal.getTime());

    }

    public Flight load() {
        List<FlightDetail> flightDetailList = new ArrayList<FlightDetail>();

        String body = loadDataFromSite();

        String[] flightString = body.split("@@@@2");
        for (String flightStr : flightString) {
            FlightDetail detail = new FlightDetail();
            String[] flightArray = flightStr.split("###");

            detail.setFlightNumber(flightArray[3]);

            String inputTimePattern = "dd.MM.yyyy HH:mm";
            String outputTimePattern = "yyyy-MM-d HH:mm:ss";

            detail.setScheduled(convertDate(inputTimePattern, outputTimePattern, flightArray[7]));

            if (flightArray[11].length() > 0) {
                String actual = flightArray[11].replace("{imgarr}", "");
                detail.setActual(convertDateWithUpdate("dd, HH:mm", outputTimePattern, actual));
            } else {
                detail.setActual("");
            }

            if (flightArray[8].length() > 0) {
                String estimated = flightArray[8];      //прогнозируемые дата/время приземления (если есть) по местному времени аэропорта в формате YYYY-mm-dd HH:MM:SS
                detail.setEstimated(convertDateWithUpdate("dd, HH:mm", outputTimePattern, estimated));
            } else {
                detail.setEstimated("");
            }

            ArrivalStatus status = ArrivalStatus.UNKNOWN;
            if (flightArray[10].equals("Прибыл")) {
                status = ArrivalStatus.LANDED;
            } else if (flightArray[10].equals("Опоздание")) {

            } else if (flightArray[10].equals("Вовремя")) {
                status = ArrivalStatus.SCHEDULED;
            }
            detail.setStatus(status);
            flightDetailList.add(detail);
        }

        Flight flight = new Flight();
        flight.setAirportId(getAirport().getAirportId());
        flight.setArrivals(flightDetailList);
        return flight;
    }

    private String loadDataFromSite() {
        String url = "http://zvartnots.am/new/get_data.php?minus_hour=" + getMinusHour() + "&plus_hour=" + getPlusHour() + "&dir=arr&lg=ru&tp=hour&_=" + System.currentTimeMillis() % 1000;
        String body = PageLoader.Loader(url);

        if (body.contains("2###")) {
            body = body.substring(1, body.length());
        }

        return body;
    }


}