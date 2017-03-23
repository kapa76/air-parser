package ru.air.parser.asia;

import ru.air.common.AirportEnum;
import ru.air.loader.AbstractLoader;

public class Bangkok extends AbstractLoader implements ru.air.parser.AirParser {
    public Bangkok() {
        setLoader(new BangkokLoader(AirportEnum.BANGKOK));
    }
}
