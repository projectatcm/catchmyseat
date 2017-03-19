package com.codemagos.catchmyride;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class DriverRegisterActivity extends AppCompatActivity {
Button btn_license;
    ImageView img_avatar;
    EditText txt_name,txt_mobile;
    final int SELECT_AVATAR = 100,CROP_PHOTO = 101,SELECT_LICENCE = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        img_avatar = (ImageView) findViewById(R.id.img_avatar);
        btn_license = (Button) findViewById(R.id.btn_license);
        txt_name = (EditText) findViewById(R.id.txt_name);
        txt_mobile = (EditText) findViewById(R.id.txt_mobile);

       Dialog imageChooser = new Dialog(DriverRegisterActivity.this);
        imageChooser.setContentView(R.layout.dialog_image_choose);
        imageChooser.setTitle("Complete Action Using..");
        imageChooser.show();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                // back button clicked
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
