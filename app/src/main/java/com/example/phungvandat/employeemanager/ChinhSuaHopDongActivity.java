package com.example.phungvandat.employeemanager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.phungvandat.model.HopDong;

public class ChinhSuaHopDongActivity extends Activity {

    Spinner spNgay;
    ArrayList<String> dsNgay;
    ArrayAdapter<String> ngayAdapter;
    int viTriSpNgay = 0;

    Spinner spThang;
    ArrayList<String> dsThang;
    ArrayAdapter<String> thangAdapter;
    int viTriSpThang = 0;

    Spinner spNam;
    ArrayList<String> dsNam;
    ArrayAdapter<String> namAdapter;
    int viTriSpNam = 0;

    Button btnLuuLai;

    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinh_sua_hop_dong);
        xuLySaoChepCSDLTuAssetsVaoHeThongMobile();
        addControls();
        addEvents();
    }

    private void xuLySaoChepCSDLTuAssetsVaoHeThongMobile() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAcsset();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();

            }
        }

    }

    private void CopyDataBaseFromAcsset() {
        try {

            InputStream myInput = getAssets().open(DATABASE_NAME);
            String outFileName = layDuongDanLuuTru();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);

            if (!f.exists()) {
                f.mkdir();
            }
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[2048];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();

        } catch (Exception ex) {
            Log.e("Loi sao chep", ex.toString());

        }
    }

    private String layDuongDanLuuTru() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    private void addEvents() {
        spNgay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpNgay = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spThang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpThang = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spNam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpNam = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnLuuLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyLuuLai();
            }
        });
    }

    private void xuLyLuuLai() {

        Intent i = getIntent();
        HopDong hopDong = (HopDong) i.getSerializableExtra("KIEU_HOP_DONG");

        int ngayNhap, thangNhap, namNhap;
        ngayNhap = Integer.parseInt(dsNgay.get(viTriSpNgay));
        thangNhap = Integer.parseInt(dsThang.get(viTriSpThang));
        namNhap = Integer.parseInt(dsNam.get(viTriSpNam));


        int ngay, thang, nam;
        ngay = thang = nam = 0;
        String maNhanVien = "0";
        try {
            maNhanVien = hopDong.getMaNhanVien();
            String ngayVaoLam = hopDong.getNgayVaoLam();
            String ngayVao, thangVao, namVao;
            ngayVao = "" + ngayVaoLam.charAt(0);
            thangVao = "";
            namVao = "";
            Pattern pattern = Pattern.compile("\\d*");
            Matcher matcher = pattern.matcher(ngayVaoLam.charAt(1) + "");
            if (matcher.matches()) {
                ngayVao += ngayVaoLam.charAt(1);
                thangVao += ngayVaoLam.charAt(3);
                matcher = pattern.matcher(ngayVaoLam.charAt(4) + "");
                if (matcher.matches())
                    thangVao += ngayVaoLam.charAt(4);

            } else {
                thangVao += ngayVaoLam.charAt(2);
                matcher = pattern.matcher(ngayVaoLam.charAt(3) + "");
                if (matcher.matches())
                    thangVao += ngayVaoLam.charAt(3);

            }
            int lengthVao = ngayVaoLam.length();
            namVao += ngayVaoLam.charAt(lengthVao - 4);
            namVao += ngayVaoLam.charAt(lengthVao - 3);
            namVao += ngayVaoLam.charAt(lengthVao - 2);
            namVao += ngayVaoLam.charAt(lengthVao - 1);

            try {
                ngay = Integer.parseInt(ngayVao);
                thang = Integer.parseInt(thangVao);
                nam = Integer.parseInt(namVao);
            } catch (Exception e) {
            }
        } catch (Exception ex) {

        }

        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor;
        try {
            cursor = database.rawQuery("select * from TinhCong where MSNV_TC=" + maNhanVien, null);

            int ngayLamLast, thangLamLast, namLamLast;
            ngayLamLast = thangLamLast = namLamLast = 0;
            if (namNhap < nam) {
                Toast.makeText(this, "nhân viên " + maNhanVien + " vào làm " +
                        ngay + "/" + thang + "/" + nam, Toast.LENGTH_SHORT).show();

            } else {
                if (thangNhap < thang && namNhap == nam) {
                    Toast.makeText(this, "nhân viên " + maNhanVien + " vào làm " +
                            ngay + "/" + thang + "/" + nam, Toast.LENGTH_SHORT).show();
                } else {
                    if (ngayNhap < ngay) {
                        Toast.makeText(this, "nhân viên " + maNhanVien + " vào làm " +
                                ngay + "/" + thang + "/" + nam, Toast.LENGTH_SHORT).show();
                    } else {

                        if (cursor.moveToLast()) {

                            ngayLamLast = cursor.getInt(3);
                            thangLamLast = cursor.getInt(2);
                            namLamLast = cursor.getInt(1);

                            if (namNhap < namLamLast) {
                                Toast.makeText(this, "Đã tồn tại ngày làm công nhân viên " + maNhanVien + " vào " +
                                        ngayLamLast + "/" + thangLamLast + "/" + namLamLast, Toast.LENGTH_SHORT).show();
                            } else {
                                if (thangNhap < thangLamLast && namNhap == namLamLast) {
                                    Toast.makeText(this, "Đã tồn tại ngày làm công nhân viên " + maNhanVien + " vào " +
                                            ngayLamLast + "/" + thangLamLast + "/" + namLamLast, Toast.LENGTH_SHORT).show();
                                } else {
                                    if (ngayNhap < ngayLamLast && ngayNhap == ngayLamLast) {
                                        Toast.makeText(this, "Đã tồn tại ngày làm công nhân viên " + maNhanVien + " vào " +
                                                ngayLamLast + "/" + thangLamLast + "/" + namLamLast, Toast.LENGTH_SHORT).show();
                                    } else {
                                        ContentValues values = new ContentValues();
                                        values.put("NgayThoiViec", String.valueOf(ngayNhap) + "/" +
                                                String.valueOf(thangNhap) + "/" + String.valueOf(namNhap));
                                        String[] dieuKien = {maNhanVien};
                                        int ret = database.update("HopDongViecLam", values, "MSNV_HD=?", dieuKien);
                                        if (ret == 0) {
                                            Toast.makeText(this, "Update không thành công", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(this, "Update thành công", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }

                                    }
                                }
                            }
                        } else {
                            ContentValues values = new ContentValues();
                            values.put("NgayThoiViec", String.valueOf(ngayNhap) + "/" +
                                    String.valueOf(thangNhap) + "/" + String.valueOf(namNhap));
                            String[] dieuKien = {maNhanVien};
                            int ret = database.update("HopDongViecLam", values, "MSNV_HD=?", dieuKien);
                            if (ret == 0) {
                                Toast.makeText(this, "Update không thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Update thành công", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
            cursor.close();
        } catch (Exception ex) {

        }
    }

    private void addControls() {
        spNgay = (Spinner) findViewById(R.id.spNgay);
        addDSNgay();
        ngayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dsNgay);
        ngayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNgay.setAdapter(ngayAdapter);

        spThang = (Spinner) findViewById(R.id.spThang);
        addDSThang();
        thangAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dsThang);
        thangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spThang.setAdapter(thangAdapter);

        spNam = (Spinner) findViewById(R.id.spNam);
        addDSNam();
        namAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dsNam);
        namAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNam.setAdapter(namAdapter);

        btnLuuLai = (Button) findViewById(R.id.btnLuuLai);

    }

    private void addDSNam() {
        dsNam = new ArrayList<>();
        dsNam.add("2017");
        dsNam.add("2018");
    }

    private void addDSThang() {
        dsThang = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            dsThang.add(String.valueOf(i));
        }
    }

    private void addDSNgay() {
        dsNgay = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            dsNgay.add(String.valueOf(i));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            database.close();
        }
        catch (Exception ex){

        }
    }
}
