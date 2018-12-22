package com.example.phungvandat.employeemanager;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.View;
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
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

public class dsPhongBanActivity extends Activity {

    ImageButton btnBack, btnSearch, btnOptionMenu, btnExit;
    LinearLayout lnlSearch;
    TableLayout tbTitle;
    TextView txtTitle;

    ListView lvNhanVien;
    ArrayList<NhanVien> dsNhanVien;
    NhanVienAdapter nhanVienAdapter;

    EditText txtTimKiem;

    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;

    int quyenTruyCap = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ds_phong_ban);
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
                Toast.makeText(dsPhongBanActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

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
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLySearch();
            }
        });
        btnOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyOptionMenu(v);
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyExit();
            }
        });
        txtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nhanVienAdapter.filter(txtTimKiem.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void xuLyExit() {
        tbTitle.setPadding(16,16,16,16);
        lnlSearch.setVisibility(View.GONE);
        txtTitle.setVisibility(View.VISIBLE);
        btnSearch.setVisibility(View.VISIBLE);
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
        popup.getMenuInflater().inflate(R.menu.menu_phong_ban, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                xuLyOptionsItemSelected(item);
                return true;
            }
        });
        popup.show();
    }


    private void xuLySearch() {
        tbTitle.setPadding(0,0,0,0);
        lnlSearch.setVisibility(View.VISIBLE);
        txtTitle.setVisibility(View.GONE);
        btnSearch.setVisibility(View.GONE);
    }

    private void xuLyBack() {
        finish();
    }

    private void addControls() {

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        btnOptionMenu = (ImageButton) findViewById(R.id.btnOpitonMenu);
        lnlSearch = (LinearLayout) findViewById(R.id.lnlSearch);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        btnExit = (ImageButton) findViewById(R.id.btnExit);
        tbTitle= (TableLayout) findViewById(R.id.tbTitle);

        lnlSearch.setVisibility(View.GONE);

        quyenTruyCap = getIntent().getIntExtra("QUYEN_TRUY_CAP", 1);

        setTitle("DANH SÁCH NHÂN VIÊN");

        txtTimKiem = (EditText) findViewById(R.id.txtTimKiem);

        lvNhanVien = (ListView) findViewById(R.id.lvNhanVien);
        addDanhSachNhanVien();
        nhanVienAdapter = new NhanVienAdapter(this, R.layout.item_nhan_vien, dsNhanVien, quyenTruyCap);
        nhanVienAdapter.notifyDataSetChanged();
        lvNhanVien.setAdapter(nhanVienAdapter);
    }

    private void addDanhSachNhanVien() {

        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor;
        try {
            String query = "select * from NhanVien";
            cursor = database.rawQuery( query, null);
            dsNhanVien = new ArrayList<>();
            int mSNV, cMND, length, numngay, numthang, numnam;
            String hoTen, ngaySinh, gioiTinh, phone, email, danToc, nguyenQuan, hoKhau, tonGiao, honNhan, phongBan,lichSuPhongBan;
            byte[] byteImage;
            Float heSoLuong;
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
                lichSuPhongBan=cursor.getString(cursor.getColumnIndex("LichSuPhongBan"));

                length = ngaySinh.length();
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
        } catch (Exception ex) {
            ex.printStackTrace();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        addDanhSachNhanVien();
        nhanVienAdapter = new NhanVienAdapter(this, R.layout.item_nhan_vien, dsNhanVien, quyenTruyCap);
        nhanVienAdapter.notifyDataSetChanged();
        lvNhanVien.setAdapter(nhanVienAdapter);

    }


    private void xuLyOptionsItemSelected(MenuItem item) {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = null;
        try {
            if (item.getItemId() == R.id.mnuTatCa) {
                cursor = database.rawQuery("select * from NhanVien"
                        , null);
            } else if (item.getItemId() == R.id.mnuTaiChinh) {
                cursor = database.rawQuery("select * from NhanVien where PhongBan='Phòng Tài Chính'"
                        , null);
                txtTitle.setText("NHÂN VIÊN TÀI CHÍNH");
            } else if (item.getItemId() == R.id.mnuKyThuat) {
                cursor = database.rawQuery("select * from NhanVien where PhongBan='Phòng Kỹ Thuật'"
                        , null);
                txtTitle.setText("NHÂN VIÊN KỸ THUẬT");
            } else if (item.getItemId() == R.id.mnuNhanSu) {
                cursor = database.rawQuery("select * from NhanVien where PhongBan='Phòng Nhân Sự'"
                        , null);
                txtTitle.setText("NHÂN VIÊN NHÂN SỰ");
            } else if (item.getItemId() == R.id.mnuKinhDoanh) {
                cursor = database.rawQuery("select * from NhanVien where PhongBan='Phòng Kinh Doanh'"
                        , null);
                txtTitle.setText("NHÂN VIÊN KINH DOANH");
            }
            dsNhanVien = new ArrayList<>();
            int mSNV, cMND, length, numngay, numthang, numnam;
            String hoTen, ngaySinh, gioiTinh, phone, email, danToc, nguyenQuan, hoKhau, tonGiao, honNhan, phongBan,lichSuPhongBan;
            byte[] byteImage;
            Float heSoLuong;
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
               lichSuPhongBan=cursor.getString(cursor.getColumnIndex("LichSuPhongBan"));

                length = ngaySinh.length();
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
        } catch (Exception ex) {

        }
        nhanVienAdapter = new NhanVienAdapter(this, R.layout.item_nhan_vien, dsNhanVien, quyenTruyCap);
        nhanVienAdapter.notifyDataSetChanged();
        lvNhanVien.setAdapter(nhanVienAdapter);

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


