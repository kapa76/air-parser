package ru.air.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kapa on 26.10.16.
 */
public class TestCommon {

    public static void main(String[] args) {

        //
        String test = "2###522885###2###U6 2703###МОСКВА###DME###УРАЛЬСКИЕ АЛ АК АООТ###26.10.2016 11:45#########Прибыл###{imgarr}26, 11:30###26, 09:15###522885###AU627032610201611###EVN###ЕРЕВАН###ЗВАРТНОЦ######A-320###2###30###Российская Федерация (IATA-2)###АРМЕНИЯ######7250########################37.61825###55.75342###4368###+4###44.515054###40.185878######+4###";
        String pattern = "(\\d*)###(.*)###(\\d*)###(.*)###(\\w*)###(\\w*)###(.*)###(.*)#########(.*)###(.*)###(.*)###(\\d*)###(\\w*)###(\\w*)###(.*)###(.*)######(.*)###(.*)###(.*)###(.*)###(.*)######(.*)########################(.*)###(.*)###(.*)###(.*)###(.*)###(.*)######(.*)###";

        if(test.contains("2###")){
            test  = test.substring(1, test.length());
        }

        String[] ss = test.split("###");


//            System.out.println("Рейс: " + m.group(1));
//            System.out.println("From: " + m.group(2) + " -> " + m.group(3));
//            System.out.println("Company: " + m.group(4));
//            System.out.println("d1: " + m.group(5));
//            System.out.println("Status: " + m.group(6));m.group(0)

        System.out.println("");

    }
}
