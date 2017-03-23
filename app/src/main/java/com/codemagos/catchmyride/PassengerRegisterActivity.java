package com.codemagos.catchmyride;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.codemagos.catchmyride.Misc.LoadingDialog;
import com.codemagos.catchmyride.Spstore.SharedPreferencesStore;
import com.codemagos.catchmyride.Utils.Settings;
import com.codemagos.catchmyride.Webservice.ImageParse;
import com.codemagos.catchmyride.Webservice.WebService;

import org.json.JSONException;
import org.json.JSONObject;

public class PassengerRegisterActivity extends AppCompatActivity {
    EditText input_name, input_mobile, input_password, input_re_password;
    ImageButton btn_avatar;
    Button btn_save;
    String device_id,fcm_id;
    SharedPreferencesStore spStore;
    String name, mobile, password, re_password,avatar = "";
    final static int AVATAR_CHOOSE_INTENT = 100;
    final static int AVATAR_CAPTURE_INTENT = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_register);
        btn_avatar = (ImageButton) findViewById(R.id.btn_avatar);
        input_name = (EditText) findViewById(R.id.input_name);
        input_mobile = (EditText) findViewById(R.id.input_mobile);
        input_password = (EditText) findViewById(R.id.input_password);
        input_re_password = (EditText) findViewById(R.id.input_re_password);
        btn_save = (Button) findViewById(R.id.btn_save);
        device_id  = Settings.getDeviceID(getApplicationContext());
        fcm_id = Settings.getFCMToken();
        spStore = new SharedPreferencesStore(getApplicationContext());
        btn_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, AVATAR_CAPTURE_INTENT);
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = input_name.getText().toString();
                mobile = input_mobile.getText().toString();
                password = input_password.getText().toString();
                re_password = input_re_password.getText().toString();

                if(name.equals("")){
                    input_name.setError("Required");
                }else if(mobile.equals("")){
                    input_name.setError("Required");
                }else if(password.equals("")){
                    input_password.setError("Required");
                }else if(re_password.equals("")){
                    input_re_password.setError("Required");
                }else if(!password.equals(re_password)){
                    input_password.setError("Passwords are not matching");
                    input_re_password.setError("Passwords are not matching");
                    input_password.setText("");
                    input_re_password.setText("");
                }else{
                    BackTask backTask = new BackTask();
                    backTask.execute();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AVATAR_CAPTURE_INTENT:
                    // result from image capture intent
                    Toast.makeText(getApplicationContext(), "Image Captured", Toast.LENGTH_LONG).show();
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    btn_avatar.setImageBitmap(imageBitmap);
                    // parsing image bitmap image into base64 string
                    avatar = ImageParse.imageToBase64(imageBitmap);
                    break;
            }
        }
    }


    private class BackTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Connecting to Server...", Toast.LENGTH_LONG).show();
            LoadingDialog.show(PassengerRegisterActivity.this);
        }
        @Override
        protected String doInBackground(String... params) {
            return WebService.passengerRegistration(name,mobile,password,avatar,device_id,fcm_id);
        }
        @Override
        protected void onPostExecute(String response) {
            Log.d("response",response);
            LoadingDialog.hide();
            try{
            JSONObject responseObject = new JSONObject(response);
            if (responseObject.getString("status").equals("success")) {
                Toast.makeText(getApplicationContext(), responseObject.getString("message"), Toast.LENGTH_SHORT).show();
                JSONObject data = new JSONObject(responseObject.getString("data"));
                spStore.setLogData(data.getString("id"), data.getString("name"),"passenger");
                Intent intent = new Intent(getApplicationContext(),NotVerifiedActivity.class);
                startActivity(intent);
                finish();
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
