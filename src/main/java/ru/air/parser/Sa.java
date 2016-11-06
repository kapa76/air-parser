package ru.air.parser;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.parser.sa.SaLoader;
import ru.air.parser.so.SoLoader;

import java.io.IOException;

/**
 * Created by Admin on 06.11.2016.
 */
public class Sa implements AirParser {
    private SaLoader loader;

    public Sa(){
        loader = new SaLoader(AirportEnum.SAMARA);
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