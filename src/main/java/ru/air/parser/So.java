package ru.air.parser;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.parser.so.SoLoader;

import java.io.IOException;

/**
 * Created by kapa on 31.10.16.
 */
public class So implements AirParser {
    private SoLoader loader;

    public So(){
        loader = new SoLoader(AirportEnum.SOCHI);
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
