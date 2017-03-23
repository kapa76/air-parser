package ru.air.parser.russia;

import ru.air.common.AirportEnum;
import ru.air.loader.AbstractLoader;
import ru.air.parser.AirParser;

public class Arhangelsk extends AbstractLoader implements AirParser {
    public Arhangelsk() {
        setLoader(new ArhangelskLoader(AirportEnum.TALAGI_ARHANGELSK));
    }
}
