package ru.air.parser.russia;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.loader.AbstractLoader;
import ru.air.parser.AirParser;
import ru.air.parser.russia.EkLoader;

import java.io.IOException;

/**
 * Created by Admin on 23.10.2016.
 */
public class Ek extends AbstractLoader implements AirParser {

    public Ek(){
        setLoader(new EkLoader(AirportEnum.EKATERINBURG));
    }

}
