package ru.air.loader;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 19.10.2016.
 */
public class PageLoader {
    private static String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; ru-RU; rv:1.9.1.4) Gecko/20091016 Firefox/3.5.4 (.NET CLR 3.5.30729)";

    public static String Loader(String url) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        String body = "";
        try {
            httpGet.setHeader("User-Agent", USER_AGENT);
            HttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                body = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (IOException exception) {
            System.out.println("PageLoader: can't load page: " + exception.getMessage());
        }

        return body;
    }

    public static String LoaderPost(String url) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        String body = "";
        try {
            httpPost.addHeader("User-Agent", USER_AGENT);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            ArrayList<NameValuePair> postParameters;
            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("active", "prilet"));
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));

            HttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                body = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (IOException exception) {
            System.out.println("PageLoader: can't load page: " + exception.getMessage());
        }
        return body;
    }

    public static String LoaderPostSamara(String url) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        String body = "";
        try {
            httpPost.addHeader("User-Agent", USER_AGENT);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; ");
            httpPost.addHeader("Accept", "application/json;");

            ArrayList<NameValuePair> postParameters;
            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("arrive", ""));
            postParameters.add(new BasicNameValuePair("rip", "127.0.0.1"));
            postParameters.add(new BasicNameValuePair("uag", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36"));

            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));

            HttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                body = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (IOException exception) {
            System.out.println("PageLoader: can't load page: " + exception.getMessage());
        }
        return body;
    }

    public static String LoaderPostKoltsovo(String url) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        String body = "";
        try {
            httpPost.addHeader("User-Agent", USER_AGENT);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpPost.addHeader("Accept", "application/json");
            httpPost.addHeader("Host", "www.koltsovo.ru");
            httpPost.addHeader("Origin", "http://www.koltsovo.ru");
            httpPost.addHeader("Referer", "http://www.koltsovo.ru/ru/onlayn_tablo");
            httpPost.addHeader("Cookie", setCockieFromResponse("http://www.koltsovo.ru/ru/onlayn_tablo"));
            httpPost.addHeader("DNT", "1");

            ArrayList<NameValuePair> postParameters;
            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("arrive", ""));
            postParameters.add(new BasicNameValuePair("rip", "91.246.100.79"));
            postParameters.add(new BasicNameValuePair("uag", USER_AGENT));

            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
            HttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                body = EntityUtils.toString(response.getEntity());
            }
        } catch (IOException exception) {
            System.out.println("PageLoader: can't load page: " + exception.getMessage());
        }
        return body;
    }

    private static String setCockieFromResponse(String url) {
        String value = "";
        try {
            URL obj = new URL(url);
            URLConnection conn = obj.openConnection();
            Map<String, List<String>> map = conn.getHeaderFields();

            List<String> contentLength = map.get("Set-Cookie");
            for (String header : contentLength) {
                value += value + ";" + header;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

}
