package com.codemagos.catchmyride.Utils;

import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;


/**
 * Created by prasanth on 11/3/17.
 */

public class Settings {

    public static String getFCMToken(){
        String FCM_ID = FirebaseInstanceId.getInstance().getToken();
        return FCM_ID;
    }
    public static String getDeviceID(Context context){
        String device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        return device_id;
    }
}
