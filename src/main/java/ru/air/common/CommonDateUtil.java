package ru.air.common;

import org.apache.commons.httpclient.util.DateUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.apache.http.client.utils.DateUtils.GMT;

public class CommonDateUtil {

    public static Date convertDDMonHHMin(String strDate, TimeZone tz) {
        SimpleDateFormat formatter = new SimpleDateFormat("d MMM H:m");
        //formatter.setTimeZone(tz);
        try {
            return formatter.parse(strDate);
        } catch (ParseException var7) {
            ;
        }

        return null;
    }

    public static Date convertDDMonHHMinORHHMin(String strDate, TimeZone tz) {
        if(strDate.split(" ").length > 1){
            return convertDDMonHHMin(strDate, tz);
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("H:m");
            //formatter.setTimeZone(tz);
            try {
                return formatter.parse(strDate);
            } catch (ParseException var7) {
                ;
            }
        }

        return null;
    }

    public static TimeZone getTimeZone(String city) {

        TimeZone zone = TimeZone.getTimeZone("Europe/Moscow");
        if (city.equals("Ekaterinburg")) {
            zone.setRawOffset(zone.getRawOffset() + 2 * 60 * 60 * 1000);
            zone.setID("Ekaterinburg");
        }

        return zone;
    }
}
