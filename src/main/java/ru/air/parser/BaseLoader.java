package ru.air.parser;

import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import ru.air.common.AirportEnum;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by Admin on 23.10.2016.
 */
public class BaseLoader {

    private int minusHour = -4;
    private int plusHour = 12;

    private WebClient webClient;
    private AirportEnum airport;

    public WebClient getWebClient() {
        return webClient;
    }


    public AirportEnum getAirport(){
        return airport;
    }

    public String getUrl() {
        return airport.getUrl();
    }

    public BaseLoader(AirportEnum airport) {
        this.airport = airport;
        this.webClient = new WebClient();
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
    }

    public HtmlPage getHtmlPage() {
        HtmlPage page = null;
        try {
            page = (HtmlPage) webClient.getPage(airport.getUrl());
        } catch (ScriptException scriptException) {
            ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return page;
    }

    public HtmlPage getHtmlPage(long delay) {
        HtmlPage page = null;
        try {
            page = (HtmlPage) webClient.getPage(airport.getUrl());
            webClient.waitForBackgroundJavaScript(delay);
        } catch (ScriptException scriptException) {
            System.out.println("Loader: " + scriptException.getMessage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return page;
    }

    public int getMinusHour() {
        return minusHour;
    }

    public void setMinusHour(int minusHour) {
        this.minusHour = minusHour;
    }

    public int getPlusHour() {
        return plusHour;
    }

    public void setPlusHour(int plusHour) {
        this.plusHour = plusHour;
    }
}
