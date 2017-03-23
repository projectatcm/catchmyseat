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
public class    WebService {

/*    static String reg_url = "http://codemagos.in/Mybook/index.php";*/
    public  static String SITE_URL = "http://192.168.1.110/catchmyseat_server/webservice/";

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

    public static String passengerRegistration(String name,String mobile,String password,String avatar,String device_id,String fcm_id) {
        Log.w("-->", "in uer login web service method");
        String data = null;
        try {
            data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                    URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(mobile, "UTF-8") + "&" +
                    URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                    URLEncoder.encode("avatar", "UTF-8") + "=" + URLEncoder.encode(avatar, "UTF-8") + "&" +
                    URLEncoder.encode("device_id", "UTF-8") + "=" + URLEncoder.encode(device_id, "UTF-8") + "&" +
                    URLEncoder.encode("fcm_id", "UTF-8") + "=" + URLEncoder.encode(fcm_id, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return postData("passenger_registration.php", data);

    }
    public static String driverRegistration(String name,String mobile,String password,String avatar,String device_id,String fcm_id,
                                            String licence,String rc_book,String vehicle_no,String vehicle_type,String vehicle_name,
                                            String vehicle_image){
        Log.w("-->", "in uer login web service method");
        String data = null;
        try {
            data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&" +
                    URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(mobile, "UTF-8") + "&" +
                    URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                    URLEncoder.encode("avatar", "UTF-8") + "=" + URLEncoder.encode(avatar, "UTF-8") + "&" +
                    URLEncoder.encode("device_id", "UTF-8") + "=" + URLEncoder.encode(device_id, "UTF-8") + "&" +
                    URLEncoder.encode("fcm_id", "UTF-8") + "=" + URLEncoder.encode(fcm_id, "UTF-8") + "&" +
                    URLEncoder.encode("licence", "UTF-8") + "=" + URLEncoder.encode(licence, "UTF-8") + "&" +
                    URLEncoder.encode("rc_book", "UTF-8") + "=" + URLEncoder.encode(rc_book, "UTF-8") + "&" +
                    URLEncoder.encode("vehicle_no", "UTF-8") + "=" + URLEncoder.encode(vehicle_no, "UTF-8") + "&" +
                    URLEncoder.encode("vehicle_type", "UTF-8") + "=" + URLEncoder.encode(vehicle_type, "UTF-8") + "&" +
                    URLEncoder.encode("vehicle_name", "UTF-8") + "=" + URLEncoder.encode(vehicle_name, "UTF-8") + "&" +
                    URLEncoder.encode("vehicle_image", "UTF-8") + "=" + URLEncoder.encode(vehicle_image, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return postData("driver_registration.php", data);

    }
    public static String userLogin(String mobile, String password,String fcm_token) {
        Log.w("-->", "in user login web service method");
        String data = null;
        try {
            data =  URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(mobile, "UTF-8") + "&" +
                    URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&" +
                    URLEncoder.encode("fcm_id", "UTF-8") + "=" + URLEncoder.encode(fcm_token, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return postData("login.php", data);

    }
    public static String updateLocation(String driver_id, Double latitude, Double longitude) {
        String data = "";
        try {
            data = URLEncoder.encode("action", "UTF-8") + "=" + URLEncoder.encode("update_location", "UTF-8") + "&" +
                    URLEncoder.encode("driver_id", "UTF-8") + "=" + URLEncoder.encode(driver_id, "UTF-8") + "&" +
                    URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(latitude), "UTF-8") + "&" +
                    URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(longitude), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return postData("update_driver_location.php", data);
    }
    public static String getDriverStatus(String driver_id) {
        String data = "";
        try {
            data = URLEncoder.encode("driver_id", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(driver_id), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return postData("get_driver_status.php", data);
    }
    public static String findDrivers(String latitude,String longitude) {
        String data = "";
        try {
            data =  URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8") + "&" +
                    URLEncoder.encode("longitude", "UTF-8") + "=" + URLEncoder.encode(longitude, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return postData("find_driver.php", data);
    }



}
