package com.codemagos.catchmyride;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codemagos.catchmyride.Adapters.DriverListAdapter;
import com.codemagos.catchmyride.Misc.LoadingDialog;
import com.codemagos.catchmyride.Spstore.SharedPreferencesStore;
import com.codemagos.catchmyride.Utils.Settings;
import com.codemagos.catchmyride.Webservice.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DriverListAcivity extends AppCompatActivity {
Intent locationIntent;
    String latitude,longitude;
    ListView list_drivers;
    SharedPreferencesStore spStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_list_acivity);
        spStore = new SharedPreferencesStore(getApplicationContext());
        locationIntent = getIntent();
        latitude = locationIntent.getStringExtra("lat");
        longitude = locationIntent.getStringExtra("lng");
        latitude = "10.243569384410671";
        longitude = "76.26266006787509";
        list_drivers = (ListView) findViewById(R.id.list_drivers);
        BackTask backTask = new BackTask();
        backTask.execute();
    }


    private class BackTask extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Connecting to Server...", Toast.LENGTH_LONG).show();
            LoadingDialog.show(DriverListAcivity.this);
        }
        @Override
        protected String doInBackground(String... params) {
            return WebService.findDrivers(latitude,longitude);
        }
        @Override
        protected void onPostExecute(String response) {
            Log.d("response",response);
            LoadingDialog.hide();
            ArrayList names = new ArrayList();
            ArrayList type = new ArrayList();
            ArrayList avatar = new ArrayList();
            ArrayList distance = new ArrayList();
            ArrayList ids = new ArrayList();

            try{
                JSONObject responseObject = new JSONObject(response);
                if (responseObject.getString("status").equals("success")) {
                    JSONArray driverArray = responseObject.getJSONArray("data");
                    for (int i=0;i<driverArray.length();i++){
                        JSONObject driver = driverArray.getJSONObject(i);
                        names.add(driver.getJSONObject("data").getString("name"));
                        type.add(driver.getJSONObject("data").getString("vehicle_type"));
                        avatar.add(driver.getJSONObject("data").getString("avatar"));
                        ids.add(driver.getString("driver_id"));
                        distance.add(driver.getString("distance"));
                    }
                    DriverListAdapter driverListAdapter = new DriverListAdapter(DriverListAcivity.this,names,type,distance,avatar);
                    list_drivers.setAdapter(driverListAdapter);
                    list_drivers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else{
                    // if the response if error
                    Toast.makeText(getApplicationContext(), responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("ERROR : ", response);
            }
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
                BackTask backTask = new BackTask();
                backTask.execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }




}
