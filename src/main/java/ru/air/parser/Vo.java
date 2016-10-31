package ru.air.parser;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.parser.so.SoLoader;
import ru.air.parser.vo.VoLoader;

import java.io.IOException;

/**
 * Created by Admin on 31.10.2016.
 */
public class Vo implements AirParser {
    private VoLoader loader;

    public Vo(){
        loader = new VoLoader(AirportEnum.WLADIWOSTOK);
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