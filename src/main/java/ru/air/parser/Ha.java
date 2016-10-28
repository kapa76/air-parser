package ru.air.parser;

import com.google.gson.Gson;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.parser.ek.entity.FlightTr;
import ru.air.parser.ha.HaLoader;
import ru.air.parser.kr.KrLoader;

import java.io.IOException;
import java.util.Set;

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