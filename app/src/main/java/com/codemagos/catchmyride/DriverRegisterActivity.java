package com.codemagos.catchmyride;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.codemagos.catchmyride.Misc.LoadingDialog;
import com.codemagos.catchmyride.Spstore.SharedPreferencesStore;
import com.codemagos.catchmyride.Utils.Settings;
import com.codemagos.catchmyride.Webservice.ImageParse;
import com.codemagos.catchmyride.Webservice.WebService;

import org.json.JSONException;
import org.json.JSONObject;

public class DriverRegisterActivity extends AppCompatActivity {
    EditText input_name, input_mobile, input_password, input_re_password, input_vehicle_no, input_vehicle_name;
    Spinner spinner_vehicle_type;
    SharedPreferencesStore spStore;
    ImageView btn_avatar, btn_licence, btn_vehicle_image, btn_rc_book;
    Button btn_save;
    final static int AVATAR_CAPTURE_INTENT = 100;
    final static int LICENCE_CAPTURE_INTENT = 101;
    final static int VEHICLE_CAPTURE_INTENT = 102;
    final static int RCBOOK_INTENT = 103;

    String name, mobile, password, re_password, vehicle_no, vehicle_name, vehicle_type, avatar = "", rc_book = "", licence = "", vehicle_image = "";
    String vehicle_types[] = {"Car", "Bus", "Auto Rickshaw"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);
        spStore = new SharedPreferencesStore(getApplicationContext());
        input_name = (EditText) findViewById(R.id.input_name);
        input_mobile = (EditText) findViewById(R.id.input_mobile);
        input_password = (EditText) findViewById(R.id.input_password);
        input_re_password = (EditText) findViewById(R.id.input_re_password);
        input_vehicle_no = (EditText) findViewById(R.id.input_vehicle_no);
        input_vehicle_name = (EditText) findViewById(R.id.input_vehicle_name);
        spinner_vehicle_type = (Spinner) findViewById(R.id.spinner_vehicle_type);
        btn_avatar = (ImageView) findViewById(R.id.btn_avatar);
        btn_licence = (ImageView) findViewById(R.id.btn_licence);
        btn_vehicle_image = (ImageView) findViewById(R.id.btn_vehicle_image);
        btn_rc_book = (ImageView) findViewById(R.id.btn_rc_book);
        btn_save = (Button) findViewById(R.id.btn_save);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vehicle_types);
        spinner_vehicle_type.setAdapter(dataAdapter);
        spinner_vehicle_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vehicle_type = vehicle_types[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = input_name.getText().toString();
                mobile = input_mobile.getText().toString();
                password = input_password.getText().toString();
                re_password = input_re_password.getText().toString();
                vehicle_no = input_vehicle_no.getText().toString();
                vehicle_name = input_vehicle_name.getText().toString();
                BackTask backTask  =new BackTask();
                backTask.execute();
            }
        });


    }

    public void captureImage(View v) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        switch (v.getId()) {
            case R.id.btn_avatar:
                startActivityForResult(intent, AVATAR_CAPTURE_INTENT);
                break;
            case R.id.btn_rc_book:
                startActivityForResult(intent, RCBOOK_INTENT);
                break;
            case R.id.btn_licence:
                startActivityForResult(intent, LICENCE_CAPTURE_INTENT);
                break;
            case R.id.btn_vehicle_image:
                startActivityForResult(intent, VEHICLE_CAPTURE_INTENT);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle extras;
            Bitmap imageBitmap;
            switch (requestCode) {
                case AVATAR_CAPTURE_INTENT:
                    // result from image capture intent
                    Toast.makeText(getApplicationContext(), "Image Captured", Toast.LENGTH_LONG).show();
                    extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    btn_avatar.setImageBitmap(imageBitmap);
                    // parsing image bitmap image into base64 string
                    avatar = ImageParse.imageToBase64(imageBitmap);
                    break;
                case RCBOOK_INTENT:
                    // result from image capture intent
                    Toast.makeText(getApplicationContext(), "RCBOOK Captured", Toast.LENGTH_LONG).show();
                    extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    btn_rc_book.setImageBitmap(imageBitmap);
                    // parsing image bitmap image into base64 string
                    rc_book = ImageParse.imageToBase64(imageBitmap);
                    break;

                case LICENCE_CAPTURE_INTENT:
                    // result from image capture intent
                    Toast.makeText(getApplicationContext(), "LICENCE Captured", Toast.LENGTH_LONG).show();
                    extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    btn_licence.setImageBitmap(imageBitmap);
                    // parsing image bitmap image into base64 string
                    licence = ImageParse.imageToBase64(imageBitmap);
                    break;
                case VEHICLE_CAPTURE_INTENT:
                    // result from image capture intent
                    Toast.makeText(getApplicationContext(), "VEHICLE Captured", Toast.LENGTH_LONG).show();
                    extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");
                    btn_vehicle_image.setImageBitmap(imageBitmap);
                    // parsing image bitmap image into base64 string
                    vehicle_image = ImageParse.imageToBase64(imageBitmap);
                    break;
            }
        }
    }


    private class BackTask extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Connecting to Server...", Toast.LENGTH_LONG).show();
            LoadingDialog.show(DriverRegisterActivity.this);
        }
        @Override
        protected String doInBackground(String... params) {
            return WebService.driverRegistration(name,mobile,password,avatar, Settings.getDeviceID(getApplicationContext()),Settings.getFCMToken(),licence,rc_book,vehicle_no,vehicle_type,vehicle_name,vehicle_image);
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
                    spStore.setLogData(data.getString("id"), data.getString("name"),"driver");
                    Intent intent = new Intent(getApplicationContext(),DriverHomeActivity.class);
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
