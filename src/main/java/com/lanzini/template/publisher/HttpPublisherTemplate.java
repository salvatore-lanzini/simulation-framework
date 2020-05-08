package com.lanzini.template.publisher;

import com.lanzini.exception.HttpPublisherException;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Useful Template to publish messages how to HttpRequest
 */
public class HttpPublisherTemplate {

    private static final CloseableHttpClient client = HttpClients.createDefault();

    /**
     * Perform an Http GET Request
     * @param url server url
     * @return response json body string
     * @throws HttpPublisherException if something went wrong
     */
    public static String get(String url) throws HttpPublisherException{
        try {
            return executeGetOrDelete(new HttpGet(url));
        }catch (Exception e){
            throw new HttpPublisherException(e.getMessage());
        }
    }

    /**
     * Perform an Http DELETE Request
     * @param url server url
     * @return response json body string
     * @throws HttpPublisherException if something went wrong
     */
    public static String delete(String url) throws HttpPublisherException{
        try {
            return executeGetOrDelete(new HttpDelete(url));
        }catch (Exception e){
            throw new HttpPublisherException(e.getMessage());
        }
    }

    /**
     * Perform an Http POST Request with json body
     * @param url server url
     * @param body request body
     * @param <T> the type of the body
     * @return response json body string
     * @throws HttpPublisherException if something went wrong
     */
    public static <T> String post(String url, T body) throws HttpPublisherException{
        try {
            return executePostOrPut(new HttpPost(url),Json.stringify(body));
        }catch (Exception e){
            throw new HttpPublisherException(e.getMessage());
        }
    }

    /**
     * Perform an Http POST Request
     * @param url server url
     * @param body request body
     * @return response json body string
     * @throws HttpPublisherException if something went wrong
     */
    public static String post(String url, String body) throws HttpPublisherException{
        try {
            return executePostOrPut(new HttpPost(url),body);
        }catch (Exception e){
            throw new HttpPublisherException(e.getMessage());
        }
    }

    /**
     * Perform an Http PUT Request with json body
     * @param url server url
     * @param body request body
     * @param <T> the type of the body
     * @return response json body string
     * @throws HttpPublisherException if something went wrong
     */
    public static <T> String put(String url, T body) throws HttpPublisherException{
        try {
            return executePostOrPut(new HttpPut(url),Json.stringify(body));
        }catch (Exception e){
            throw new HttpPublisherException(e.getMessage());
        }
    }

    /**
     * Perform an Http PUT Request
     * @param url server url
     * @param body request body
     * @return response json body string
     * @throws HttpPublisherException if something went wrong
     */
    public static String put(String url, String body) throws HttpPublisherException{
        try {
            return executePostOrPut(new HttpPut(url),body);
        }catch (Exception e){
            throw new HttpPublisherException(e.getMessage());
        }
    }

    private static String executeGetOrDelete(HttpRequestBase method) throws IOException {
        CloseableHttpResponse response = client.execute(method);
        return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    }

    private static String executePostOrPut(HttpEntityEnclosingRequestBase method, String body) throws IOException {
        StringEntity entity = new StringEntity(body);
        method.setEntity(entity);
        method.setHeader("Accept", "application/json");
        method.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(method);
        return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    }
}
