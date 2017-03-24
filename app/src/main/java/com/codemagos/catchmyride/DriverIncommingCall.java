package com.codemagos.catchmyride;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codemagos.catchmyride.Webservice.ImageParse;
import com.codemagos.catchmyride.Webservice.WebService;

import org.json.JSONException;
import org.json.JSONObject;

public class DriverIncommingCall extends AppCompatActivity {
ImageView img_avatar;
    ImageButton btn_reject,btn_accept;
    TextView txt_name,txt_destination;
    Intent incommingIntent;
    String id,name,destination,avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_incomming_call);
        img_avatar = (ImageView) findViewById(R.id.img_avatar);
        btn_reject = (ImageButton) findViewById(R.id.btn_reject);
        btn_accept  =(ImageButton) findViewById(R.id.btn_accept);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_destination =(TextView) findViewById(R.id.txt_destination);
        incommingIntent = getIntent();
        name  = incommingIntent.getStringExtra("name");
        destination = incommingIntent.getStringExtra("destination");
        id = incommingIntent.getStringExtra("id");
        txt_name.setText(name);
        txt_destination.setText(destination);
        BackTask backTask = new BackTask();
        backTask.execute();
    }


    protected class BackTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            return WebService.getUserAvatar(id);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("avatar",result);
            try {
                JSONObject avatarObject = new JSONObject(result);
                String avatar = avatarObject.getString("avatar");
                if(!avatar.equals("")){
                    img_avatar.setImageBitmap(ImageParse.base64ToImage(avatar));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
