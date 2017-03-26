package ru.air.parser.europe;

import ru.air.common.AirportEnum;
import ru.air.loader.AbstractLoader;

public class CharlesDeGaulle extends AbstractLoader implements ru.air.parser.AirParser {
    public CharlesDeGaulle() {
        setLoader(new CharlesDeGaulleLoader(AirportEnum.CHARLES_DE_GAULLE));
    }
}
