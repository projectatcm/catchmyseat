package com.codemagos.catchmyride.Misc;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;


import com.codemagos.catchmyride.R;

import java.util.Random;

/**
 * Created by Sree on 6/5/2016.
 */
public class LoadingDialog {
   static Dialog dialog;
    static GIFView gifView;
    public static void show(Activity context){
        dialog =new Dialog(context, R.style.myGifLoadingDilalog);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_loading);
        gifView = (GIFView) dialog.findViewById(R.id.imageView);

        gifView.loadGIFResource(context, context.getResources().getIdentifier("load_anim", "drawable", context.getPackageName()));
        WindowManager.LayoutParams a = dialog.getWindow().getAttributes();
        a.dimAmount = 0;
        dialog.getWindow().setAttributes(a);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void hide(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               dialog.hide();
            }
        },2000);
    }

}
