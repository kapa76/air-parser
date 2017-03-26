package ru.air.parser.asia;

import ru.air.common.AirportEnum;
import ru.air.entity.FlightAD;
import ru.air.loader.BaseLoader;

public class GuangzhouLoader extends BaseLoader {

    private String arrivalUrl = "http://www.gbiac.net/hbxx/flightQuery?isArrival=false&isAll=true&recently_20_data=true";
    private String outputTimePattern = "yyyy-MM-d HH:mm";
    private String formatterUrlDate = "yyyy-MM-d";

    public GuangzhouLoader(AirportEnum airportEnum) {
        super(airportEnum);
    }

    @Override
    public FlightAD load() {
        FlightAD flight = new FlightAD();
        flight.setAirportId(getAirport().getAirportId());
//        flight.setArrivals(loadParse(arrivalUrl, true));
//        flight.setDeparture(loadParse(departuresUrl, false));
        return flight;
    }
}
