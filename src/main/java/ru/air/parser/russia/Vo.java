package ru.air.parser.russia;

import ru.air.common.AirportEnum;
import ru.air.loader.AbstractLoader;
import ru.air.parser.AirParser;

/**
 * Created by Admin on 31.10.2016.
 */
public class Vo extends AbstractLoader implements AirParser {
    public Vo(){
        setLoader(new VoLoader(AirportEnum.WLADIWOSTOK));
    }
}