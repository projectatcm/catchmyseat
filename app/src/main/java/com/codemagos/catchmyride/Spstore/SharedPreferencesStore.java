package com.codemagos.catchmyride.Spstore;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by prasanth on 11/3/17.
 */

public class SharedPreferencesStore {
    SharedPreferences sharedPreferences;

    public SharedPreferencesStore(Context context) {
        sharedPreferences = context.getSharedPreferences("CMR_PREFERENCES", Context.MODE_PRIVATE);
    }

    public void setLogData(String id, String name, String type) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("id", id);
        editor.putString("name", name);
        editor.putString("type", type);
        editor.commit();
    }

    public void clearLogData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void isVerified(boolean isVerified){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("verified", isVerified);
        editor.commit();
    }
    public boolean isHired(){
        return sharedPreferences.getBoolean("hire",false);
    }
    public void isHired(boolean hire){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("hire", hire);
        editor.commit();
    }
    public boolean isVerified(){
        return sharedPreferences.getBoolean("verified",false);
    }
    public void setTempAvatar(String avatar){
        SharedPreferences.Editor editor = sharedPreferences.edit();
         editor.putString("avatar", avatar);
         editor.commit();
    }
public String getTempAvatar(){
    return sharedPreferences.getString("avatar","");
}
    public String getID() {
        return sharedPreferences.getString("id", "");
    }

    public String getName() {
        return sharedPreferences.getString("name", "");
    }

    public String getType() {
        return sharedPreferences.getString("type", "");
    }
}





