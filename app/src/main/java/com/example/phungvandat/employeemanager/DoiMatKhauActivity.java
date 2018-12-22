package com.example.phungvandat.employeemanager;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DoiMatKhauActivity extends Activity {
    TextView txtMaNhanVien;
    EditText txtMatKhauHienTai, txtMatKhauMoi, txtXacNhanMatKhauMoi;
    Button btnCapNhat;

    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;

    String maNhanVien = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);
        xuLySaoChepCSDLTuAssetsVaoHeThongMobile();
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyCapNhat();
            }
        });

    }

    private void xuLyCapNhat() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor ckt = null;
        try {
            String query = "select *  from TaiKhoanNhanVien where MSNV_TK=";
            ckt = database.rawQuery(query + maNhanVien, null);
            int check = 0;
            while (ckt.moveToNext())
                if (!txtMatKhauHienTai.getText().toString().equals(ckt.getString(1)))
                    check++;
            if (check != 0) {
                Toast.makeText(this, "Mật khẩu hiện tại không chính xác", Toast.LENGTH_SHORT).show();
                txtMatKhauHienTai.setText("");
                return;
            }
            if (!txtMatKhauMoi.getText().toString().equals(txtXacNhanMatKhauMoi.getText().toString())) {
                Toast.makeText(this, "Mật khẩu xác nhận không trùng", Toast.LENGTH_SHORT).show();
                txtXacNhanMatKhauMoi.setText("");
                return;
            }
            ContentValues values = new ContentValues();
            values.put("MatKhau", txtMatKhauMoi.getText().toString());
            String[] dieuKien = {maNhanVien};
            int ret = database.update("TaiKhoanNhanVien", values, "MSNV_TK=?", dieuKien);
            if (ret == 0) {
                Toast.makeText(this, "Không thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Thành công", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception ex) {

        }
    }

    private void addControls() {
        maNhanVien = getIntent().getStringExtra("MSNV");

        txtMaNhanVien = (TextView) findViewById(R.id.txtMaNhanVien);
        txtMaNhanVien.setText(maNhanVien);
        txtMatKhauHienTai = (EditText) findViewById(R.id.txtMatKhauHienTai);
        txtMatKhauMoi = (EditText) findViewById(R.id.txtMatKhauMoi);
        txtXacNhanMatKhauMoi = (EditText) findViewById(R.id.txtXacNhanMatKhauMoi);

        btnCapNhat = (Button) findViewById(R.id.btnCapNhat);
    }

    private void xuLySaoChepCSDLTuAssetsVaoHeThongMobile() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAcsset();
                Toast.makeText(this, "Sao chep database thanh cong", Toast.LENGTH_SHORT).show();

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
}