package ru.air.parser.russia;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.parser.AirParser;
import ru.air.parser.russia.DuLoader;

import java.io.IOException;

/**
 * Created by kapa on 28.10.16.
 */
public class Du implements AirParser {
    private DuLoader loader;

    public Du() {
        loader = new DuLoader(AirportEnum.DUSHANBE);
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
