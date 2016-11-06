package ru.air.parser;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.parser.ha.HaLoader;

import java.io.IOException;

/**
 * Created by kapa on 28.10.16.
 */
public class Ha implements AirParser {
    private HaLoader loader;

    public Ha(){
        loader = new HaLoader(AirportEnum.HABAROVSK);
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