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
import org.joda.time.DateTime;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PageLoader {
    private static String USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; ru-RU; rv:1.9.1.4) Gecko/20091016 Firefox/3.5.4 (.NET CLR 3.5.30729)";
    private static HttpClient client = HttpClientBuilder.create().build();

    private static long bytesTransferred = 0;
    private static long startTimeNano = 0;
    private static long endTimeNano = 0;
    private static long timeExecutingSecs = 0;

    private static void startTime() {
        startTimeNano = System.nanoTime();
    }

    private static void endTime() {
        endTimeNano = System.nanoTime();
        timeExecutingSecs += (endTimeNano - startTimeNano) / 1000000;
    }

    public static String Loader(String url) {
        HttpGet httpGet = new HttpGet(url);
        String body = "";
        try {
            startTime();
            httpGet.setHeader("User-Agent", USER_AGENT);
            HttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                body = EntityUtils.toString(response.getEntity(), "UTF-8");
                bytesTransferred += body.length();
            }
        } catch (IOException exception) {
            System.out.println("PageLoader: can't load page: " + exception.getMessage());
        } finally {
            endTime();
        }

        return body;
    }

    public static String LoaderPost(String url) {
        HttpPost httpPost = new HttpPost(url);
        String body = "";
        try {
            startTime();
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
                bytesTransferred += body.length();
            }
        } catch (IOException exception) {
            System.out.println("PageLoader: can't load page: " + exception.getMessage());
        } finally {
            endTime();
        }
        return body;
    }

    public static String LoaderPostSamara(String url) {
        HttpPost httpPost = new HttpPost(url);
        String body = "";
        try {
            startTime();
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
                bytesTransferred += body.length();
            }
        } catch (IOException exception) {
            System.out.println("PageLoader: can't load page: " + exception.getMessage());
        } finally {
            endTime();
        }
        return body;
    }

    public static String LoaderPostKoltsovo(String url) {
        HttpPost httpPost = new HttpPost(url);
        String body = "";
        try {
            startTime();
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
                bytesTransferred += body.length();
            }
        } catch (IOException exception) {
            System.out.println("PageLoader: can't load page: " + exception.getMessage());
        } finally {
            endTime();
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

    public static String LoaderChelyabinksPost(String httpUrl, String paramName, String paramValue) {
        HttpPost httpPost = new HttpPost(httpUrl);
        String body = "";
        try {
            startTime();
            httpPost.addHeader("User-Agent", USER_AGENT);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpPost.addHeader("Accept", "application/json");

            ArrayList<NameValuePair> postParameters;
            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair(paramName, paramValue));

            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
            HttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                body = EntityUtils.toString(response.getEntity());
                bytesTransferred += body.length();
            }
        } catch (IOException exception) {
            System.out.println("PageLoader: can't load page: " + exception.getMessage());
        } finally {
            endTime();
        }
        return body;
    }


    public static String LoaderPostGuandjou(String url, Integer numberPage, Integer typeFlight, String dateFrom, String dateTo) {

        HttpPost httpPost = new HttpPost(url);
        String body = "";
        try {
            httpPost.addHeader("User-Agent", USER_AGENT);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpPost.addHeader("Accept", "application/json");

            ArrayList<NameValuePair> postParameters;
            postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("p_p_id", "flightindex_WAR_flightqueryportlet"));
            postParameters.add(new BasicNameValuePair("p_p_lifecycle", "0"));
            postParameters.add(new BasicNameValuePair("p_p_state", "normal"));
            postParameters.add(new BasicNameValuePair("p_p_mode", "view"));
            postParameters.add(new BasicNameValuePair("p_p_col_id", "column-4"));
            postParameters.add(new BasicNameValuePair("p_p_col_count", "1"));
            postParameters.add(new BasicNameValuePair("_flightindex_WAR_flightqueryportlet_delta", "100"));
            postParameters.add(new BasicNameValuePair("_flightindex_WAR_flightqueryportlet_keywords,", ""));
            postParameters.add(new BasicNameValuePair("_flightindex_WAR_flightqueryportlet_advancedSearch", "false"));
            postParameters.add(new BasicNameValuePair("_flightindex_WAR_flightqueryportlet_andOperator", "true"));
            postParameters.add(new BasicNameValuePair("_flightindex_WAR_flightqueryportlet_flight_number", ""));
            postParameters.add(new BasicNameValuePair("_flightindex_WAR_flightqueryportlet_city_code", ""));
            postParameters.add(new BasicNameValuePair("_flightindex_WAR_flightqueryportlet_airline_code", ""));
            postParameters.add(new BasicNameValuePair("_flightindex_WAR_flightqueryportlet_flight_route", typeFlight.toString()));
            postParameters.add(new BasicNameValuePair("_flightindex_WAR_flightqueryportlet_begin_flight_date", dateFrom));
            postParameters.add(new BasicNameValuePair("_flightindex_WAR_flightqueryportlet_end_flight_date", dateTo));
            postParameters.add(new BasicNameValuePair("_flightindex_WAR_flightqueryportlet_flight_status", "all"));
            postParameters.add(new BasicNameValuePair("_flightindex_WAR_flightqueryportlet_isPassengerPlane", "true"));
            postParameters.add(new BasicNameValuePair("_flightindex_WAR_flightqueryportlet_isDomestic", "true"));
            postParameters.add(new BasicNameValuePair("_flightindex_WAR_flightqueryportlet_isArrival", "true"));
            postParameters.add(new BasicNameValuePair("_flightindex_WAR_flightqueryportlet_languageId", ""));
            postParameters.add(new BasicNameValuePair("_flightindex_WAR_flightqueryportlet_search_type", ""));
            postParameters.add(new BasicNameValuePair("_flightindex_WAR_flightqueryportlet_flight_number_or_city", ""));
            postParameters.add(new BasicNameValuePair("_flightindex_WAR_flightqueryportlet_flight_date", "0"));
            postParameters.add(new BasicNameValuePair("_flightindex_WAR_flightqueryportlet_flight_time", "all"));
            postParameters.add(new BasicNameValuePair("cur", numberPage.toString()));

            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
            startTime();
            HttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                body = EntityUtils.toString(response.getEntity());
                bytesTransferred += body.length();
            }
        } catch (IOException exception) {
            System.out.println("PageLoader: can't load page: " + exception.getMessage());
        } finally {
            endTime();
        }
        return body;

    }

    public static long getTimeExecutingSecs() {
        return timeExecutingSecs;
    }

    public static long getBytesTransferred() {
        return bytesTransferred;
    }
}
