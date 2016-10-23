package ru.air.parser;

import com.google.gson.Gson;
import ru.air.common.AirportEnum;
import ru.air.parser.ek.EkLoader;
import ru.air.parser.ek.entity.FlightTr;
import ru.air.parser.kr.KrLoader;

import java.util.Set;

/**
 * Created by Admin on 23.10.2016.
 */
public class Kr implements AirParser {
    private KrLoader loader;

    public Kr(){
        loader = new KrLoader(AirportEnum.KRASNODAR);
    }

    public String parse() {
        Set<FlightTr> flight = loader.load();
        Gson value = new Gson();
        value.toJson(flight);
        return value.toString();
    }

}
