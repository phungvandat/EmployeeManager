package com.example.phungvandat.employeemanager;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NhapCongActivity extends Activity {

    ImageButton btnBack;

    EditText txtMaNhanVien;

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

    Spinner spTrangThai;
    ArrayList<String> dsTrangThai;
    ArrayAdapter<String> trangThaiAdapter;
    int viTriSpTrangThai = 0;

    Spinner spGioRa;
    ArrayList<String> dsGioRa;
    ArrayAdapter<String> gioRaAdapter;
    int viTriSpGioRa = 0;

    Spinner spPhutRa;
    ArrayList<String> dsPhutRa;
    ArrayAdapter<String> phutRaAdapter;
    int viTriSpPhutRa = 0;

    Spinner spGiayRa;
    ArrayList<String> dsGiayRa;
    ArrayAdapter<String> giayRaAdapter;
    int viTriSpGiayRa = 0;

    Spinner spGioVao;
    ArrayList<String> dsGioVao;
    ArrayAdapter<String> gioVaoAdapter;
    int viTriSpGioVao = 0;

    Spinner spPhutVao;
    ArrayList<String> dsPhutVao;
    ArrayAdapter<String> phutVaoAdapter;
    int viTriSpPhutVao = 0;

    Spinner spGiayVao;
    ArrayList<String> dsGiayVao;
    ArrayAdapter<String> giayVaoAdapter;
    int viTriSpGiayVao = 0;

    Button btnThemCong;

    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_cong);

        xuLySaoChepCSDLTuAssetsVaoHeThongMobile();
        addControls();
        addEvents();
    }


    private void addControls() {
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        txtMaNhanVien = (EditText) findViewById(R.id.txtMaNhanVien);

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

        spTrangThai = (Spinner) findViewById(R.id.spTrangThai);
        addDSTrangThai();
        trangThaiAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, dsTrangThai);
        trangThaiAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spTrangThai.setAdapter(trangThaiAdapter);

        spGioVao = (Spinner) findViewById(R.id.spGioVao);
        addDSGioVao();
        gioVaoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dsGioVao);
        gioVaoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGioVao.setAdapter(gioVaoAdapter);

        spPhutVao = (Spinner) findViewById(R.id.spPhutVao);
        addDSPhutVao();
        phutVaoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dsPhutVao);
        phutVaoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPhutVao.setAdapter(phutVaoAdapter);

        spGiayVao = (Spinner) findViewById(R.id.spGiayVao);
        addDSGiayVao();
        giayVaoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dsGiayVao);
        giayVaoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGiayVao.setAdapter(giayVaoAdapter);

        spGioRa = (Spinner) findViewById(R.id.spGioRa);
        addDSGioRa();
        gioRaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dsGioRa);
        gioRaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGioRa.setAdapter(gioRaAdapter);

        spPhutRa = (Spinner) findViewById(R.id.spPhutRa);
        addDSPhutRa();
        phutRaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dsPhutRa);
        phutRaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPhutRa.setAdapter(phutRaAdapter);

        spGiayRa = (Spinner) findViewById(R.id.spGiayRa);
        addDSGiayRa();
        giayRaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dsGiayRa);
        giayRaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGiayRa.setAdapter(giayRaAdapter);

        btnThemCong = (Button) findViewById(R.id.btnThemCong);


    }

    private void addDSGiayRa() {
        dsGiayRa = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            if (i < 10) {
                String num = "0" + String.valueOf(i);
                dsGiayRa.add(num);
            } else {
                dsGiayRa.add(String.valueOf(i));
            }
        }
    }

    private void addDSPhutRa() {
        dsPhutRa = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            if (i < 10) {
                String num = "0" + String.valueOf(i);
                dsPhutRa.add(num);
            } else {
                dsPhutRa.add(String.valueOf(i));
            }
        }
    }

    private void addDSGioRa() {
        dsGioRa = new ArrayList<>();
        for (int i = 17; i <= 23; i++) {
            dsGioRa.add(String.valueOf(i));
        }
    }

    private void addDSGiayVao() {
        dsGiayVao = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            if (i < 10) {
                String num = "0" + String.valueOf(i);
                dsGiayVao.add(num);
            } else {
                dsGiayVao.add(String.valueOf(i));
            }
        }
    }

    private void addDSPhutVao() {
        dsPhutVao = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            if (i < 10) {
                String num = "0" + String.valueOf(i);
                dsPhutVao.add(num);
            } else {
                dsPhutVao.add(String.valueOf(i));
            }
        }
    }

    private void addDSGioVao() {
        dsGioVao = new ArrayList<>();
        for (int i = 7; i <= 11; i++) {
            if (i < 10) {
                String num = "0" + String.valueOf(i);
                dsGioVao.add(num);
            } else {
                dsGioVao.add(String.valueOf(i));
            }
        }
    }

    private void addDSTrangThai() {
        dsTrangThai = new ArrayList<>();
        dsTrangThai.add("Làm việc");
        dsTrangThai.add("Nghỉ việc");
    }

    private void addDSNam() {
        dsNam = new ArrayList<>();
        dsNam.add("2016");
        dsNam.add("2017");
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

    private void addEvents() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnThemCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyThemCong();
            }
        });
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

        spTrangThai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpTrangThai = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spGioVao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpGioVao = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPhutVao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpPhutVao = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spGiayVao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpGiayVao = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spGioRa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpGioRa = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPhutRa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpPhutRa = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spGiayRa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpGiayRa = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void xuLyThemCong() {
        int maNhanVien = -1;
        int ngay = -1;
        int thang = -1;
        int nam = -1;
        int trangThai = -1;
        String gioVao = "";
        String gioRa = "";

        try {
            database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            Cursor ckt = database.rawQuery("select MSNV from NhanVien", null);
            int check = 0;
            while (ckt.moveToNext()) {
                int msnv_DB = ckt.getInt(0);
                if (Integer.parseInt(txtMaNhanVien.getText().toString()) == msnv_DB) {
                    check = 1;
                    break;
                }
            }
            if (check == 0) {
                Toast.makeText(this, "Không tồn tại nhân viên có mã số " + txtMaNhanVien.getText().toString(), Toast.LENGTH_SHORT).show();
                return;
            }
            ckt.close();
        } catch (Exception ex) {

        }

        try {
            maNhanVien = Integer.parseInt(txtMaNhanVien.getText().toString());
            ngay = Integer.parseInt(dsNgay.get(viTriSpNgay));

            thang = Integer.parseInt(dsThang.get(viTriSpThang));
            nam = Integer.parseInt(dsNam.get(viTriSpNam));
        } catch (Exception ex) {

        }

        if (dsTrangThai.get(viTriSpTrangThai).equals("Làm việc")) {
            trangThai = 1;
        } else {
            trangThai = 0;
        }
        gioVao = dsGioVao.get(viTriSpGioVao) + ":" + dsPhutVao.get(viTriSpPhutVao) + ":" + dsGioVao.get(viTriSpGioVao);
        gioRa = dsGiayRa.get(viTriSpGioRa) + ":" + dsPhutRa.get(viTriSpPhutRa) + ":" + dsGioRa.get(viTriSpGioRa);

        Cursor cursorTonTai;
        cursorTonTai = database.rawQuery("select MSNV_TC,Nam,Thang,Ngay from TinhCong" +
                " where MSNV_TC=" + String.valueOf(maNhanVien) +
                " and Nam=" + String.valueOf(nam) + " and Thang=" + String.valueOf(thang) +
                " and Ngay=" + String.valueOf(ngay), null);
        if (cursorTonTai.moveToFirst()) {
            Toast.makeText(this, "Đã tồn tại dữ liệu tính công nhân viên " + String.valueOf(maNhanVien)
                            + " vào " + String.valueOf(ngay) + "/" + String.valueOf(thang) + "/" + String.valueOf(nam),
                    Toast.LENGTH_SHORT).show();
            cursorTonTai.close();
        } else {
            Cursor cursor;
            try {
                cursor = database.rawQuery("select MSNV_HD,NgayVaoLam,NgayThoiViec" +
                        " from HopDongViecLam " +
                        "where  MSNV_HD=" + txtMaNhanVien.getText().toString(), null);
                String ngayVaoLam = "";
                String ngayVao, thangVao, namVao;
                ngayVao = "";
                thangVao = "";
                namVao = "";
                int numngayVao, numthangVao, numnamVao;
                numnamVao = numngayVao = numthangVao = 0;


                String ngayThoiViec = "";
                String ngayNghi, thangNghi, namNghi;
                ngayNghi = "";
                thangNghi = "";
                namNghi = "";
                int numngayNghi, numthangNghi, numnamNghi;
                numngayNghi = numthangNghi = numnamNghi = 0;


                if (cursor.moveToFirst()) {
                    ngayVaoLam = cursor.getString(1);
                    ngayVao += ngayVaoLam.charAt(0);
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
                        numngayVao = Integer.parseInt(ngayVao);
                        numthangVao = Integer.parseInt(thangVao);
                        numnamVao = Integer.parseInt(namVao);
                    } catch (Exception e) {
                    }
                    if (cursor.getString(2) != null) {
                        ngayThoiViec = cursor.getString(2);
                        ngayNghi += ngayThoiViec.charAt(0);
                        Pattern patternNghi = Pattern.compile("\\d*");
                        Matcher matcherNghi = patternNghi.matcher(ngayThoiViec.charAt(1) + "");


                        if (matcherNghi.matches()) {
                            ngayNghi += ngayThoiViec.charAt(1);
                            thangNghi += ngayThoiViec.charAt(3);
                            matcherNghi = patternNghi.matcher(ngayThoiViec.charAt(4) + "");
                            if (matcherNghi.matches())
                                thangNghi += ngayThoiViec.charAt(4);

                        } else {
                            thangNghi += ngayThoiViec.charAt(2);
                            matcherNghi = patternNghi.matcher(ngayThoiViec.charAt(3) + "");
                            if (matcherNghi.matches())
                                thangNghi += ngayThoiViec.charAt(3);
                        }

                        int lengthNghi = ngayThoiViec.length();
                        namNghi += ngayThoiViec.charAt(lengthNghi - 4);
                        namNghi += ngayThoiViec.charAt(lengthNghi - 3);
                        namNghi += ngayThoiViec.charAt(lengthNghi - 2);
                        namNghi += ngayThoiViec.charAt(lengthNghi - 1);

                        try {
                            numngayNghi = Integer.parseInt(ngayNghi);
                            numthangNghi = Integer.parseInt(thangNghi);
                            numnamNghi = Integer.parseInt(namNghi);
                        } catch (Exception e) {
                        }

                    }
                }


                if (!ngayThoiViec.equals("")) {


                    if (nam < numnamVao || nam > numnamNghi) {
                        if (nam < numnamVao) {
                            Toast.makeText(this, "Nhân viên bắt đầu làm " + cursor.getString(1), Toast.LENGTH_SHORT).show();
                        } else if (nam > numnamNghi) {
                            Toast.makeText(this, "Nhân viên đã nghỉ việc " + cursor.getString(2), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        if ((thang < numthangVao || thang > numthangNghi) && (nam == numnamVao)) {
                            if (thang < numthangVao) {
                                Toast.makeText(this, "Nhân viên bắt đầu làm " + cursor.getString(1), Toast.LENGTH_SHORT).show();
                            } else if (thang > numthangNghi) {
                                Toast.makeText(this, "Nhân viên đã nghỉ việc " + cursor.getString(2), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if ((ngay < numngayVao || ngay > numngayNghi) && (thang == numthangVao)) {
                                if (ngay < numngayVao) {
                                    Toast.makeText(this, "Nhân viên bắt đầu làm " + cursor.getString(1), Toast.LENGTH_SHORT).show();
                                } else if (ngay > numngayNghi) {
                                    Toast.makeText(this, "Nhân viên đã nghỉ việc " + cursor.getString(2), Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                ContentValues values = new ContentValues();
                                values.put("MSNV_TC", maNhanVien);
                                values.put("Nam", nam);
                                values.put("Thang", thang);
                                values.put("Ngay", ngay);
                                values.put("TrangThai", trangThai);
                                values.put("GioRa", gioRa);
                                values.put("GioVao", gioVao);
                                long add = database.insert("TinhCong", null, values);
                                if (add == -1) {
                                    Toast.makeText(this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                } else {
                    if (nam < numnamVao) {
                        Toast.makeText(this, "Nhân viên bắt đầu làm " + cursor.getString(1), Toast.LENGTH_SHORT).show();
                    } else {
                        if (thang < numthangVao) {
                            Toast.makeText(this, "Nhân viên bắt đầu làm " + cursor.getString(1), Toast.LENGTH_SHORT).show();
                        } else {
                            if (thang < numthangVao) {
                                Toast.makeText(this, "Nhân viên bắt đầu làm " + cursor.getString(1), Toast.LENGTH_SHORT).show();
                            } else {
                                ContentValues values = new ContentValues();
                                values.put("MSNV_TC", maNhanVien);
                                values.put("Nam", nam);
                                values.put("Thang", thang);
                                values.put("Ngay", ngay);
                                values.put("TrangThai", trangThai);
                                values.put("GioRa", gioRa);
                                values.put("GioVao", gioVao);
                                long add = database.insert("TinhCong", null, values);
                                if (add == -1) {
                                    Toast.makeText(this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(this, "Thêm thành công dòng thứ " + add, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    }


                }
                cursor.close();
            } catch (Exception ex) {

            }
        }

    }

    private void xuLySaoChepCSDLTuAssetsVaoHeThongMobile() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAcsset();
            } catch (Exception e) {
                Toast.makeText(NhapCongActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

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
