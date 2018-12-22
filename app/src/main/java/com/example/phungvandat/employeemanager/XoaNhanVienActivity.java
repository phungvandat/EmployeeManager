package com.example.phungvandat.employeemanager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.phungvandat.model.NhanVien;

public class XoaNhanVienActivity extends Activity {

    ImageView imgAvatar;
    TextView txtMaNhanVien, txtHoTen;
    Button btnDongY, btnBoQua;

    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xoa_nhan_vien);
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

    private void addControls() {

        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        txtHoTen = (TextView) findViewById(R.id.txtHoTen);
        txtMaNhanVien = (TextView) findViewById(R.id.txtMaNhanVien);
        btnDongY = (Button) findViewById(R.id.btnDongY);
        btnBoQua = (Button) findViewById(R.id.btnBoQua);

        Intent i = getIntent();
        NhanVien nhanVien = (NhanVien) i.getSerializableExtra("KIEU_NHAN_VIEN");
        String maNhanVien = nhanVien.getMaNhanVien();
        String hoTen = nhanVien.getHoTen();
        byte[] avatar = nhanVien.getHinhAnh();
        txtMaNhanVien.setText(maNhanVien);
        txtHoTen.setText(hoTen);

        if (avatar == null) {
            imgAvatar.setImageResource(R.drawable.imgavatar);
        } else {
            Bitmap bm = BitmapFactory.decodeByteArray(avatar, 0, avatar.length);
            imgAvatar.setImageBitmap(bm);
        }
    }

    private void addEvents() {
        btnDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDongY();
            }
        });
        btnBoQua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyBoQua();
            }
        });
    }

    private void xuLyBoQua() {
        finish();
    }

    private void xuLyDongY() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        try {
            Cursor cursor = database.rawQuery("select  * from NhanVien where MSNV=" + txtMaNhanVien.getText().toString(),
                    null);
            int mSNV = 0, cMND = 0;
            String hoTen = null, ngaySinh = null, gioiTinh = null, phone = null, email = null, danToc = null, nguyenQuan = null, hoKhau = null,
                    tonGiao = null, honNhan = null, phongBan = null, lichSuPhongBan = null;
            byte[] byteImage = new byte[0];
            Float heSoLuong = null;

            while (cursor.moveToNext()) {
                mSNV = cursor.getInt(0);
                hoTen = cursor.getString(1);
                byteImage = cursor.getBlob(cursor.getColumnIndex("Hinh"));
                ngaySinh = cursor.getString(2);
                gioiTinh = cursor.getString(3);
                cMND = cursor.getInt(4);
                phone = cursor.getString(5);
                email = cursor.getString(6);
                danToc = cursor.getString(7);
                nguyenQuan = cursor.getString(8);
                hoKhau = cursor.getString(9);
                tonGiao = cursor.getString(10);
                honNhan = cursor.getString(11);
                phongBan = cursor.getString(12);
                heSoLuong = cursor.getFloat(13);
                lichSuPhongBan = cursor.getString(15);
            }


            ContentValues values = new ContentValues();
            values.put("MSNV", mSNV);
            values.put("HoTen", hoTen);
            values.put("NgaySinh", ngaySinh);
            values.put("GioiTinh", gioiTinh);
            values.put("CMND", cMND);
            values.put("Phone", phone);
            values.put("Email", email);
            values.put("DanToc", danToc);
            values.put("NguyenQuan", nguyenQuan);
            values.put("HoKhau", hoKhau);
            values.put("TonGiao", tonGiao);
            values.put("HonNhan", honNhan);
            values.put("PhongBan", phongBan);
            values.put("HeSoLuong", heSoLuong);
            values.put("Hinh", byteImage);
            values.put("LichSuPhongBan", lichSuPhongBan);
            long add = database.insert("NhanVienBackup", null, values);
            if (add == -1) {
                Toast.makeText(this, "Backup thất bại", Toast.LENGTH_SHORT).show();
            }
            database.delete("NhanVien", "MSNV=?", new String[]{txtMaNhanVien.getText().toString()});
            cursor.close();
            finish();
        } catch (Exception ex) {
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
