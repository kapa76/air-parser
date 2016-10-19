package ru;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import ru.air.loader.PageLoader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class App {

    public static void main(String[] args) throws IOException {
//        String page = PageLoader.Loader("http://www.koltsovo.ru/ru/onlayn_tablo");

        final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_2);
        final HtmlPage page = (HtmlPage)webClient.getPage("http://www.koltsovo.ru/ru/onlayn_tablo");

        // page.getBody();





    }




}
