package ru;

import ru.air.parser.*;


import java.io.*;
import java.util.logging.Level;

import static java.lang.System.exit;

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
        System.out.println("    Для выбора аэропорта укажите один из номеров");
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
        System.exit(0);
    }

    public static void main(String[] args) throws IOException {
        int airNumber = 0;

        if (args.length <= 0) {
            exit(0);
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
            default:
                usage();
        }

        App app = new App(airParser);
        app.load();

    }


}
