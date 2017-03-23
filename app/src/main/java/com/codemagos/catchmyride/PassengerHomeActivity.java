package com.codemagos.catchmyride;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.codemagos.catchmyride.Location.AndroidLocationServices;
import com.codemagos.catchmyride.Misc.LoadingDialog;
import com.codemagos.catchmyride.Spstore.SharedPreferencesStore;
import com.codemagos.catchmyride.Webservice.WebService;

public class PassengerHomeActivity extends AppCompatActivity {
    SharedPreferencesStore spStore;
    Location location;
    Button btn_search;
    Double latitude;
    Double longitude;
    WebView webView;
    boolean locationFinderReady = false;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_home);
        spStore = new SharedPreferencesStore(getApplicationContext());
        btn_search  = (Button) findViewById(R.id.btn_search);
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                5 * 60 * 1000, 20, listener);
        LoadingDialog.show(PassengerHomeActivity.this);

        webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // webView.loadUrl(WebService.SITE_URL+"map.php?lat="+10+"&lng="+7);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(latitude == null){
                    Toast.makeText(getApplicationContext(),"GPS is not Activated Yet...Searching your Location...",Toast.LENGTH_LONG).show();
                }else{
                    Intent locationIntent = new Intent(getApplicationContext(),DriverListAcivity.class);
                    locationIntent.putExtra("lat",String.valueOf(latitude));
                    locationIntent.putExtra("lng",String.valueOf(longitude));
                    startActivity(locationIntent);
                }
            }
        });
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

            Log.e("Latitude", latitude + "");
            Log.e("Longitude", longitude + "");
            locationFinderReady = true;
            if (locationFinderReady) {
                LoadingDialog.hide();
            }
            webView.loadUrl(WebService.SITE_URL + "map.php?lat=" + latitude + "&lng=" + longitude);


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

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_inner_with_refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                spStore.clearLogData();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
            case R.id.action_refresh:
                spStore.clearLogData();
                startActivity(new Intent(getApplicationContext(), PassengerHomeActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
