package ru;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import ru.air.loader.PageLoader;
import ru.air.loader.js.SimpleLoader;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;

public class App {

    public static void main(String[] args) throws IOException {
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);


        HtmlPage page = SimpleLoader.Loader("http://www.koltsovo.ru/ru/onlayn_tablo");


        String str = page.asXml();


    }


}
