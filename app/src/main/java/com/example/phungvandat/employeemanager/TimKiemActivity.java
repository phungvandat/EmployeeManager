package com.example.phungvandat.employeemanager;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.phungvandat.adapter.NhanVienAdapter;
import com.example.phungvandat.model.NhanVien;

public class TimKiemActivity extends Activity {

    ImageButton btnBack, btnOptionMenu;
    TextView txtTitle;

    EditText txtThuocTinh;
    ImageButton btnTimKiem;

    String thuocTinh="MSNV";

    ListView lvTimKiem;
    ArrayList<NhanVien> dsNhanVien;
    NhanVienAdapter nhanVienAdapter;

    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/database/";
    SQLiteDatabase database = null;

    int quyenTruyCap = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kiem);
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
                Toast.makeText(TimKiemActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

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
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyBack();
            }
        });
        btnOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyOptionMenu(v);
            }
        });
        btnTimKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyTimKiem();
            }
        });
    }

    private void xuLyOptionMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        popup.getMenuInflater().inflate(R.menu.menu_tim_kiem, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                xuLyOptionsItemSelected(item);
                return true;
            }
        });
        popup.show();
    }

    private void xuLyOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnuMaNhanVien) {
           thuocTinh="MSNV";

        } else if (item.getItemId() == R.id.mnuHoTen) {
            thuocTinh="HoTen";
            txtTitle.setText("TÌM THEO HỌ TÊN");
        } else if (item.getItemId() == R.id.mnuCMND) {
            thuocTinh="CMND";
            txtTitle.setText("TÌM THEO SỐ CMND");
        } else if (item.getItemId() == R.id.mnuPhone) {
            thuocTinh="Phone";
            txtTitle.setText("TÌM THEO SỐ ĐIỆN THOẠI");
        } else if (item.getItemId() == R.id.mnuEmail) {
            thuocTinh="Email";
            txtTitle.setText("TÌM THEO EMAIL");
        }else if (item.getItemId() == R.id.mnuNguyenQuan){
            thuocTinh="NguyenQuan";
            txtTitle.setText("TÌM THEO NGUYÊN QUÁN");
        }
    }

    private void xuLyBack() {
        finish();
    }

    private void addDSNhanVien() {
        try {
            database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            String noiDungThuocTinh = "";
            try {
                noiDungThuocTinh = txtThuocTinh.getText().toString();
            } catch (Exception ex) {

            }
            if (noiDungThuocTinh.equals("")) {
                return;
            }
            dsNhanVien = new ArrayList<>();
            Cursor cursor = null;
            Pattern pattern = Pattern.compile("\\d*");
            int check;

            if (thuocTinh.equals("MSNV")) {
                check = 0;
                for (int i = 0; i < noiDungThuocTinh.length(); i++) {
                    Matcher matcher = pattern.matcher(noiDungThuocTinh.charAt(i) + "");
                    if (!matcher.matches() || Character.isLetter(noiDungThuocTinh.charAt(i))) {
                        check++;
                        break;
                    }
                }
                if (check != 0) {
                    Toast.makeText(TimKiemActivity.this, "Mã nhân viên không hợp lệ", Toast.LENGTH_SHORT).show();
                } else {
                    cursor = database.rawQuery("select * from NhanVien where MSNV like '%" + noiDungThuocTinh + "%'", null);
                    napListView(cursor);


                }

            } else if (thuocTinh.equals("HoTen")) {
                cursor = database.rawQuery("select * from NhanVien where HoTen like '%" + noiDungThuocTinh + "%'", null);
                napListView(cursor);

            } else if (thuocTinh.equals("CMND")) {
                check = 0;
                for (int i = 0; i < noiDungThuocTinh.length(); i++) {
                    Matcher matcher = pattern.matcher(noiDungThuocTinh.charAt(i) + "");
                    if (!matcher.matches() || Character.isLetter(noiDungThuocTinh.charAt(i))) {
                        check++;
                        break;
                    }
                }
                if (check != 0) {
                    Toast.makeText(TimKiemActivity.this, "Số chứng minh không hợp lệ", Toast.LENGTH_SHORT).show();
                } else {
                    cursor = database.rawQuery("select * from NhanVien where CMND like '%" + noiDungThuocTinh + "%'", null);
                    napListView(cursor);


                }
            } else if (thuocTinh.equals("Phone")) {
                check = 0;
                for (int i = 0; i < noiDungThuocTinh.length(); i++) {
                    Matcher matcher = pattern.matcher(noiDungThuocTinh.charAt(i) + "");
                    if (!matcher.matches() || Character.isLetter(noiDungThuocTinh.charAt(i))) {
                        check++;
                        break;
                    }
                }
                if (check != 0) {
                    Toast.makeText(TimKiemActivity.this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                } else {
                    cursor = database.rawQuery("select * from NhanVien where Phone like '%" + noiDungThuocTinh + "%'", null);
                    napListView(cursor);
                }

            } else if (thuocTinh.equals("Email")) {
                cursor = database.rawQuery("select * from NhanVien where Email like '%" + noiDungThuocTinh + "%'", null);
                napListView(cursor);

            } else if (thuocTinh.equals("NguyenQuan")) {
                cursor = database.rawQuery("select * from NhanVien where NguyenQuan like '%" + noiDungThuocTinh + "%'", null);
                napListView(cursor);
            }
            cursor.close();
        }
        catch (Exception ex){

        }
    }

    private void xuLyTimKiem() {
        if (txtThuocTinh.getText().toString().equals(""))
            return;
        addDSNhanVien();
        nhanVienAdapter = new NhanVienAdapter(TimKiemActivity.this, R.layout.item_nhan_vien, dsNhanVien, quyenTruyCap);
        nhanVienAdapter.notifyDataSetChanged();
        lvTimKiem.setAdapter(nhanVienAdapter);
    }

    private void napListView(Cursor cursor) {
        while (cursor.moveToNext()) {
            int mSNV = cursor.getInt(0);
            String hoTen = cursor.getString(1);
            byte[] byteImage = cursor.getBlob(cursor.getColumnIndex("Hinh"));
            String ngaySinh = cursor.getString(2);
            String gioiTinh = cursor.getString(3);
            int cMND = cursor.getInt(4);
            String phone = cursor.getString(5);
            String email = cursor.getString(6);
            String danToc = cursor.getString(7);
            String nguyenQuan = cursor.getString(8);
            String hoKhau = cursor.getString(9);
            String tonGiao = cursor.getString(10);
            String honNhan = cursor.getString(11);
            String phongBan = cursor.getString(12);
            Float heSoLuong = cursor.getFloat(13);
            String lichSuPhongBan=cursor.getString(15);


            int length = ngaySinh.length();
            int numngay, numthang, numnam;
            numnam = numngay = numthang = 0;
            if (length >= 8) {
                String ngay, thang, nam;
                thang = "";
                nam = "";
                ngay = "" + ngaySinh.charAt(0);
                Pattern pattern = Pattern.compile("\\d*");
                Matcher matcher = pattern.matcher(ngaySinh.charAt(1) + "");

                if (matcher.matches()) {
                    ngay += ngaySinh.charAt(1);
                    thang += ngaySinh.charAt(3);
                    matcher = pattern.matcher(ngaySinh.charAt(4) + "");
                    if (matcher.matches())
                        thang += ngaySinh.charAt(4);
                    System.out.println(ngay + "   " + thang);
                } else {
                    thang += ngaySinh.charAt(2);
                    matcher = pattern.matcher(ngaySinh.charAt(3) + "");
                    if (matcher.matches())
                        thang += ngaySinh.charAt(3);

                    System.out.println(ngay + "   " + thang);

                }
                nam += ngaySinh.charAt(length - 4);
                nam += ngaySinh.charAt(length - 3);
                nam += ngaySinh.charAt(length - 2);
                nam += ngaySinh.charAt(length - 1);

                System.out.println(ngay + "   " + thang + "   " + nam + "   " + length);
                try {
                    numngay = Integer.parseInt(ngay);
                    numthang = Integer.parseInt(thang);
                    numnam = Integer.parseInt(nam);
                } catch (Exception e) {
                }

            }


            dsNhanVien.add(new NhanVien(String.valueOf(mSNV), hoTen, numngay, numthang, numnam, gioiTinh, cMND,
                    phone, email, danToc, nguyenQuan, hoKhau, tonGiao, honNhan, phongBan, heSoLuong, byteImage,lichSuPhongBan));

        }
        cursor.close();

    }

    private void addControls() {
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnOptionMenu = (ImageButton) findViewById(R.id.btnOptionMenu);
        txtTitle = (TextView) findViewById(R.id.txtTitle);

        quyenTruyCap = getIntent().getIntExtra("QUYEN_TRUY_CAP", 1);

        txtThuocTinh = (EditText) findViewById(R.id.txtThuocTinh);
        btnTimKiem = (ImageButton) findViewById(R.id.btnTimKiem);

        lvTimKiem = (ListView) findViewById(R.id.lvTimKiem);
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
