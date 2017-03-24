package ru.air.parser.russia;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.loader.AbstractLoader;
import ru.air.parser.AirParser;
import ru.air.parser.russia.KrLoader;

import java.io.IOException;

/**
 * Created by Admin on 23.10.2016.
 */
public class Kr extends AbstractLoader implements AirParser {

    public Kr(){
        setLoader(new KrLoader(AirportEnum.KRASNODAR));
    }

}
