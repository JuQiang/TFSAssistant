package com.example.juqiang_pc.tfsassistant.API;

import android.util.Base64;

import com.example.juqiang_pc.tfsassistant.Entity.Authentication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JUQIANG-PC on 2017/3/29.
 */

public class HttpHelper {
    public static String getHttpResponse(String urlString){
        HttpURLConnection urlConnection = null;
        String ret = "";
        final Authentication auth = Utils.getAuthentication();
        if(auth.user.equals(""))return ret;

        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        auth.user,
                        auth.password.toCharArray());
            }
        });
        try {
            URL url = new URL(urlString);
            /*byte[] buf = ("juqiang:Password02!").getBytes();
            String author = "Basic " + Base64.encode(buf,buf.length);*/

            urlConnection = (HttpURLConnection) url.openConnection();
            /*urlConnection.setRequestProperty("Authorization", author);*/

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            reader.close();
            in.close();

            ret = buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return ret;
    }

    public static List<String> getHttpResponseBatch(String input){
        HttpURLConnection urlConnection = null;
        List<String> ret = new ArrayList<String>();
        String[] urlList = input.split(";");

        for(int i=0;i<urlList.length;i++) {
            try {
                URL url = new URL("http://tfs.teld.cn:8080/tfs/Teld/c955f4f8-3b05-4afc-9969-3a54f7b70533/ad0bf755-9688-4d6a-95f9-4e20702a2972/_api/_widgets/queryresultcount?queryId="+urlList[i]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                reader.close();
                in.close();

                ret.add(buffer.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        return ret;
    }
}
