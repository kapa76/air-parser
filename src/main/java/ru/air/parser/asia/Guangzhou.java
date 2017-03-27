package ru.air.parser.asia;

import ru.air.common.AirportEnum;
import ru.air.loader.AbstractLoader;
import ru.air.parser.AirParser;

public class Guangzhou extends AbstractLoader implements AirParser {
    public Guangzhou(){
        setLoader(new GuangzhouLoader(AirportEnum.GUANGZHOU));
    }
}
