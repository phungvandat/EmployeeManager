package com.example.phungvandat.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.phungvandat.employeemanager.ChinhSuaHopDongActivity;
import com.example.phungvandat.employeemanager.HienThiThongTinActivity;
import com.example.phungvandat.employeemanager.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.phungvandat.model.HopDong;
import com.example.phungvandat.model.NhanVien;

/**
 * Created by PHUNGVANDAT on 3/19/2018.
 */
public class HopDongAdapter extends ArrayAdapter<HopDong> {
    Activity context;
    int resource;
    List<HopDong> objects;
    List<NhanVien> objects2;
    ArrayList<HopDong> dsHopDong;
    ArrayList<NhanVien> dsNhanVien;
    int quyenTruyCap=1;


    public HopDongAdapter(Activity context, int resource, List<HopDong> objects, List<NhanVien> objects2, int quyenTruyCap) {
        super(context, resource, objects);
        this.context= context;
        this.resource=resource;
        this.objects= objects;
        this.objects2=objects2;
        dsHopDong= new ArrayList<>();
        dsHopDong.addAll(objects);
        dsNhanVien= new ArrayList<>();
        dsNhanVien.addAll(objects2);
        this.quyenTruyCap=quyenTruyCap;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=this.context.getLayoutInflater();
        View row=inflater.inflate(this.resource,null);

        final TextView txtMaNhanVien, txtNgayVaoLam, txtNgayThoiViec;
        Button btnChinhSua;

        txtMaNhanVien= (TextView) row.findViewById(R.id.txtMaNhanVien);
        txtNgayVaoLam= (TextView) row.findViewById(R.id.txtNgayVaoLam);
        txtNgayThoiViec= (TextView) row.findViewById(R.id.txtNgayThoiViec);

        btnChinhSua= (Button) row.findViewById(R.id.btnChinhSua);

        final HopDong hopDong= this.objects.get(position);
        txtMaNhanVien.setText(hopDong.getMaNhanVien());
        txtNgayVaoLam.setText(hopDong.getNgayVaoLam());
        txtNgayThoiViec.setText(hopDong.getNgayThoiViec());
        if (quyenTruyCap!=1)
            btnChinhSua.setVisibility(View.GONE);

        btnChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quyenTruyCap==1) {
                    Intent i = new Intent(getContext(), ChinhSuaHopDongActivity.class);
                    i.putExtra("KIEU_HOP_DONG", hopDong);
                    getContext().startActivity(i);
                }

            }
        });

        final NhanVien nv= this.objects2.get(position);
        txtMaNhanVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getContext(),HienThiThongTinActivity.class);
                i.putExtra("KIEU_NHAN_VIEN",nv);
                getContext().startActivity(i);


            }
        });
        return row;
    }

    public void filter(String noiDungAuto){
        noiDungAuto= noiDungAuto.toLowerCase(Locale.getDefault());
        objects.clear();
        if (noiDungAuto.length()==0) {
            objects.addAll(dsHopDong);
        }
        else {
            for (HopDong hd: dsHopDong){
                if (hd.getMaNhanVien().toLowerCase(Locale.getDefault()).contains(noiDungAuto))
                {
                    objects.add(hd);
                }
            }
        }
        notifyDataSetChanged();
    }
}
