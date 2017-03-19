package com.codemagos.catchmyride.Webservice;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Sree on 01-Oct-15.
 */
public class WebService {

/*    static String reg_url = "http://codemagos.in/Mybook/index.php";*/
    static String SITE_URL = "http://10.0.2.2/project/catchmyseat_server/";

    public static String postData(String action_URL, String data) {
        String responce = "";
        URL url = null;
        Log.w("-->", "in web service");
        try {
            Log.w("-->", "in web service try");
            url = new URL(SITE_URL + action_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                responce += line;

            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return responce;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.w("-->", "in web service first catch");
        } catch (ProtocolException e) {
            Log.w("-->", "in web service second catch");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responce;
    }

    public static String getData(String action_URL) {
        String responce = "";
        URL url = null;
        Log.w("-->", "in web service");
        try {
            Log.w("-->", "in web service try");
            url = new URL(SITE_URL + action_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoOutput(true);
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                responce += line;

            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return responce;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.w("-->", "in web service first catch");
        } catch (ProtocolException e) {
            Log.w("-->", "in web service second catch");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responce;
    }

    public static String userRegistration(String name,String email,String mobile,String password,String aadhar,String device_id, String fcm_id) {
        Log.w("-->", "in uer login web service method");
        String data = null;
        try {
            data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                    URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                    URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(mobile, "UTF-8") + "&" +
                    URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                    URLEncoder.encode("aadhar", "UTF-8") + "=" + URLEncoder.encode(aadhar, "UTF-8") + "&" +
                    URLEncoder.encode("device_id", "UTF-8") + "=" + URLEncoder.encode(device_id, "UTF-8") + "&" +
                    URLEncoder.encode("fcm_id", "UTF-8") + "=" + URLEncoder.encode(fcm_id, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return postData("webservice.php", data);

    }
    public static String userLogin(String email, String password) {
        Log.w("-->", "in user login web service method");
        String data = null;
        try {
            data = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode("login_check", "UTF-8") + "&" +
                    URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8") + "&" +
                    URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return postData("webservice.php", data);

    }

    public static String getNews() {
        String url = "webservice.php";
        String responce = getData(url);
        return responce;
    }



}
