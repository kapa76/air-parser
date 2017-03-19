package ru.air.parser.europe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.FlightAD;
import ru.air.parser.AirParser;

import java.io.IOException;

/**
 * Created by Admin on 17.03.2017.
 */
public class Amsterdam implements AirParser {
    private AmsterdamLoader loader;

    public Amsterdam() {
        loader = new AmsterdamLoader(AirportEnum.AMSTERDAM);
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
