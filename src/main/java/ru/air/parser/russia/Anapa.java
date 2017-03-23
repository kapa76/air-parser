package ru.air.parser.russia;

import ru.air.common.AirportEnum;
import ru.air.loader.AbstractLoader;
import ru.air.parser.AirParser;

public class Anapa extends AbstractLoader implements AirParser {
    public Anapa() {
        setLoader(new AnapaLoader(AirportEnum.ANAPA));
    }
}
