package com.example.phungvandat.employeemanager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DanhSachNhanVienDaXoaActivity extends Activity {
    ListView lvNhanVienDaXoa;
    ArrayAdapter<String> adapterNhanVienDaXoa;
    ArrayList<String> dsNhanVienDaXoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_nhan_vien_da_xoa);
        addControls();
        addEvents();
    }

    private void addEvents() {
        lvNhanVienDaXoa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nhanVien=dsNhanVienDaXoa.get(position);
                String maNhanVien="";
                String tenNhanVien="";
                for(int i=0; i<nhanVien.length(); i++){
                    Pattern pattern = Pattern.compile("\\d*");
                    Matcher matcher = pattern.matcher(nhanVien.charAt(i) + "");
                    if(matcher.matches()){
                        maNhanVien+=nhanVien.charAt(i);
                    }
                    if(!matcher.matches() && nhanVien.charAt(i)!='-'){
                        tenNhanVien+=nhanVien.charAt(i);
                    }
                }
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DanhSachNhanVienDaXoaActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("MA_NHAN_VIEN", maNhanVien);
                editor.putString("TEN_NHAN_VIEN",tenNhanVien);
                editor.commit();
                Toast.makeText(DanhSachNhanVienDaXoaActivity.this,maNhanVien+"   "+tenNhanVien,Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void addControls() {
        lvNhanVienDaXoa= (ListView) findViewById(R.id.lvNhanVienDaXoa);
        addDSNhanVienDaXoa();
        adapterNhanVienDaXoa= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dsNhanVienDaXoa);
        adapterNhanVienDaXoa.notifyDataSetChanged();
        lvNhanVienDaXoa.setAdapter(adapterNhanVienDaXoa);

    }

    private void addDSNhanVienDaXoa() {
        dsNhanVienDaXoa= new ArrayList<>();
        dsNhanVienDaXoa=getIntent().getStringArrayListExtra("DANH_SACH_NHAN_VIEN_DA_XOA");

    }
}
