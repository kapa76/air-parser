package ru.air.parser;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ru.air.common.CommonDateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        testConvertDate();
    }


    public void testConvertDate() {


        String test = "2###522885###2###U6 2703###МОСКВА###DME###УРАЛЬСКИЕ АЛ АК АООТ###26.10.2016 11:45#########Прибыл###{imgarr}26, 11:30###26, 09:15###522885###AU627032610201611###EVN###ЕРЕВАН###ЗВАРТНОЦ######A-320###2###30###Российская Федерация (IATA-2)###АРМЕНИЯ######7250########################37.61825###55.75342###4368###+4###44.515054###40.185878######+4###";
        String pattern = "\\w*###\\w*###\\w*###([\\w]+)###.*"; //([\\w]+)###([\\w]+)###([.]+)###([.]+)#########([.]+)"; //###[.]+"; //{imgarr}26, 11:30###26, 09:15###522885###AU627032610201611###EVN###ЕРЕВАН###ЗВАРТНОЦ######A-320###2###30###Российская Федерация (IATA-2)###АРМЕНИЯ######7250########################37.61825###55.75342###4368###+4###44.515054###40.185878######+4###";
        Pattern p = Pattern.compile(pattern);

        Matcher m = p.matcher(test);

        if (m.find()) {
            System.out.println("Рейс: " + m.group(1));
            System.out.println("From: " + m.group(2) + " -> " + m.group(3));
            System.out.println("Company: " + m.group(4));
            System.out.println("d1: " + m.group(5));
            System.out.println("Status: " + m.group(6));
//                System.out.println();


        }


    }
}
