package com.codemagos.catchmyride;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.codemagos.catchmyride.Location.AndroidLocationServices;
import com.codemagos.catchmyride.Misc.LoadingDialog;
import com.codemagos.catchmyride.Spstore.SharedPreferencesStore;
import com.codemagos.catchmyride.Webservice.WebService;

import org.json.JSONException;
import org.json.JSONObject;

public class DriverHomeActivity extends AppCompatActivity {
ToggleButton toggle_ride;
    Intent trackerIntent;
    SharedPreferencesStore spStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);
        toggle_ride = (ToggleButton) findViewById(R.id.toggle_ride);

        spStore = new SharedPreferencesStore(getApplicationContext());
        trackerIntent = new Intent(getApplicationContext(), AndroidLocationServices.class);
        toggle_ride.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Turn on GPS Tracker
                    Toast.makeText(getApplicationContext(),"Starting ride",Toast.LENGTH_LONG).show();
                    startService(trackerIntent);
                } else {
                    // Turn off Gps tracker
                    Toast.makeText(getApplicationContext(),"Stoping ride",Toast.LENGTH_LONG).show();
                    stopService(trackerIntent);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.passenger_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                spStore.clearLogData();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!spStore.isVerified()){
            finish();
        }
        if(spStore.isHired()){
            toggle_ride.setChecked(false);
            spStore.isHired(false);
        }
    }
}
