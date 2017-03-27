package ru.air.parser.russia;

import ru.air.common.AirportEnum;
import ru.air.loader.AbstractLoader;
import ru.air.parser.AirParser;

public class Chelyabinks extends AbstractLoader implements AirParser {
    public Chelyabinks() {
        setLoader(new ChelyabinksLoader(AirportEnum.CHELYABINKS));
    }
}
