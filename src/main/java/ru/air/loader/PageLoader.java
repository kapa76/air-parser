package ru.air.loader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
            System.out.println("Response Code : " + statusCode);

            if(statusCode == 200) {
//                StringBuilder sb = new StringBuilder();
//                String line;
//
//                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//                while ((line = rd.readLine()) != null) {
//                    sb.append(line);
//                }

                body = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch(IOException exception){
            System.out.println("PageLoader: can't load page: " + exception.getMessage() );
        }



        return body;
    }

}
