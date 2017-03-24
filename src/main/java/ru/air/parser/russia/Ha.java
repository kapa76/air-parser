package ru.air.parser.russia;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.loader.AbstractLoader;
import ru.air.parser.AirParser;
import ru.air.parser.russia.HaLoader;

import java.io.IOException;

/**
 * Created by kapa on 28.10.16.
 */
public class Ha extends AbstractLoader implements AirParser {
    public Ha(){
        setLoader(new HaLoader(AirportEnum.HABAROVSK));
    }
}