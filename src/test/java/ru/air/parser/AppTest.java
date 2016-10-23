package ru.air.parser;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ru.air.common.CommonDateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        testConvertDate();

    }

    public void testConvertDate() {
        TimeZone zone = TimeZone.getTimeZone("Europe/Moscow");
        zone.setRawOffset(zone.getRawOffset() + 2 * 60 * 60 * 1000);
        zone.setID("Ekaterinburg");

        SimpleDateFormat formatter = new SimpleDateFormat("d MMM H:m");
        formatter.setTimeZone(zone);
        try {
            System.out.println( formatter.parse("23 Окт 11:10") );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(zone);
    }
}
