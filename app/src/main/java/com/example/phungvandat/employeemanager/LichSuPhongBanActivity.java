package com.example.phungvandat.employeemanager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.phungvandat.model.NhanVien;

public class LichSuPhongBanActivity extends Activity {
    ListView lvLichSuPhongBan;
    ArrayList<String> dsLichSuPhongBan;
    ArrayAdapter<String> adapterLichSuPhongBan;
    TextView txtMaNhanVien;

    String maNhanVien="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_phong_ban);
        addControls();
        addEvents();
    }

    private void addEvents() {
    }

    private void addControls() {
        txtMaNhanVien= (TextView) findViewById(R.id.txtMaNhanVien);
        lvLichSuPhongBan= (ListView) findViewById(R.id.lvLichSuPhongBan);
        addDSLichSuPhongBan();
        adapterLichSuPhongBan= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dsLichSuPhongBan);
        adapterLichSuPhongBan.notifyDataSetChanged();
        lvLichSuPhongBan.setAdapter(adapterLichSuPhongBan);
    }

    private void addDSLichSuPhongBan() {
        try {
            dsLichSuPhongBan = new ArrayList<>();
            NhanVien nhanVien = (NhanVien) getIntent().getSerializableExtra("KIEU_NHAN_VIEN");
            txtMaNhanVien.setText(nhanVien.getMaNhanVien());
            String lichSuPhongBan = nhanVien.getLichSuNhanVien();
            String temp = "";
            for (int i = 0; i < lichSuPhongBan.length(); i++) {
                if (lichSuPhongBan.charAt(i) != '(' && lichSuPhongBan.charAt(i) != '|' && lichSuPhongBan.charAt(i) != ')') {
                    temp += String.valueOf(lichSuPhongBan.charAt(i));
                }
                if (lichSuPhongBan.charAt(i) == '|')
                    temp += " - ";
                if (lichSuPhongBan.charAt(i) == ')') {
                    dsLichSuPhongBan.add(temp);
                    temp = "";
                }
            }
        }
        catch (Exception ex){

        }
    }
}
