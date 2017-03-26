package ru.air.parser.asia;

import jdk.nashorn.internal.parser.AbstractParser;
import ru.air.common.AirportEnum;
import ru.air.loader.AbstractLoader;
import ru.air.parser.AirParser;

/**
 * Created by Admin on 17.03.2017.
 */
public class Guangzhou extends AbstractLoader implements AirParser {
    public Guangzhou(){
        setLoader(new GuangzhouLoader(AirportEnum.GUANGZHOU));
    }
}
