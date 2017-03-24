package ru.air.parser.russia;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.loader.AbstractLoader;
import ru.air.parser.AirParser;
import ru.air.parser.russia.ErLoader;

import java.io.IOException;

/**
 * Created by kapa on 26.10.16.
 */
public class Er extends AbstractLoader implements AirParser {

    public Er() {
        setLoader(new ErLoader(AirportEnum.EREWAN));
    }

}
