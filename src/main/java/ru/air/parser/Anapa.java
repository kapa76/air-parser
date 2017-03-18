package ru.air.parser;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.FlightAD;

import java.io.IOException;

/**
 * Created by Admin on 17.03.2017.
 */
public class Anapa implements AirParser {

    private AnapaLoader loader;

    public Anapa() {
        loader = new AnapaLoader(AirportEnum.ANAPA);
    }

    public String parse() {
        FlightAD flight = loader.load();

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
