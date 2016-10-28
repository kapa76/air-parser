package ru.air.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kapa on 26.10.16.
 */
public class TestCommon {

    public static void main(String[] args) {
        String dateStr ="28.10.2016 11:45";

        String pattern = "dd.MM.yyyy HH:mm";
        DateFormat formatOutput = new SimpleDateFormat(pattern);


        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT +4:00"));
        try {
            cal.setTime( formatOutput.parse(dateStr) );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("cal: " + cal.getTime());

        String tt = convertDate("dd.MM.yyyy HH:mm", "yyyy-MM-d HH:mm:SS", dateStr);
        System.out.println("cal: " + tt);

    }

    public static String convertDate(String inputPattern, String outputPattern, String strDate){
        DateFormat df = new SimpleDateFormat(inputPattern);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT +4:00"));
        try {
            cal.setTime( df.parse(strDate) );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DateFormat output = new SimpleDateFormat(outputPattern);
        return output.format(cal.getTime());

    }


}
