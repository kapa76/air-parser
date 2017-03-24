package ru.air.parser.russia;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.loader.AbstractLoader;
import ru.air.parser.AirParser;
import ru.air.parser.russia.SoLoader;

import java.io.IOException;

/**
 * Created by kapa on 31.10.16.
 */
public class So extends AbstractLoader implements AirParser {
    private SoLoader loader;

    public So() {
        setLoader( new SoLoader(AirportEnum.SOCHI));
    }

}
