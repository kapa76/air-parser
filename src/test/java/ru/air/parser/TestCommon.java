package ru.air.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kapa on 26.10.16.
 */
public class TestCommon {

    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);


        String inputPattern = "dd MMM HH:mm";
        String localDateTime = "30 Oct 03:23";
        try {
            cal.setTime((new SimpleDateFormat(inputPattern, Locale.ENGLISH).parse(localDateTime)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("cal: " + cal.getTime());


    }

/*
*  "v049e0a6ce78328cec2d1f214f26b32cb": {
    "Терминал": "A (новый терминал) внутренние воздушные линии",
    "Номер рейса": "LIN-02804",
    "Авиакомпания": "ООО " Вельталь   -   авиа   "",
    "Плановый маршрут": "ХабаровскВладивосток",
    "Время по расписанию": "16:00,  6 ноя 2016",
    "Фактическое время": "16:12,  6 ноя 2016",
    "Тип воздушного судна": "Hawker",
    "Статус": "Прибыл"
  },*/
}
