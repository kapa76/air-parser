package ru.air.parser.russia;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.parser.AirParser;
import ru.air.parser.russia.KaLoader;

import java.io.IOException;

/**
 * Created by Admin on 06.11.2016.
 */
public class Ka implements AirParser {
    private KaLoader loader;

    public Ka() {
        loader = new KaLoader(AirportEnum.KAZAN);
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