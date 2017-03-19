package ru.air.parser.asia;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.FlightAD;

import java.io.IOException;

/**
 * Created by Admin on 17.03.2017.
 */
public class Bangkok implements ru.air.parser.AirParser {
    private BangkokLoader loader;

    public Bangkok() {
        loader = new BangkokLoader(AirportEnum.BANGKOK);
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
