package ru.air.loader.js;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by Admin on 20.10.2016.
 */
public class SimpleLoader {

    public static HtmlPage Loader(String url){
        WebClient webClient = new WebClient();
        HtmlPage page = null;
        try {
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            page = (HtmlPage)webClient.getPage("http://www.koltsovo.ru/ru/onlayn_tablo");
            webClient.waitForBackgroundJavaScript(2 * 1000);
        } catch (ScriptException scriptException) {
            System.out.println("Loader: " + scriptException.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return page;
    }
}
