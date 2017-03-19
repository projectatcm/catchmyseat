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

import com.codemagos.catchmyride.Spstore.SharedPreferencesStore;
import com.codemagos.catchmyride.Webservice.WebService;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    TextView text_new_account, text_forget;
    EditText input_email;
    EditText input_passord;
    Button button_login;
    LinearLayout btn_driver, btn_passenger;
    String email, password;
    SharedPreferencesStore spStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        spStore = new SharedPreferencesStore(getApplicationContext());
        text_new_account = (TextView) findViewById(R.id.text_new_account);
        text_forget = (TextView) findViewById(R.id.text_forget);
        input_email = (EditText) findViewById(R.id.input_email);
        input_passord = (EditText) findViewById(R.id.input_password);
        button_login = (Button) findViewById(R.id.button_login);

        if (!spStore.getID().equals("")) {
            Intent i;

            if(spStore.getType().equals("driver")){
                i = new Intent(getApplicationContext(), DriverHomeActivity.class);
            }else{
                 i = new Intent(getApplicationContext(), PassengerHomeActivity.class);
            }
            //  startActivity(i);
            finish();
        }

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
                        startActivity(new Intent(getApplicationContext(), DriverRegisterActivity.class));
                    }
                });

                btn_passenger.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), PassengerRegisterActivity.class));
                    }
                });
            }
        });
    }


    protected class BackTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            return WebService.userLogin(email,password);

        }

        @Override
        protected void onPostExecute(String response) {
            Log.e("login",response);

        }
    }


}
