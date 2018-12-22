package com.example.phungvandat.employeemanager;

import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
public class MenuActivity extends TabActivity {

    ImageView imgAvatar;
    TextView txtHoTen, txtMaNhanVien, txtPhongBan;
    LinearLayout lnlDoiMatKhau;
    ImageView imgPhongBan;
    ImageButton btnLogout;

    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;

    String maNhanVien = "";
    int quyenTruyCap = 1;

    TabHost tabHost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    tabHost.getTabWidget().getChildAt(i)
                            .setBackgroundColor(Color.WHITE); // unselected
                }
                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
                        .setBackgroundColor(Color.parseColor("#ff5a01")); // selected
            }
        });
        lnlDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDoiMatKhau();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyLogout();
            }
        });
    }

    private void xuLyLogout() {
        Intent i= new Intent(this,MainActivity.class);
        startActivity(i);
    }

    private void xuLyDoiMatKhau() {
        if(quyenTruyCap!=1) {
            Intent i = new Intent(this, DoiMatKhauActivity.class);
            i.putExtra("MSNV", maNhanVien);
            startActivity(i);
        }
    }

    private void addControls() {

        quyenTruyCap = getIntent().getIntExtra("QUYEN_TRUY_CAP", 1);
        maNhanVien = getIntent().getStringExtra("MSNV");

        Intent intent1 = new Intent(this, Tab1Menu.class);
        intent1.putExtra("QUYEN_TRUY_CAP", quyenTruyCap);

        Intent intent2 = new Intent(this, Tab2Menu.class);
        intent2.putExtra("QUYEN_TRUY_CAP", quyenTruyCap);
        intent2.putExtra("MSNV", maNhanVien);

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        TabHost.TabSpec tab1 = tabHost.newTabSpec("NHÂN VIÊN");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("TÍNH CÔNG");

        tab1.setIndicator("", getResources().getDrawable(R.drawable.imgemployess));
        tab1.setContent(intent1);

        tab2.setIndicator("", getResources().getDrawable(R.drawable.imgpayroll));
        tab2.setContent(intent2);

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);

        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#ff5a01"));

        maNhanVien = getIntent().getStringExtra("MSNV");
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        txtHoTen = (TextView) findViewById(R.id.txtHoTen);
        txtMaNhanVien = (TextView) findViewById(R.id.txtMaNhanVien);
        txtPhongBan = (TextView) findViewById(R.id.txtPhongBan);
        lnlDoiMatKhau= (LinearLayout) findViewById(R.id.lnlDoiMatKhau);
        btnLogout= (ImageButton) findViewById(R.id.btnLogout);
        loadThongTinNhanVien();
    }

    private void loadThongTinNhanVien() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = null;
        try {
            String hoTen, phongBan;
            hoTen = phongBan = "";
            byte[] avatar = null;
            String query  = "select HoTen,PhongBan,Hinh from NhanVien where MSNV=";
            cursor = database.rawQuery( query+ maNhanVien, null);
            while (cursor.moveToNext()) {
                hoTen = cursor.getString(0);
                phongBan = cursor.getString(1);
                avatar = cursor.getBlob(2);
            }
            if (avatar == null)
                imgAvatar.setImageResource(R.drawable.imgavatar);
            else {
                Bitmap bmImage = BitmapFactory.decodeByteArray(avatar, 0, avatar.length);
                imgAvatar.setImageBitmap(bmImage);
            }
            txtHoTen.setText("  "+hoTen);
            txtMaNhanVien.setText(maNhanVien);
            txtPhongBan.setText("  "+phongBan);
            if(phongBan.equals("Tất Cả"))
                imgPhongBan.setImageResource(R.drawable.all_icon);
            else if(phongBan.equals("Phòng Tài Chính"))
                imgPhongBan.setImageResource(R.drawable.tai_chinh_icon);
            else if(phongBan.equals("Phòng Kỹ Thuật"))
                imgPhongBan.setImageResource(R.drawable.ky_thuat_icon);
            else if(phongBan.equals("Phòng Nhân Sự"))
                imgPhongBan.setImageResource(R.drawable.nhan_su_icon);
            else if(phongBan.equals("Phòng Kinh Doanh"))
                imgPhongBan.setImageResource(R.drawable.kinh_doanh_icon);
            cursor.close();
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
