package com.codemagos.catchmyride;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codemagos.catchmyride.Spstore.SharedPreferenceStore;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    TextView text_new_account, text_forget;
    EditText input_email;
    EditText input_passord;
    Button button_login;
    LinearLayout btn_driver, btn_passenger;
    String email, password;
    SharedPreferenceStore spStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Intent i = new Intent(this, RegistrationService.class);
        //startService(i);

        init();
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = input_email.getText().toString();
                password = input_passord.getText().toString();
                BackTask bb = new BackTask();
                bb.execute();
            }
        });
        text_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Clicked Forget", Toast.LENGTH_LONG).show();
            }
        });
        text_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Clicked New Account", Toast.LENGTH_LONG).show();
                Dialog dialog = new Dialog(LoginActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.dialog_new_account);
                dialog.show();
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                dialog.getWindow().setLayout(metrics.widthPixels, metrics.heightPixels);
                btn_driver = (LinearLayout) dialog.findViewById(R.id.btn_driver);
                btn_passenger = (LinearLayout) dialog.findViewById(R.id.btn_passenger);



                btn_driver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // startActivity(new Intent(getApplicationContext(), DriverRegisterActivity.class));
                    }
                });

                btn_passenger.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // startActivity(new Intent(getApplicationContext(), PassengerRegisterActivity.class));
                    }
                });
            }
        });
    }

    public void init() {
        spStore = new SharedPreferenceStore(getApplicationContext());
        text_new_account = (TextView) findViewById(R.id.text_new_account);
        text_forget = (TextView) findViewById(R.id.text_forget);
        input_email = (EditText) findViewById(R.id.input_email);
        input_passord = (EditText) findViewById(R.id.input_password);
        button_login = (Button) findViewById(R.id.button_login);

        if (!spStore.getLoginID().equals("")) {
            Intent i;

            if(spStore.getType().equals("driver")){
                //i = new Intent(getApplicationContext(), DriverHomeActivity.class);
            }else{
               // i = new Intent(getApplicationContext(), PassengerHomeActivity.class);
            }
          //  startActivity(i);
            finish();
        }
    }


    protected class BackTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(), "Fetching data....", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
          //  return WebService.login(email, password);
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            Log.w("Server ->", response);
            try {
                JSONObject json = new JSONObject(response);
                if (json.getString("status").equals("success")) {
                    String user_id = json.getString("user_id");
                    String user_name = json.getString("user_name");
                    String user_type = json.getString("user_type");
                    spStore.setLogin(user_id, user_name,user_type);

                    Toast.makeText(getApplicationContext(), "Welcome !", Toast.LENGTH_LONG).show();

                    if (user_type.equals("driver")) {
                     //   startActivity(new Intent(getApplicationContext(), DriverHomeActivity.class));
                    } else {
                       // startActivity(new Intent(getApplicationContext(), PassengerHomeActivity.class));
                    }

                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Username or Password Error\nPlease Try Again", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
