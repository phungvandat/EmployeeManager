package com.example.phungvandat.employeemanager;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThemNhanVienActivity extends Activity {

    ImageButton btnBack, btnOptionMenu;

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    ImageView imgAvatar;
    int RESULT_LOAD_IMG = 1;

    Button btnChange, btnThem;
    EditText txtMaNhanVien, txtHoTen, txtCMND, txtHoKhau, txtNguyenQuan, txtTonGiao, txtPhone, txtEmail;

    AutoCompleteTextView autotxtDanToc;
    String[] arrDanToc;
    ArrayAdapter<String> adapterDanToc;

    Spinner spPhongBan;
    ArrayList<String> dsPhongBan;
    ArrayAdapter<String> adapterPhongBan;
    int viTriSpPhongBan = -1;

    Spinner spNgaySinh;
    ArrayList<String> dsNgaySinh;
    ArrayAdapter<String> adapterNgaySinh;
    int viTriSpNgaySinh = -1;

    Spinner spThangSinh;
    ArrayList<String> dsThangSinh;
    ArrayAdapter<String> adapterThangSinh;
    int viTriSpThangSinh = -1;

    Spinner spNamSinh;
    ArrayList<String> dsNamSinh;
    ArrayAdapter<String> adapterNamSinh;
    int viTriSpNamSinh = -1;

    Spinner spHonNhan;
    ArrayList<String> dsHonNhan;
    ArrayAdapter<String> adapterHonNhan;
    int viTriSpHonNhan = -1;


    Spinner spGioiTinh;
    ArrayList<String> dsGioiTinh;
    ArrayAdapter<String> adapterGioiTinh;
    int viTriSpGioiTinh = -1;

    int trangThai=0;


    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_nhan_vien);
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
        btnOptionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyOptionMenu(v);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyBack();
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyChange();

            }

        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyThem();
            }
        });

        spNgaySinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpNgaySinh = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spThangSinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpThangSinh = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spNamSinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpNamSinh = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spHonNhan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpHonNhan = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spPhongBan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpPhongBan = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spGioiTinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriSpGioiTinh = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void xuLyBack() {
        finish();
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
        popup.getMenuInflater().inflate(R.menu.menu_them_nhan_vien, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                xuLyOptionsItemSelected(item);
                return true;
            }
        });
        popup.show();
    }

    private void xuLyOptionsItemSelected(MenuItem item) {
        Cursor cursor = null;
        try {
            ArrayList<String> dsNhanVienDaXoa = new ArrayList<>();
            if (item.getItemId() == R.id.mnuNhanVienDaBiXoa) {
                cursor = database.rawQuery("select MSNV,HoTen from NhanVienBackup"
                        , null);
                while (cursor.moveToNext()) {
                    String maNhanVien = String.valueOf(cursor.getInt(0));
                    String hoTen = cursor.getString(1);
                    dsNhanVienDaXoa.add(maNhanVien + "-" + hoTen);
                }
                if (dsNhanVienDaXoa.size() > 0) {
                    trangThai++;
                    Intent i = new Intent(this, DanhSachNhanVienDaXoaActivity.class);
                    i.putExtra("DANH_SACH_NHAN_VIEN_DA_XOA", dsNhanVienDaXoa);
                    startActivity(i);
                }
            }
        } catch (Exception ex) {

        }
    }

    private void xuLyChange() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imgAvatar.setImageBitmap(selectedImage);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ThemNhanVienActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(ThemNhanVienActivity.this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        if(trangThai%2!=0) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String maNhanVien = prefs.getString("MA_NHAN_VIEN", null);
            String tenNhanVien = prefs.getString("TEN_NHAN_VIEN", null);
            if (maNhanVien != null && tenNhanVien != null) {
                xuLyThemThongTinNhanVienDaBiXoa(maNhanVien);
            }
            trangThai--;
        }
    }

    private void xuLyThem()  {
        try {
            Cursor cursor = database.rawQuery("select MSNV,CMND,Phone,Email from NhanVien", null);

            int mSNV, cMND, ngaySinh, thangSinh, namSinh;
            float heSoLuong;
            Bitmap bm = ((BitmapDrawable) imgAvatar.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 90, stream);
            byte[] hinhAvatar = stream.toByteArray();
            String hoTen, gioiTinh, phone, email, danToc, nguyenQuan, hoKhau, tonGiao, honNhan, phongBan;

            mSNV = Integer.parseInt(txtMaNhanVien.getText().toString());
            cMND = Integer.parseInt(txtCMND.getText().toString());
            ngaySinh = Integer.parseInt(dsNgaySinh.get(viTriSpNgaySinh));
            thangSinh = Integer.parseInt(dsThangSinh.get(viTriSpThangSinh));
            namSinh = Integer.parseInt(dsNamSinh.get(viTriSpNamSinh));

            hoTen = txtHoTen.getText().toString();
            gioiTinh = dsGioiTinh.get(viTriSpGioiTinh);
            phone = txtPhone.getText().toString();
            email = txtEmail.getText().toString();
            danToc = autotxtDanToc.getText().toString();
            nguyenQuan = txtNguyenQuan.getText().toString();
            hoKhau = txtHoKhau.getText().toString();
            tonGiao = txtTonGiao.getText().toString();
            honNhan = dsHonNhan.get(viTriSpHonNhan);
            phongBan = dsPhongBan.get(viTriSpPhongBan);

            if (phongBan.equals("Phòng Kỹ Thuật")) {
                heSoLuong = (float) 6.5;
            } else if (phongBan.equals("Phòng Tài Chính")) {
                heSoLuong = (float) 5;
            } else if (phone.equals("Phòng Kinh Doanh")) {
                heSoLuong = (float) 5.5;
            } else {
                heSoLuong = (float) 4.5;
            }

            int check = 0;

            if (mSNV < 7040000 || mSNV > 7049999) {
                Toast.makeText(this, "Mã số nhân viên không hợp lệ", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    if (cursor.getInt(cursor.getColumnIndex("MSNV")) == mSNV) {
                        check++;
                        Toast.makeText(this, "Đã tồn tại mã số nhân viên có mã số " + String.valueOf(mSNV), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (cursor.getInt(cursor.getColumnIndex("CMND")) == cMND) {
                        check++;
                        Toast.makeText(this, "Đã tồn tại nhân viên có CMND " + String.valueOf(cMND), Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (cursor.getString(cursor.getColumnIndex("Phone")).equals(phone)) {
                        check++;
                        Toast.makeText(this, "Đã tồn tại nhân viên có số phone " + phone, Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (cursor.getString(cursor.getColumnIndex("Email")).equals(email)) {
                        check++;
                        Toast.makeText(this, "Đã tồn tại nhân viên có email " + email, Toast.LENGTH_SHORT).show();
                        break;
                    }

                }
                if (check == 0) {
                    ContentValues values = new ContentValues();
                    values.put("MSNV", mSNV);
                    values.put("HoTen", hoTen);
                    values.put("NgaySinh", String.valueOf(ngaySinh) + "/" + String.valueOf(thangSinh) + "/"
                            + String.valueOf(namSinh));
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
                    values.put("Hinh", hinhAvatar);
                    values.put("HeSoLuong",heSoLuong);
                    //Them lich su lam viec
                    String lichSuLamViec="";
                    Calendar c= Calendar.getInstance();
                    int ngay=c.get(Calendar.DATE);
                    int thang=c.get(Calendar.MONTH);
                    int nam=c.get(Calendar.YEAR);
                    lichSuLamViec+="("+phongBan+"|"+ngay+"/"+thang+"/"+nam+")";
                    values.put("LichSuPhongBan",lichSuLamViec);
                    long add = database.insert("NhanVien", null, values);
                    if (add == -1) {
                        Toast.makeText(this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Cursor cursorXoaBackup=database.rawQuery("select * from NhanVienBackup where HoTen='"+hoTen+"'" +
                                " and CMND="+cMND,null);
                        while (cursorXoaBackup.moveToNext()){
                            database.delete("NhanVienBackup", "CMND=?", new String[]{txtCMND.getText().toString()});
                            break;
                        }
                        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();

                    }
                }

            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void addControls() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        btnOptionMenu = (ImageButton) findViewById(R.id.btnOptionMenuThemNhanVien);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnChange = (Button) findViewById(R.id.btnChange);
        btnThem = (Button) findViewById(R.id.btnThem);


        txtMaNhanVien = (EditText) findViewById(R.id.txtMaNhanVien);
        txtHoTen = (EditText) findViewById(R.id.txtHoTen);
        txtCMND = (EditText) findViewById(R.id.txtCMND);
        txtHoKhau = (EditText) findViewById(R.id.txtHoKhau);
        txtNguyenQuan = (EditText) findViewById(R.id.txtNguyenQuan);
        txtTonGiao = (EditText) findViewById(R.id.txtTonGiao);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtEmail = (EditText) findViewById(R.id.txtEmail);

        autotxtDanToc = (AutoCompleteTextView) findViewById(R.id.autotxtDanToc);
        arrDanToc = getResources().getStringArray(R.array.arrDanToc);
        adapterDanToc = new ArrayAdapter<String>(ThemNhanVienActivity.this, android.R.layout.simple_list_item_1, arrDanToc);
        autotxtDanToc.setAdapter(adapterDanToc);


        spGioiTinh = (Spinner) findViewById(R.id.spGioiTinh);
        spNgaySinh = (Spinner) findViewById(R.id.spNgaySinh);
        spThangSinh = (Spinner) findViewById(R.id.spThangSinh);
        spNamSinh = (Spinner) findViewById(R.id.spNamSinh);
        spPhongBan = (Spinner) findViewById(R.id.spPhongBan);
        spHonNhan = (Spinner) findViewById(R.id.spHonNhan);

        addDSNgaySinh();
        adapterNgaySinh = new ArrayAdapter<String>(ThemNhanVienActivity.this, android.R.layout.simple_spinner_item
                , dsNgaySinh);
        adapterNgaySinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNgaySinh.setAdapter(adapterNgaySinh);

        addDSTThangSinh();
        adapterThangSinh = new ArrayAdapter<String>(ThemNhanVienActivity.this, android.R.layout.simple_spinner_item
                , dsThangSinh);
        adapterThangSinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spThangSinh.setAdapter(adapterThangSinh);

        addDSNamSinh();
        adapterNamSinh = new ArrayAdapter<String>(ThemNhanVienActivity.this, android.R.layout.simple_spinner_item
                , dsNamSinh);
        adapterNamSinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNamSinh.setAdapter(adapterNamSinh);

        addDSPhongBan();
        adapterPhongBan = new ArrayAdapter<String>(ThemNhanVienActivity.this, android.R.layout.simple_list_item_checked
                , dsPhongBan);
        adapterPhongBan.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spPhongBan.setAdapter(adapterPhongBan);

        addDSHonNhan();
        adapterHonNhan = new ArrayAdapter<String>(ThemNhanVienActivity.this, android.R.layout.simple_list_item_checked
                , dsHonNhan);
        adapterHonNhan.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spHonNhan.setAdapter(adapterHonNhan);

        addDSGioiTinh();
        adapterGioiTinh = new ArrayAdapter<String>(ThemNhanVienActivity.this, android.R.layout.simple_list_item_checked,
                dsGioiTinh);
        adapterGioiTinh.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spGioiTinh.setAdapter(adapterGioiTinh);

        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);

    }

    private void xuLyThemThongTinNhanVienDaBiXoa(String maNhanVien) {
        try {
            Cursor cursor = database.rawQuery("select * from NhanVienBackup where MSNV=" + maNhanVien, null);
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
            txtMaNhanVien.setText(maNhanVien);
            txtHoTen.setText(hoTen);
            txtCMND.setText(String.valueOf(cMND));

            //Xu ly phong ban
            for (int i = 0; i < dsPhongBan.size(); i++)
                if (dsPhongBan.get(i).equals(phongBan))
                    viTriSpPhongBan = i;
            spPhongBan.setSelection(viTriSpPhongBan);

            txtPhone.setText(phone);
            txtEmail.setText(email);

            //Chuyen doi ngay sinh
            int length, numnam, numngay, numthang;
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
                } else {
                    thang += ngaySinh.charAt(2);
                    matcher = pattern.matcher(ngaySinh.charAt(3) + "");
                    if (matcher.matches())
                        thang += ngaySinh.charAt(3);
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

                //xu ly ngay sinh
                for (int i = 0; i < dsNgaySinh.size(); i++)
                    if (dsNgaySinh.get(i).equals(String.valueOf(numngay)))
                        viTriSpNgaySinh = i;
                spNgaySinh.setSelection(viTriSpNgaySinh);
                //xu ly thang sinh
                for (int i = 0; i < dsThangSinh.size(); i++)
                    if (dsThangSinh.get(i).equals(String.valueOf(numthang)))
                        viTriSpThangSinh = i;
                spThangSinh.setSelection(viTriSpThangSinh);

                //xu ly nam sinh
                for (int i = 0; i < dsNamSinh.size(); i++)
                    if (dsNamSinh.get(i).equals(String.valueOf(numnam)))
                        viTriSpNamSinh = i;
                spNamSinh.setSelection(viTriSpNamSinh);

                //xu ly gioi tinh
                for(int i=0; i<dsGioiTinh.size(); i++)
                    if (dsGioiTinh.get(i).equals(gioiTinh))
                        viTriSpGioiTinh=i;
                spGioiTinh.setSelection(viTriSpGioiTinh);

                autotxtDanToc.setText(danToc);
                txtHoKhau.setText(hoKhau);
                txtNguyenQuan.setText(nguyenQuan);
                txtTonGiao.setText(tonGiao);
                //xu ly hon nhan
                for (int i=0; i<dsHonNhan.size(); i++)
                    if(dsHonNhan.get(i).equals(honNhan))
                        viTriSpHonNhan=i;
                spHonNhan.setSelection(viTriSpHonNhan);
                if (byteImage == null)
                    imgAvatar.setImageResource(R.drawable.imgavatar);
                else {
                    Bitmap bmImage = BitmapFactory.decodeByteArray(byteImage, 0,byteImage.length);
                    imgAvatar.setImageBitmap(bmImage);
                }
            }
            cursor.close();
        } catch (Exception ex) {

        }
    }

    private void addDSGioiTinh() {
        dsGioiTinh = new ArrayList<>();
        dsGioiTinh.add("Nam");
        dsGioiTinh.add("Nữ");
    }

    private void addDSHonNhan() {
        dsHonNhan = new ArrayList<>();
        dsHonNhan.add("Chưa kết hôn");
        dsHonNhan.add("Đã kết hôn");
        dsHonNhan.add("Không rõ");
    }


    private void addDSPhongBan() {
        dsPhongBan = new ArrayList<>();
        dsPhongBan.add("Phòng Tài Chính");
        dsPhongBan.add("Phòng Kỹ Thuật");
        dsPhongBan.add("Phòng Nhân Sự");
        dsPhongBan.add("Phòng Kinh Doanh");
    }

    private void addDSNgaySinh() {
        dsNgaySinh = new ArrayList<>();

        for (int i = 0; i < 31; i++)
            dsNgaySinh.add(String.valueOf((i + 1)));
    }

    private void addDSNamSinh() {
        dsNamSinh = new ArrayList<>();
        for (int i = 1949; i < 1999; i++)
            dsNamSinh.add(String.valueOf((i + 1)));

    }

    private void addDSTThangSinh() {
        dsThangSinh = new ArrayList<>();
        for (int i = 0; i < 12; i++)
            dsThangSinh.add(String.valueOf((i + 1)));


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
