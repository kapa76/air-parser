package ru;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import ru.air.common.AirportEnum;
import ru.air.loader.PageLoader;
import ru.air.loader.js.SimpleLoader;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

public class App {


    public App(){
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
    }

    public void load(){

        SimpleLoader loader = new SimpleLoader(AirportEnum.EKATERINBURG);
        loader.load();

    }

    public static void main(String[] args) throws IOException {

        App app = new App();
        app.load();

    }


}
