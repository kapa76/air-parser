package ru.air.parser;

import ru.air.common.AirportEnum;
import ru.air.parser.ek.EkLoader;

/**
 * Created by Admin on 23.10.2016.
 */
public class Ek implements AirParser {

    private EkLoader loader;

    public Ek(){
        loader = new EkLoader(AirportEnum.EKATERINBURG);
    }

    public String parse() {
        loader.load();

        return "";
    }
}
