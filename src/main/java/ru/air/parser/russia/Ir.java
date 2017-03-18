package ru.air.parser.russia;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.parser.AirParser;
import ru.air.parser.russia.IrLoader;

import java.io.IOException;

/**
 * Created by kapa on 28.10.16.
 */
public class Ir implements AirParser {
    private IrLoader loader;

    public Ir() {
        loader = new IrLoader(AirportEnum.IRKUTSK);
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
