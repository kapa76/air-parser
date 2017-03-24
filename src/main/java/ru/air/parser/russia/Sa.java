package ru.air.parser.russia;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.loader.AbstractLoader;
import ru.air.parser.AirParser;
import ru.air.parser.russia.SaLoader;

import java.io.IOException;

/**
 * Created by Admin on 06.11.2016.
 */
public class Sa extends AbstractLoader implements AirParser {

    public Sa(){
        setLoader(new SaLoader(AirportEnum.SAMARA));
    }


}