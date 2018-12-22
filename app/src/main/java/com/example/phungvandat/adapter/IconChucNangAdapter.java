package com.example.phungvandat.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phungvandat.employeemanager.R;

import java.util.ArrayList;
import java.util.List;

import com.example.phungvandat.model.IconChucNang;

/**
 * Created by PHUNGVANDAT on 4/11/2018.
 */
public class IconChucNangAdapter extends ArrayAdapter<IconChucNang> {

    Activity context;
    int resource;
    List<IconChucNang> objects;


    public IconChucNangAdapter(Activity context, int resource, ArrayList<IconChucNang> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        View row=inflater.inflate(this.resource,null);
        ImageView imgIcon= (ImageView) row.findViewById(R.id.imgIcon);
        TextView txtChucNang= (TextView) row.findViewById(R.id.txtChucNang);

        final IconChucNang iconChucNang=this.objects.get(position);
        imgIcon.setImageResource(iconChucNang.getImgIcon());
        txtChucNang.setText(iconChucNang.getChucNang());

        return row;

    }
}
