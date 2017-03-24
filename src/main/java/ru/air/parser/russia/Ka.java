package ru.air.parser.russia;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.entity.FlightAD;
import ru.air.loader.AbstractLoader;
import ru.air.parser.AirParser;
import ru.air.parser.russia.KaLoader;

import java.io.IOException;

/**
 * Created by Admin on 06.11.2016.
 */
public class Ka extends AbstractLoader implements AirParser {
    public Ka() {
        setLoader(new KaLoader(AirportEnum.KAZAN));
    }
}