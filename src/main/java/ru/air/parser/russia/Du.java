package ru.air.parser.russia;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import ru.air.common.AirportEnum;
import ru.air.entity.Flight;
import ru.air.loader.AbstractLoader;
import ru.air.parser.AirParser;
import ru.air.parser.russia.DuLoader;

import java.io.IOException;

/**
 * Created by kapa on 28.10.16.
 */
public class Du extends AbstractLoader implements AirParser {
    public Du() {
        setLoader(new DuLoader(AirportEnum.DUSHANBE));
    }
}
