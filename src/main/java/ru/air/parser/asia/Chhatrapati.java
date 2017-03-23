package ru.air.parser.asia;

import ru.air.common.AirportEnum;
import ru.air.loader.AbstractLoader;

public class Chhatrapati extends AbstractLoader implements ru.air.parser.AirParser {
    public Chhatrapati(){
        setLoader(new ChhatrapatiLoader(AirportEnum.CHHATRAPATI_SHIVAJI));
    }
}
