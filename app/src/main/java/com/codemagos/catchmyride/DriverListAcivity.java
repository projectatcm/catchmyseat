package com.codemagos.catchmyride;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
    String latitude,longitude,destination;
    ListView list_drivers;
    SharedPreferencesStore spStore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_list_acivity);
        spStore = new SharedPreferencesStore(getApplicationContext());
        locationIntent = getIntent();
        latitude = locationIntent.getStringExtra("lat");
        longitude = locationIntent.getStringExtra("lng");
        destination = "";
        progressDialog = new ProgressDialog(DriverListAcivity.this);
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
            final ArrayList ids = new ArrayList();

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
                list_drivers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        LayoutInflater li = LayoutInflater.from(getApplicationContext());
                        final View dialogView = li.inflate(R.layout.dialog_custom_input, null);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DriverListAcivity.this);
                        // set title
                        alertDialogBuilder.setTitle("Custom Dialog");
                        // set custom dialog icon
                        // set custom_dialog.xml to alertdialog builder
                        alertDialogBuilder.setView(dialogView);
                        final EditText txt_destination = (EditText) dialogView
                                .findViewById(R.id.txt_destination);
                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                destination = txt_destination.getText().toString();
                                                if(!destination.equals("")) {
                                                    CallBackTask callBackTask = new CallBackTask();
                                                    callBackTask.execute(spStore.getID(), ids.get(position).toString(), destination);
                                                    Toast.makeText(getApplicationContext(), ids.get(position).toString(), Toast.LENGTH_SHORT).show();
                                                }
                                                // get user input and set it to etOutput
                                                // edit text

                                            }
                                        })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                dialog.cancel();
                                            }
                                        });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        // show it
                        alertDialog.show();

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


    protected class CallBackTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Calling Driver");
        }

        @Override
        protected String doInBackground(String... params) {
            return WebService.callDriver(params[0],params[1], params[2],latitude,longitude);

        }

        @Override
        protected void onPostExecute(String response) {
            Log.e("login",response);
            progressDialog.hide();
            LoadingDialog.hide();
            try {
                JSONObject responseObject = new JSONObject(response);
                if (responseObject.getString("status").equals("success")) {
                    Toast.makeText(getApplicationContext(), responseObject.getString("message"), Toast.LENGTH_SHORT).show();

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




}
