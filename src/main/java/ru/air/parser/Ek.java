package ru.air.parser;

import com.google.gson.Gson;
import ru.air.common.AirportEnum;
import ru.air.parser.ek.EkLoader;
import ru.air.parser.ek.entity.FlightTr;

import java.util.Set;

/**
 * Created by Admin on 23.10.2016.
 */
public class Ek implements AirParser {

    private EkLoader loader;

    public Ek(){
        loader = new EkLoader(AirportEnum.EKATERINBURG);
    }

    public String parse() {
        Set<FlightTr> flight = loader.load();
        Gson value = new Gson();
        value.toJson(flight);
        return value.toString();
    }
}
