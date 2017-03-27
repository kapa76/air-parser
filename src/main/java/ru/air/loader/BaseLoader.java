package ru.air.loader;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import org.apache.commons.logging.LogFactory;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;
import ru.air.common.AirportEnum;
import ru.air.entity.FlightAD;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;

/**
 * Created by Admin on 23.10.2016.
 */
public abstract class BaseLoader {

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
    }

    public BaseLoader(AirportEnum airport, boolean flag) {
        this.airport = airport;
        this.webClient = new WebClient();
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
        java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
        webClientInit();
    }

    protected void webClientInit(){
        webClient.setIncorrectnessListener(new IncorrectnessListener() {

            @Override
            public void notify(String arg0, Object arg1) {
                // TODO Auto-generated method stub

            }
        });

        webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {
            @Override
            public void scriptException(InteractivePage page, ScriptException scriptException) {
            }

            @Override
            public void timeoutError(InteractivePage page, long allowedTime, long executionTime) {
            }

            @Override
            public void malformedScriptURL(InteractivePage page, String url, MalformedURLException malformedURLException) {
            }

            @Override
            public void loadScriptError(InteractivePage page, URL scriptUrl, Exception exception) {
            }
        });

        webClient.setHTMLParserListener(new HTMLParserListener() {
            @Override
            public void error(String message, URL url, String html, int line, int column, String key) {
            }

            @Override
            public void warning(String message, URL url, String html, int line, int column, String key) {
            }
        });

        webClient.setCssErrorHandler(new ErrorHandler() {

            @Override
            public void warning(CSSParseException exception) throws CSSException {
                // TODO Auto-generated method stub

            }

            @Override
            public void fatalError(CSSParseException exception) throws CSSException {
                // TODO Auto-generated method stub

            }

            @Override
            public void error(CSSParseException exception) throws CSSException {
                // TODO Auto-generated method stub

            }
        });

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

    public HtmlPage getHtmlPage(String url) {
        HtmlPage page = null;
        try {
            page = (HtmlPage) webClient.getPage(url);
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

    public abstract FlightAD load();
}
