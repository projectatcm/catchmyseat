package com.codemagos.catchmyride;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codemagos.catchmyride.Location.AndroidLocationServices;
import com.codemagos.catchmyride.Spstore.SharedPreferencesStore;
import com.codemagos.catchmyride.Webservice.ImageParse;
import com.codemagos.catchmyride.Webservice.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class DriverIncommingCall extends AppCompatActivity {
ImageView img_avatar;
    ImageButton btn_reject,btn_accept;
    TextView txt_name,txt_destination;
    Intent incommingIntent;
    String id,name,destination,avatar,latitude,longitude;
    MediaPlayer mp;
    SharedPreferencesStore spStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_incomming_call);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mp = MediaPlayer.create(getApplicationContext(), notification);
        spStore = new SharedPreferencesStore(getApplicationContext());
        img_avatar = (ImageView) findViewById(R.id.img_avatar);
        btn_reject = (ImageButton) findViewById(R.id.btn_reject);
        btn_accept  =(ImageButton) findViewById(R.id.btn_accept);
        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_destination =(TextView) findViewById(R.id.txt_destination);
        incommingIntent = getIntent();
        name  = incommingIntent.getStringExtra("name");
        destination = incommingIntent.getStringExtra("destination");
        id = incommingIntent.getStringExtra("id");
        latitude = incommingIntent.getStringExtra("latitude");
        longitude = incommingIntent.getStringExtra("longitude");
        id = incommingIntent.getStringExtra("id");
        txt_name.setText(name);
        txt_destination.setText(destination);

    mp.start();
        BackTask backTask = new BackTask();
        backTask.execute();
        final CallBackTask callBackTask = new CallBackTask();
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackTask.execute(spStore.getID(),id,"accepted");
           mp.stop();
                String map = "http://maps.google.co.in/maps?q=" + latitude+","+longitude;
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
               stopService(new Intent(getApplicationContext(), AndroidLocationServices.class));
                spStore.isHired(true);
                startActivity(i);
                finish();
            }
        });
        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackTask.execute(spStore.getID(),id,"rejected");
               mp.stop();
                finish();
            }
        });
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

    protected class CallBackTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            return WebService.callRespose(params[0],params[1],params[2]);
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            Log.e("response",response);


        }
    }
}
