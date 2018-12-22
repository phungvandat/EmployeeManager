package com.example.phungvandat.employeemanager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DangKiTaiKhoanActivity extends Activity {
    ImageButton btnBack;
    TextView txtMSNV, txtMatKhau, txtMatKhauXacNhan;
    Button btnDangKi;

    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ki_tai_khoan);
        xuLySaoChepCSDLTuAsssetsVaoHeThongMobile();
        addControls();
        addEvents();

    }

    private void xuLySaoChepCSDLTuAsssetsVaoHeThongMobile() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                copyDataBaseFromAcssets();
            } catch (Exception ex) {
                Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void copyDataBaseFromAcssets() {
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
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDangKi();
            }
        });
    }

    private void xuLyDangKi() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor ckt = null;
        try {
            ckt = database.rawQuery("select MSNV from NhanVien", null);
            boolean checkTonTai = false;
            while (ckt.moveToNext()) {
                if (String.valueOf(ckt.getInt(0)).equals(txtMSNV.getText().toString())) {
                    checkTonTai = true;
                    break;
                }
            }
            if (checkTonTai == false) {
                Toast.makeText(this, "Không tồn tại nhân viên " + txtMSNV.getText().toString(), Toast.LENGTH_SHORT).show();
                return;
            }
            ckt = database.rawQuery("select MSNV_TK from TaiKhoanNhanVien", null);
            while (ckt.moveToNext()) {
                if (String.valueOf(ckt.getInt(0)).equals(txtMSNV.getText().toString())) {
                    Toast.makeText(this, "Tài khoản nhân viên " + txtMSNV.getText().toString() + " đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (!txtMatKhau.getText().toString().equals(txtMatKhauXacNhan.getText().toString())) {
                txtMatKhauXacNhan.setText("");
                txtMatKhauXacNhan.setHint("Mật khẩu không trùng");
                return;
            }

            Intent i = new Intent(this, MaXacNhanActivity.class);
            i.putExtra("MA_NHAN_VIEN", txtMSNV.getText().toString());
            i.putExtra("MAT_KHAU", txtMatKhau.getText().toString());

            startActivityForResult(i, 97);

            ckt.close();

        } catch (Exception ex) {

        }
    }

    private void addControls() {
        btnBack= (ImageButton) findViewById(R.id.btnBack);
        txtMSNV = (TextView) findViewById(R.id.txtMSNV);
        txtMatKhau = (TextView) findViewById(R.id.txtMatKhau);
        txtMatKhauXacNhan = (TextView) findViewById(R.id.txtMatKhauXacNhan);
        btnDangKi = (Button) findViewById(R.id.btnDangKi);
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
