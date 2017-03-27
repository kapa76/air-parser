package ru.air.parser.russia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
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

public class ArhangelskLoader extends BaseLoader {

    private String urlDeparture = "http://arhaero.ru/pax/flying/online-tablo-departure";
    private String urlArrive = "http://arhaero.ru/pax/flying/online-tablo-arrivals";

    private String outputTimePattern = "yyyy-MM-d HH:mm";

    public ArhangelskLoader(AirportEnum ae) {
        super(ae);
    }

    public FlightAD load() {
        FlightAD flight = new FlightAD();
        flight.setAirportId(getAirport().getAirportId());
        flight.setDeparture(parse(PageLoader.Loader(urlDeparture), true));
        flight.setArrivals(parse(PageLoader.Loader(urlArrive), false));
        return flight;
    }

    private List<FlightDetail> parse(String body, boolean b) {
        List<FlightDetail> detailList = new ArrayList<>();

        org.jsoup.nodes.Document doc = Jsoup.parse(body);
        Elements tables = doc.select("div#maintext").first().select("table");
        for (int i = 0; i < tables.size(); i++) {
            Element table = tables.get(i);
            detailList.addAll(parseTableRows(i, table, b));
        }
        return detailList;
    }

    private Collection<? extends FlightDetail> parseTableRows(int indexDay, Element table, boolean b) {
        List<FlightDetail> detailList = new ArrayList<>();
        Elements rows = table.select("tr");

        for (int i = 0; i < rows.size(); i++) {
            Elements tds = rows.get(i).select("td");
            if (tds.size() == 8) {
                FlightDetail detail = new FlightDetail();

                detail.setFlightNumber(tds.get(4).text());
                String status = tds.get(7).text();
                String schedulerTime = "";
                String actualTime = "";
                if (b) {
                    schedulerTime = tds.get(0).text();
                    actualTime = tds.get(1).text();
                } else {
                    schedulerTime = tds.get(1).text();
                    actualTime = tds.get(2).text();
                }

                if (status.contains("Перенос на")) {
                    detail.setStatus(ArrivalStatus.CANCELLED);
                } else if (status.contains("Отправлен")) {
                    detail.setStatus(ArrivalStatus.DEPARTED);
                } else if (status.equals("Рейс прибыл")) {
                    detail.setStatus(ArrivalStatus.LANDED);
                } else if (status.equals("По расписанию")) {
                    detail.setStatus(ArrivalStatus.SCHEDULED);
                } else if (status.contains("вылет задержан до")) {
                    detail.setStatus(ArrivalStatus.DELAYED);
                } else if (status.equals("отменен")) {
                    detail.setStatus(ArrivalStatus.CANCELLED);
                } else if (status.contains("Произвёл посадку")) {
                    detail.setStatus(ArrivalStatus.LANDED);
                } else if (status.contains("Вылетел в")) {
                    detail.setStatus(ArrivalStatus.EXPECTED);
                } else if(status.contains("Задержан до")){
                    detail.setStatus(ArrivalStatus.DELAYED);
                }

                if (!schedulerTime.equals(actualTime) && actualTime.length() >= 5) {
                    detail.setActual(getDateTime(indexDay, actualTime));
                }

                if (schedulerTime.length() >= 5) {
                    detail.setScheduled(getDateTime(indexDay, schedulerTime));
                } else if(actualTime.length() >= 5){
                    detail.setActual(getDateTime(indexDay, actualTime));
                }
                detail.setEstimated("");
                detailList.add(detail);
            }
        }

        return detailList;
    }

    private String getDateTime(int index, String time) {
        String[] times = time.split(":");
        LocalDateTime ldt = LocalDateTime.now();

        ldt = ldt.withHour(Integer.parseInt(times[0]));
        ldt = ldt.withMinute(Integer.parseInt(times[1]));

        switch (index) {
            case 0: {
                ldt = ldt.minusDays(1);
                break;
            }
            case 2: {
                ldt = ldt.plusDays(1);
                break;
            }
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(outputTimePattern);
        return ldt.format(fmt) + ":00";
    }
}
