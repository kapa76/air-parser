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
            case 4:
            case 5:
            case 6:
                airParser = new Er();
                break;
            case 7:
            case 8:
            case 9:
            case 10:
                airParser = new Du();
                break;
            default:
                break;
        }

        App app = new App(airParser);
        app.load();

    }


}
