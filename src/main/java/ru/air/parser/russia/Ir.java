package ru.air.parser.russia;

import ru.air.common.AirportEnum;
import ru.air.loader.AbstractLoader;
import ru.air.parser.AirParser;

public class Ir extends AbstractLoader implements AirParser {
    public Ir() {
        setLoader(new IrLoader(AirportEnum.IRKUTSK));
    }
}
