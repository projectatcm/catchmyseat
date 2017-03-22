package com.codemagos.catchmyride.Location;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.codemagos.catchmyride.Spstore.SharedPreferencesStore;
import com.codemagos.catchmyride.Webservice.WebService;


public class AndroidLocationServices extends Service {
    PowerManager.WakeLock wakeLock;
    private LocationManager locationManager;
    Double latitude, longitude;
    SharedPreferencesStore spStore;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        PowerManager pm = (PowerManager) getSystemService(this.POWER_SERVICE);
        spStore = new SharedPreferencesStore(getApplication());
        // wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DoNotSleep");

        // Toast.makeText(getApplicationContext(), "Service Created",
        // Toast.LENGTH_SHORT).show();

        Log.e("CatchmyRide", "Service Created");
    }

    @Override
    @Deprecated
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);

        Log.e("Google", "Service Started");

        locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 5, listener);
        }
    }

    private LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub

            Log.e("Google", "Location Changed");

            if (location == null)
                return;


            latitude = location.getLatitude();
            longitude = location.getLongitude();

            Log.e("Latitude",latitude+"");
            Log.e("Longitude",longitude+"");

            BackTast bb = new BackTast();
            bb.execute();


        }
        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

//        wakeLock.release();

    }

  /*  public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context
       *//*         .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }*//*
        return false;
    }*/

    protected  class BackTast extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {
            return WebService.updateLocation(spStore.getID(),latitude,longitude);
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("location update",s);
            Toast.makeText(getApplication(),"Location Updated", Toast.LENGTH_SHORT).show();
            super.onPostExecute(s);
        }
    }


    @Override
    public boolean onUnbind(Intent intent) {
     Log.e("CatchMyride","Service Stoped");
        return super.onUnbind(intent);

    }

}
