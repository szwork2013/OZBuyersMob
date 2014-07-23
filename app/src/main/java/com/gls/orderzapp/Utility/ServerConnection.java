package com.gls.orderzapp.Utility;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by prajyot on 1/4/14.
 */
public class ServerConnection {

    public static HttpClient httpClient;
    public static HttpContext localContext;
    public static final String url = "http://ec2-54-254-210-45.ap-southeast-1.compute.amazonaws.com:5000";
//    public static final String url = "https://ec2-54-255-211-121.ap-southeast-1.compute.amazonaws.com";
//    public static final String url="http://192.169.1.71:5000";

    //    public static final String url="http://192.168.1.40:5000";
    public static HttpClient getHttpClient() {
        if (httpClient == null) {
            httpClient = new DefaultHttpClient();
        }
        return httpClient;
    }

    public static void setHttpClient(HttpClient httpClient) {
        ServerConnection.httpClient = httpClient;
    }

    public static HttpContext getLocalContext() {
        if (localContext == null)
            localContext = new BasicHttpContext();
        return localContext;
    }

    public static String getUrl() {
        return url;
    }

    public static void setLocalContext(HttpContext localContext) {
        ServerConnection.localContext = localContext;
    }

    public static String getASCIIContentFromEntity(HttpEntity entity) {

        InputStream in = null;
        StringBuffer out = null;
        try {
            in = entity.getContent();
            out = new StringBuffer();
            int n = 1;
            while (n > 0) {
                byte[] b = new byte[4096];
                n = in.read(b);
                if (n > 0)
                    out.append(new String(b, 0, n));
            }
        } catch (IllegalStateException ie) {
            ie.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (OutOfMemoryError oom) {
            oom.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return out.toString();
    }

    public static String executeGet(Context c, String url) {

        httpClient = getHttpClient();
        localContext = getLocalContext();
        String json = "";
//        CheckConnection cc = new CheckConnection(c);
//        isConnect = cc.isConnectingToInternet();
        HttpEntity entity;
        HttpGet httpGet = new HttpGet(ServerConnection.getUrl() + url);
        try {
            HttpResponse response = httpClient.execute(httpGet, localContext);
            entity = response.getEntity();
            json = getASCIIContentFromEntity(entity);
        } catch (HttpHostConnectException hhce) {
            hhce.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return json;
    }

    public static String executePost(UrlEncodedFormEntity urlEnc, String urlPath) {
        httpClient = getHttpClient();
        localContext = getLocalContext();
//        CheckConnection cc = new CheckConnection(c);
//        isConnect = cc.isConnectingToInternet();
        HttpEntity entity;
        String entityString = "";
        HttpPost httpPost = new HttpPost(ServerConnection.getUrl() + urlPath);

        try {
            httpPost.setEntity(urlEnc);
            HttpResponse response = httpClient.execute(httpPost, localContext);
            entity = response.getEntity();
            entityString = getASCIIContentFromEntity(entity);

        } catch (HttpHostConnectException hhce) {
            hhce.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return entityString;

    }

    public static String executePost1(String param, String urlPath) {
        httpClient = getHttpClient();
        localContext = getLocalContext();
//        CheckConnection cc = new CheckConnection(c);
//        isConnect = cc.isConnectingToInternet();
        HttpEntity entity;
        String entityString = "";
        HttpPost httpPost = new HttpPost(ServerConnection.getUrl() + urlPath);

        try {

            StringEntity stringEntity = new StringEntity(param);
            stringEntity.setContentType("application/json");

            httpPost.setEntity(stringEntity);

            HttpResponse response = httpClient.execute(httpPost, localContext);
            entity = response.getEntity();
            entityString = getASCIIContentFromEntity(entity);

        } catch (HttpHostConnectException hhce) {
            hhce.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return entityString;

    }

    public static String executePost2(String param, String imgPath, String urlPath) {
        httpClient = getHttpClient();
        localContext = getLocalContext();
        HttpEntity entity;
        String entityString = "";
        File file = new File(imgPath);

        Log.d("Param from connection", param);

        //Create the POST object
        HttpPost httppost = new HttpPost(ServerConnection.getUrl() + urlPath);
        try {
            MultipartEntity mEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            //Add the file to the content's body
            ContentBody cbFile = new FileBody(file, "image/jpeg");
            mEntity.addPart("logo", cbFile);

            mEntity.addPart("data", new StringBody(param));


            //We add the entity to the post request
            httppost.setEntity(mEntity);


            //Execute post request
            HttpResponse response = httpClient.execute(httppost, localContext);

            entity = response.getEntity();
            entityString = getASCIIContentFromEntity(entity);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return entityString;

    }

    public static String executePut(String param, String urlPath) {
        httpClient = getHttpClient();
        localContext = getLocalContext();
//        CheckConnection cc = new CheckConnection(c);
//        isConnect = cc.isConnectingToInternet();
        HttpEntity entity;
        String entityString = "";
        HttpPut httpPut = new HttpPut(ServerConnection.getUrl() + urlPath);

        try {

            StringEntity stringEntity = new StringEntity(param);
            stringEntity.setContentType("application/json");
            httpPut.setEntity(stringEntity);

            HttpResponse response = httpClient.execute(httpPut, localContext);
            entity = response.getEntity();
            entityString = getASCIIContentFromEntity(entity);

        } catch (HttpHostConnectException hhce) {
            hhce.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return entityString;

    }

}

