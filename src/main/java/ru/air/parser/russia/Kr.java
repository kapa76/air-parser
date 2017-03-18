package ru.air.parser.russia;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.parser.AirParser;
import ru.air.parser.russia.KrLoader;

import java.io.IOException;

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
