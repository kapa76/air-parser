package ru.air.parser.europe;

import ru.air.common.AirportEnum;
import ru.air.loader.AbstractLoader;

public class Barselona extends AbstractLoader implements ru.air.parser.AirParser {

    public Barselona() {
        setLoader(new BarselonaLoader(AirportEnum.BARSELONA));
    }


}
