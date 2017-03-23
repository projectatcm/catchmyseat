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

import com.codemagos.catchmyride.Misc.LoadingDialog;
import com.codemagos.catchmyride.Spstore.SharedPreferencesStore;
import com.codemagos.catchmyride.Utils.Settings;
import com.codemagos.catchmyride.Webservice.WebService;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    TextView text_new_account, text_forget;
    EditText input_mobile;
    EditText input_passord;
    Button button_login;
    Button btn_driver, btn_passenger;
    String mobile, password;
    SharedPreferencesStore spStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        spStore = new SharedPreferencesStore(getApplicationContext());
        text_new_account = (TextView) findViewById(R.id.text_new_account);
        text_forget = (TextView) findViewById(R.id.text_forget);
        input_mobile = (EditText) findViewById(R.id.input_mobile);
        input_passord = (EditText) findViewById(R.id.input_password);
        button_login = (Button) findViewById(R.id.button_login);

        if (!spStore.getID().equals("")) {
            Intent i;
            if(spStore.getType().equals("driver")){
                if(spStore.isVerified()) {
                    i = new Intent(getApplicationContext(), DriverHomeActivity.class);
                }else{
                    i = new Intent(getApplicationContext(), NotVerifiedActivity.class);
                }
            }else{
                 i = new Intent(getApplicationContext(), PassengerHomeActivity.class);
            }
          startActivity(i);
            finish();
        }

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = input_mobile.getText().toString();
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
                Dialog dialog = new Dialog(LoginActivity.this);
                // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setTitle("Choose Account Type");
                dialog.setContentView(R.layout.dialog_new_account);
                dialog.show();

                btn_driver = (Button) dialog.findViewById(R.id.btn_driver);
                btn_passenger = (Button) dialog.findViewById(R.id.btn_passenger);

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
        protected void onPreExecute() {
            super.onPreExecute();
            LoadingDialog.show(LoginActivity.this);
        }

        @Override
        protected String doInBackground(String... params) {
            return WebService.userLogin(mobile,password, Settings.getFCMToken());

        }

        @Override
        protected void onPostExecute(String response) {
            Log.e("login",response);
            LoadingDialog.hide();
            try {
                JSONObject responseObject = new JSONObject(response);
                if (responseObject.getString("status").equals("success")) {
                    Toast.makeText(getApplicationContext(), responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                    JSONObject data = new JSONObject(responseObject.getString("data"));
                    String type = data.getString("type");
                    Intent intent;
                    if(type.equals("driver")){
                        if(data.getString("status").equals("0")){
                            spStore.isVerified(false);
                            intent = new Intent(getApplicationContext(), NotVerifiedActivity.class);
                        }else {
                            intent = new Intent(getApplicationContext(), DriverHomeActivity.class);
                        }
                        }else{
                        intent = new Intent(getApplicationContext(), PassengerHomeActivity.class);
                    }
                    spStore.setLogData(data.getString("id"), data.getString("name"),type);
                    startActivity(intent);
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
