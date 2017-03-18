package ru;

import ru.air.parser.*;
import ru.air.parser.asia.Bangkok;
import ru.air.parser.asia.Chhatrapati;
import ru.air.parser.asia.Guangzhou;
import ru.air.parser.europe.Amsterdam;
import ru.air.parser.europe.Barselona;
import ru.air.parser.europe.CharlesDeGaulle;
import ru.air.parser.russia.*;
import ru.air.parser.usa.Atlanta;


import java.io.*;
import java.util.logging.Level;

public class App {

    private AirParser airParser;

    public App(AirParser airParser) {
        this.airParser = airParser;
    }

    public App() {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
    }

    public void load() {
        airParser.parse();
    }


    private static void usage() {
        System.out.println("Usage: Необходимо указать номер аэропорта");
        System.out.println("    Для выбора аэропорта укажите один из номеров:");
        System.out.println("    1  SVX - Екатеринбург (Кольцово)");
        System.out.println("    2  KRR - Краснодар (Пашковский)");
        System.out.println("    3  AER - Сочи ");
        System.out.println("    4  KUF - Самара (Курумоч) ");
        System.out.println("    5  KHV - Хабаровск (Новый) ");
        System.out.println("    6  EVN - Ереван (Звартноц) ");
        System.out.println("    7  VVO - Владивосток (Кневичи) ");
        System.out.println("    8  KZN - Казань ");
        System.out.println("    9  IKT - Иркутск ");
        System.out.println("    10 DYU - Душанбе");

        System.out.println("    11 AAQ - Анапа");
        System.out.println("    12 CEK - Челябинск");
        System.out.println("    13 ARH - Архангельск");
        System.out.println("    14 AMS - Амстердам North Holland");
        System.out.println("    15 ATL - Hartsfield Jackson Atlanta International, Georgia");
        System.out.println("    16 BCN - Barcelona International");
        System.out.println("    17 BKK - Suvarnabhumi Bangkok");
        System.out.println("    18 BOM - Chhatrapati Shivaji International Mumbai");
        System.out.println("    19 CAN - Guangzhou Baiyun International Guangzhou");
        System.out.println("    20 CDG - Charles de Gaulle International Paris");

        System.exit(0);
    }

    public static void main(String[] args) throws IOException {
        int airNumber = 0;

        if (args.length <= 0) {
            usage();
        } else {
            airNumber = Integer.parseInt(args[0]);
        }

        AirParser airParser = null;
        switch (airNumber) {
            case 1:
                airParser = new Ek();
                break;
            case 2:
                airParser = new Kr();
                break;
            case 3:
                airParser = new So();
                break;
            case 4:
                airParser = new Sa();
                break;
            case 5:
                airParser = new Ha();
                break;
            case 6:
                airParser = new Er();
                break;
            case 7:
                airParser = new Vo();
                break;
            case 8:
                airParser = new Ka();
                break;
            case 9:
                airParser = new Ir();
                break;
            case 10:
                airParser = new Du();
                break;
            case 11:
                airParser = new Anapa();
                break;
            case 12:
                airParser = new Chelyabinks();
                break;
            case 13:
                airParser = new Arhangelsk();
                break;
            case 14:
                airParser = new Amsterdam();
                break;
            case 15:
                airParser = new Atlanta();
                break;
            case 16:
                airParser = new Barselona();
                break;
            case 17:
                airParser = new Bangkok();
                break;
            case 18:
                airParser = new Chhatrapati();
                break;
            case 19:
                airParser = new Guangzhou();
                break;
            case 20:
                airParser = new CharlesDeGaulle();
                break;
            default:
                usage();
        }

        App app = new App(airParser);
        app.load();

    }


}
