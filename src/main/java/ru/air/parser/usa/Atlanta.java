package ru.air.parser.usa;

import ru.air.common.AirportEnum;
import ru.air.loader.AbstractLoader;

public class Atlanta extends AbstractLoader implements ru.air.parser.AirParser {

    public Atlanta() {
        setLoader(new AtlantaLoader(AirportEnum.JACKSON_ATLANTA));
    }

}
