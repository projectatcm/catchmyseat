package com.codemagos.catchmyride.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codemagos.catchmyride.R;
import com.codemagos.catchmyride.Webservice.ImageParse;

import java.util.ArrayList;


/**
 * Created by Sree on 26-Mar-16.
 */
public class DriverListAdapter extends ArrayAdapter<String> {
    final Activity activity;
    final ArrayList names;
    final ArrayList type;
    final ArrayList distance;
    final ArrayList photo;


    public DriverListAdapter(Activity activity, ArrayList names,ArrayList type,ArrayList distance,ArrayList photo) {
        super(activity.getApplicationContext(), R.layout.listview_driver_item,names);
        this.activity = activity;
        this.names = names;
        this.type = type;
        this.distance = distance;
        this.photo = photo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rawView = inflater.inflate(R.layout.listview_driver_item, null);
        TextView txt_name = (TextView) rawView.findViewById(R.id.txt_driver_name);
        TextView txt_distance = (TextView) rawView.findViewById(R.id.txt_distance);
        TextView txt_type = (TextView) rawView.findViewById(R.id.txt_type);
        ImageView img_photo = (ImageView) rawView.findViewById(R.id.img_driver_avatar);
        txt_name.setText(names.get(position).toString());
        txt_type.setText(type.get(position).toString());
        txt_distance.setText(distance.get(position).toString()+" away) ");
        try {
            img_photo.setImageBitmap(ImageParse.base64ToImage(photo.get(position).toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
           // img_photo.setImageBitmap(ImageParse.base64ToImage(photo[position]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rawView;
    }
}
