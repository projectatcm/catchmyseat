package com.codemagos.catchmyride;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codemagos.catchmyride.Misc.LoadingDialog;
import com.codemagos.catchmyride.Spstore.SharedPreferencesStore;
import com.codemagos.catchmyride.Webservice.WebService;

import org.json.JSONException;
import org.json.JSONObject;

public class NotVerifiedActivity extends AppCompatActivity {
    SharedPreferencesStore spStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_verified);
        spStore = new SharedPreferencesStore(getApplicationContext());
        BackTask backTask = new BackTask();
        backTask.execute();
        Toast.makeText(getApplicationContext(),"id "+spStore.getID(),
                Toast.LENGTH_LONG).show();
    }

    public void logout(View v) {
        spStore.clearLogData();
        finish();
    }

    public void refresh(View v) {
        BackTask backTask = new BackTask();
        backTask.execute();
    }


    protected class BackTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingDialog.show(NotVerifiedActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            return WebService.getDriverStatus(spStore.getID());

        }

        @Override
        protected void onPostExecute(String response) {
            Log.e("login", response);
            LoadingDialog.hide();
            try {
                JSONObject responseObject = new JSONObject(response);
                if (responseObject.getString("status").equals("success")) {
                    Toast.makeText(getApplicationContext(), responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                    spStore.isVerified(true);
                    startActivity(new Intent(getApplicationContext(), DriverHomeActivity.class));


                } else {
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
