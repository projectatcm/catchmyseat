package com.codemagos.catchmyride;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.codemagos.catchmyride.Spstore.SharedPreferencesStore;

public class NotVerifiedActivity extends AppCompatActivity {
    SharedPreferencesStore spStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_verified);
        spStore = new SharedPreferencesStore(getApplicationContext());
    }

    public void logout(View v){
        spStore.clearLogData();
        finish();
    }



}
