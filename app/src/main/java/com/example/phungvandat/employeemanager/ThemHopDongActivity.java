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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ThemHopDongActivity extends Activity {

    AutoCompleteTextView autotxtMaNhanVien;
    ArrayList<String> dsMaNhanVien;
    ArrayAdapter<String> maNhanVienAdapter;

    Spinner spNgayVao, spThangVao, spNamVao, spNgayRa, spThangRa, spNamRa;

    ArrayList<String> dsNgayVao;
    ArrayAdapter<String> ngayVaoAdapter;
    int viTriNgayVao = 0;

    ArrayList<String> dsThangVao;
    ArrayAdapter<String> thangVaoAdapter;
    int viTriThangVao = 0;

    ArrayList<String> dsNamVao;
    ArrayAdapter<String> namVaoAdapter;
    int viTriNamVao = 0;

    ArrayList<String> dsNgayRa;
    ArrayAdapter<String> ngayRaAdapter;
    int viTriNgayRa = 0;

    ArrayList<String> dsThangRa;
    ArrayAdapter<String> thangRaAdapter;
    int viTriThangRa = 0;

    ArrayList<String> dsNamRa;
    ArrayAdapter<String> namRaAdapter;
    int viTriNamRa = 0;

    Button btnThem;

    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_hop_dong);
        xuLySaoChepCSDLTuAssetsVaoHeThongMobile();
        addControl();
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
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyThem();
            }
        });
        spNgayVao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriNgayVao = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spThangVao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriThangVao = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spNamVao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriNamVao = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spNgayRa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriNgayRa = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spThangRa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriThangRa = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spNamRa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriNamRa = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void xuLyThem() {
        Cursor ckt = null;
        Cursor ckt2 = null;
        try {
            ckt = database.rawQuery("select MSNV from NhanVien", null);
            String maNhanVien = autotxtMaNhanVien.getText().toString();
            int kt = 0;
            while (ckt.moveToNext())
                if (Integer.parseInt(maNhanVien) == ckt.getInt(0))
                    kt++;
            if (kt == 0) {
                Toast.makeText(this, "Không tồn tại nhân viên " + maNhanVien, Toast.LENGTH_SHORT).show();
                return;
            }
            ckt2 = database.rawQuery("select MSNV_HD from HopDongViecLam where MSNV_HD=" + maNhanVien, null);
            if (ckt2.getCount() > 0) {
                Toast.makeText(this, "Hợp đồng làm việc của nhân viên " + maNhanVien + " đã tồn tại", Toast.LENGTH_SHORT).show();
                return;
            }

            String ngayVaolam = dsNgayVao.get(viTriNamVao) + "/" + dsThangVao.get(viTriThangVao) + "/"
                    + dsNamVao.get(viTriNamVao);
            String ngayNghiViec = "";
            if (dsNgayRa.get(viTriNgayRa).equals("null") || dsThangRa.get(viTriThangRa).equals("null")
                    || dsNamRa.get(viTriNamRa).equals("null"))
                ngayNghiViec = null;
            else {
                ngayNghiViec = dsNgayRa.get(viTriNgayRa) + "/" + dsThangRa.get(viTriThangRa) + "/" + dsNamRa.get(viTriNamRa);
            }
            ContentValues values = new ContentValues();
            values.put("MSNV_HD", Integer.parseInt(autotxtMaNhanVien.getText().toString()));
            values.put("NgayVaoLam", ngayVaolam);
            values.put("NgayThoiViec", ngayNghiViec);
            if (ngayNghiViec == null)
                values.put("TrangThaiHopDong", "Không");
            else
                values.put("TrangThaiHopDong", "Có");
            long add = database.insert("HopDongViecLam", null, values);
            if (add == -1) {
                Toast.makeText(this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Thêm thành công dòng thứ " + add, Toast.LENGTH_SHORT).show();
            }
            ckt.close();
            ckt2.close();

        } catch (Exception ex) {
        }

    }

    private void addControl() {
        autotxtMaNhanVien = (AutoCompleteTextView) findViewById(R.id.autotxtMaNhanVien);
        addDSMaNhanVien();
        maNhanVienAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dsMaNhanVien);
        autotxtMaNhanVien.setAdapter(maNhanVienAdapter);


        spNgayVao = (Spinner) findViewById(R.id.spNgayVao);
        addDSNgayVao();
        ngayVaoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dsNgayVao);
        ngayVaoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNgayVao.setAdapter(ngayVaoAdapter);

        spThangVao = (Spinner) findViewById(R.id.spThangVao);
        addDSThangVao();
        thangVaoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dsThangVao);
        thangVaoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spThangVao.setAdapter(thangVaoAdapter);

        spNamVao = (Spinner) findViewById(R.id.spNamVao);
        addDSNamVao();
        namVaoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dsNamVao);
        namVaoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNamVao.setAdapter(namVaoAdapter);

        spNgayRa = (Spinner) findViewById(R.id.spNgayRa);
        addDSNgayRa();
        ngayRaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dsNgayRa);
        ngayRaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNgayRa.setAdapter(ngayRaAdapter);

        spThangRa = (Spinner) findViewById(R.id.spThangRa);
        addDSThangRa();
        thangRaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dsThangRa);
        thangRaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spThangRa.setAdapter(thangRaAdapter);

        spNamRa = (Spinner) findViewById(R.id.spNamRa);
        addDSNamRa();
        namRaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dsNamRa);
        namRaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNamRa.setAdapter(namRaAdapter);

        btnThem = (Button) findViewById(R.id.btnThem);


    }

    private void addDSMaNhanVien() {

        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("SELECT MSNV FROM NhanVien\n" +
                    "where MSNV not in\n" +
                    "(select MSNV_HD from HopDongViecLam)", null);
            dsMaNhanVien = new ArrayList<>();
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    dsMaNhanVien.add(cursor.getString(0));
                }
            }
        } catch (Exception ex) {

        }
    }

    private void addDSNamRa() {
        dsNamRa = new ArrayList<>();
        dsNamRa.add("null");
        for (int i = 2017; i <= 3000; i++) {
            dsNamRa.add(String.valueOf(i));
        }

    }

    private void addDSThangRa() {
        dsThangRa = new ArrayList<>();
        dsThangRa.add("null");
        for (int i = 1; i <= 12; i++) {
            dsThangRa.add(String.valueOf(i));
        }
    }

    private void addDSNgayRa() {
        dsNgayRa = new ArrayList<>();
        dsNgayRa.add("null");
        for (int i = 1; i <= 31; i++) {
            dsNgayRa.add(String.valueOf(i));
        }
    }

    private void addDSNamVao() {
        dsNamVao = new ArrayList<>();
        dsNamVao.add(String.valueOf(2017));
    }

    private void addDSThangVao() {
        dsThangVao = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            dsThangVao.add(String.valueOf(i));
        }

    }

    private void addDSNgayVao() {
        dsNgayVao = new ArrayList<>();
        for (int i = 1; i <= 31; i++) {
            dsNgayVao.add(String.valueOf(i));
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
