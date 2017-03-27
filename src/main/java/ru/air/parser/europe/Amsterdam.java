package ru.air.parser.europe;

import ru.air.common.AirportEnum;
import ru.air.loader.AbstractLoader;
import ru.air.parser.AirParser;

public class Amsterdam extends AbstractLoader implements AirParser {
    public Amsterdam() {
        setLoader(new AmsterdamLoader(AirportEnum.AMSTERDAM));
    }
}
