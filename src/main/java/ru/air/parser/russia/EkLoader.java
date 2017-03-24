package ru.air.parser.russia;

import ru.air.common.AirportEnum;
import ru.air.common.ArrivalStatus;
import ru.air.entity.Flight;
import ru.air.entity.FlightAD;
import ru.air.entity.FlightDetail;
import ru.air.loader.PageLoader;
import ru.air.loader.BaseLoader;
import ru.air.parser.xml.OnlineArrive;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 06.11.2016.
 */
public class EkLoader extends BaseLoader {

    private String url = "http://www.koltsovo.ru/1linetablo.ajax.5.19.php?arrive";
    private String url24 = "http://www.koltsovo.ru/1linetablo.ajax.5.19.php?arrive24h";
    private String outputTimePattern = "yyyy-MM-d HH:mm:ss";

    public EkLoader(AirportEnum airport) {
        super(airport);
    }

    public FlightAD load() {
        FlightAD flight = new FlightAD();
        flight.setAirportId(getAirport().getAirportId());
        flight.setArrivals(loadDataFromSite());
        return flight;
    }

    private List<FlightDetail> loadDataFromSite() {
        List<FlightDetail> value = new ArrayList<>();

        value.addAll(parse(PageLoader.LoaderPostKoltsovo(url24)));
        value.addAll(parse(PageLoader.LoaderPostKoltsovo(url)));

        return value;
    }

    private String getDate(String inputPattern, String dt) {
        DateFormat df = new SimpleDateFormat(inputPattern);
        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(df.parse(dt));
        } catch (ParseException e) {
            return "";
        }

        DateFormat output = new SimpleDateFormat(outputTimePattern);
        return output.format(cal.getTime());
    }

    private List<FlightDetail> parse(String strBody) {
        List<FlightDetail> detailList = new ArrayList<>();

        JAXBContext jaxbContext = null;

        try {

            jaxbContext = JAXBContext.newInstance(OnlineArrive.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(strBody);
            OnlineArrive arrive = (OnlineArrive) unmarshaller.unmarshal(reader);

            for (OnlineArrive.Flight flight : arrive.getFlight()) {
                FlightDetail detail = new FlightDetail();

                detail.setFlightNumber(flight.getRfEng() + "-" + flight.getFlt());

                String status = flight.getStatuzz();
                if (status.equals("Прибыл")) {
                    detail.setStatus(ArrivalStatus.LANDED);
                }

                String[] temp = flight.getRoute().getStatus().split(";");
                if (temp.length > 1) {
                    String dt = temp[1];
                    if(dt.contains("Прибыл в ")){
                        dt = dt.replace("Прибыл в ", "");
                    } else if(dt.contains("Ожидается прибытие в ")){
                        dt = dt.replace("Ожидается прибытие в ", "");
                    }

                    String date = getDate("HH:mm dd.MM.yyyy (HH:mm )", dt);
                    detail.setScheduled(date);
                }

                detailList.add(detail);
            }

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return detailList;
    }
}
