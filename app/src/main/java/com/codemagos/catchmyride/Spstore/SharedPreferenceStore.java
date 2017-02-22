package com.codemagos.catchmyride.Spstore;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceStore {
	public final static String SHARED_PREFS = "PreferenceStore";
	private Editor edit;
	private SharedPreferences settings;
	public SharedPreferenceStore(Context context){
		settings = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
	}
	public void setLogin(String id, String name, String type){
		edit = settings.edit();
		edit.putString("LOG_ID", id);
		edit.putString("LOG_NAME", name);
		edit.putString("LOG_TYPE", type);
		edit.commit();
	}
	public String getGCMRegID() {
		return settings.getString("REG_KEY", "");
	}
	public void setGCMRegID(String reg_key) {
		edit = settings.edit();
		edit.putString("REG_KEY", reg_key);
		edit.commit();
	}

	public String getLoginID(){
		return settings.getString("LOG_ID","");
	}

	public String getName(){
		return settings.getString("LOG_NAME","");
	}
	public String getType(){
		return settings.getString("LOG_TYPE","");
	}
	public void removeLoginStatus(){
		edit = settings.edit();
		edit.putString("LOG_ID", "");
		edit.putString("LOG_NAME", "");
		edit.commit();
	}




}
