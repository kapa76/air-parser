package ru.air.parser;

import com.google.gson.Gson;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.parser.ek.EkLoader;
import ru.air.parser.ek.entity.FlightTr;
import ru.air.parser.kr.KrLoader;

import java.io.IOException;
import java.util.Set;

/**
 * Created by Admin on 23.10.2016.
 */
public class Kr implements AirParser {
    private KrLoader loader;

    public Kr(){
        loader = new KrLoader(AirportEnum.KRASNODAR);
    }

    public String parse() {
        Flight flight = loader.load();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = "";

        try {
            json = ow.writeValueAsString(flight);
            System.out.println(json);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }
}
