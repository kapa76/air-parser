package ru.air.parser;

import com.google.gson.Gson;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.parser.ek.entity.FlightTr;
import ru.air.parser.er.ErLoader;
import ru.air.parser.kr.KrLoader;

import java.io.IOException;
import java.util.Set;

/**
 * Created by kapa on 26.10.16.
 */
public class Er implements AirParser {
    private ErLoader loader;

    public Er() {
        loader = new ErLoader(AirportEnum.EREWAN);
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
