package ru.air.parser.russia;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.parser.AirParser;
import ru.air.parser.russia.EkLoader;

import java.io.IOException;

/**
 * Created by Admin on 23.10.2016.
 */
public class Ek implements AirParser {

    private EkLoader loader;

    public Ek(){
        loader = new EkLoader(AirportEnum.EKATERINBURG);
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
