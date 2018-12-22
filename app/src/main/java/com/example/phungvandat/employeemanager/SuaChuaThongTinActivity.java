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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import com.example.phungvandat.model.NhanVien;

public class SuaChuaThongTinActivity extends Activity {
    ImageButton btnBack;

    ImageView imgAvatar;
    Button btnLuu;
    EditText txtHoKhau, txtNguyenQuan, txtTonGiao, txtPhone;
    TextView txtMaNhanVien;
    TextView txtHoTen;
    TextView txtCMND;

    EditText txtEmail;

    AutoCompleteTextView autotxtDanToc;
    String[] arrDanToc;
    ArrayAdapter<String> adapterDanToc;

    Spinner spGioiTinh;
    ArrayList<String> dsGioiTinh;
    ArrayAdapter<String> adapterGioiTinh;
    int viTriGioiTinh;

    Spinner spPhongBan;
    ArrayList<String> dsPhongBan;
    ArrayAdapter<String> adapterPhongBan;
    int viTriPhongBan;

    Spinner spNgaySinh;
    ArrayList<String> dsNgaySinh;
    ArrayAdapter<String> adapterNgaySinh;
    int viTriNgaySinh;

    Spinner spThangSinh;
    ArrayList<String> dsThangSinh;
    ArrayAdapter<String> adapterThangSinh;
    int viTriThangSinh;

    Spinner spNamSinh;
    ArrayList<String> dsNamSinh;
    ArrayAdapter<String> adapterNamSinh;
    int viTriNamSinh;

    Spinner spHonNhan;
    ArrayList<String> dsHonNhan;
    ArrayAdapter<String> adapterHonNhan;
    int viTriHonNhan;

    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_chua_thong_tin);
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
                Toast.makeText(SuaChuaThongTinActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

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
                finish();
            }
        });
        spNgaySinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriNgaySinh = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spThangSinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriThangSinh = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spNamSinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriNamSinh = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spHonNhan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriHonNhan = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spPhongBan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriPhongBan = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spGioiTinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTriGioiTinh = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateARow();
            }
        });

    }

    private void updateARow() {
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("select MSNV,CMND,Phone,Email,LichSuPhongBan from NhanVien", null);

        int mSNV, ngaySinh, thangSinh, namSinh;
        float heSoLuong;


        String gioiTinh, phone, email, danToc, nguyenQuan, hoKhau, tonGiao, honNhan, phongBan;
        mSNV = Integer.parseInt(txtMaNhanVien.getText().toString());
        ngaySinh = Integer.parseInt(dsNgaySinh.get(viTriNgaySinh));
        thangSinh = Integer.parseInt(dsThangSinh.get(viTriThangSinh));
        namSinh = Integer.parseInt(dsNamSinh.get(viTriNamSinh));

        gioiTinh = dsGioiTinh.get(viTriGioiTinh);
        phone = txtPhone.getText().toString();
        email = txtEmail.getText().toString();
        danToc = autotxtDanToc.getText().toString();
        nguyenQuan = txtNguyenQuan.getText().toString();
        hoKhau = txtHoKhau.getText().toString();
        tonGiao = txtTonGiao.getText().toString();
        honNhan = dsHonNhan.get(viTriHonNhan);
        phongBan = dsPhongBan.get(viTriPhongBan);

        if (phongBan.equals("Phòng Kỹ Thuật")) {
            heSoLuong = (float) 6.5;
        } else if (phongBan.equals("Phòng Tài Chính")) {
            heSoLuong = (float) 5;
        } else if (phone.equals("Phòng Kinh Doanh")) {
            heSoLuong = (float) 5.5;
        } else {
            heSoLuong = (float) 4.5;
        }

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

        while (cursor.moveToNext()) {
            Intent i = getIntent();
            NhanVien nhanVien = (NhanVien) i.getSerializableExtra("KIEU_NHAN_VIEN");
            String oldPhone = nhanVien.getSoPhone();
            String oldEmail = nhanVien.getEmail();

            if (!oldPhone.equals(phone)) {
                if (cursor.getString(cursor.getColumnIndex("Phone")).equals(phone)) {
                    check++;
                    Toast.makeText(this, "Đã tồn tại nhân viên có số phone " + phone, Toast.LENGTH_SHORT).show();
                    break;
                }
            }

            if (!oldEmail.equals(email)) {

                if (cursor.getString(cursor.getColumnIndex("Email")).equals(email)) {
                    check++;
                    Toast.makeText(this, "Đã tồn tại nhân viên có email " + email, Toast.LENGTH_SHORT).show();
                    break;
                }
            }

        }
        if (check == 0) {
            ContentValues values = new ContentValues();
            values.put("NgaySinh", String.valueOf(ngaySinh) + "/" + String.valueOf(thangSinh) + "/"
                    + String.valueOf(namSinh));
            values.put("GioiTinh", gioiTinh);
            values.put("Phone", phone);
            values.put("Email", email);
            values.put("DanToc", danToc);
            values.put("NguyenQuan", nguyenQuan);
            values.put("HoKhau", hoKhau);
            values.put("TonGiao", tonGiao);
            values.put("HonNhan", honNhan);
            values.put("PhongBan", phongBan);

            String lichSuPhongBan="";
            Cursor cursorChekcLichSuPhongBan=database.rawQuery("select PhongBan,LichSuPhongBan from NhanVien where MSNV="+txtMaNhanVien.getText().toString(),null);
            int checkLichSuPhongBan=0;
            while (cursorChekcLichSuPhongBan.moveToNext()){
                lichSuPhongBan=cursorChekcLichSuPhongBan.getString(1);
                if(!cursorChekcLichSuPhongBan.getString(0).equals(dsPhongBan.get(viTriPhongBan)))
                    checkLichSuPhongBan++;
            }
            if (checkLichSuPhongBan>0)
            {
                lichSuPhongBan+="("+dsPhongBan.get(viTriPhongBan)+"|";
                Calendar c= Calendar.getInstance();
                int ngay= c.get(Calendar.DATE);
                int thang=c.get(Calendar.MONTH);
                int nam=c.get(Calendar.YEAR);
                lichSuPhongBan+=String.valueOf(ngay)+"/"+String.valueOf(thang)+"/"+String.valueOf(nam)+")";
                values.put("LichSuPhongBan",lichSuPhongBan);
            }

            String[] dieuKien = {String.valueOf(mSNV)};
            int ret = database.update("NhanVien", values, "MSNV=?", dieuKien);
            if (ret == 0) {
                Toast.makeText(this, "Update không thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Update thành công", Toast.LENGTH_SHORT).show();
            }
        }
        cursor.close();
    }

    private void addControls() {
        btnBack= (ImageButton) findViewById(R.id.btnBack);
        btnLuu = (Button) findViewById(R.id.btnLuu);

        txtHoTen = (TextView) findViewById(R.id.txtHoTen);
        txtCMND = (TextView) findViewById(R.id.txtCMND);


        txtMaNhanVien = (TextView) findViewById(R.id.txtMaNhanVien);
        txtHoKhau = (EditText) findViewById(R.id.txtHoKhau);
        txtNguyenQuan = (EditText) findViewById(R.id.txtNguyenQuan);
        txtTonGiao = (EditText) findViewById(R.id.txtTonGiao);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtEmail = (EditText) findViewById(R.id.txtEmail);

        autotxtDanToc = (AutoCompleteTextView) findViewById(R.id.autotxtDanToc);
        arrDanToc = getResources().getStringArray(R.array.arrDanToc);
        adapterDanToc = new ArrayAdapter<String>(SuaChuaThongTinActivity.this, android.R.layout.simple_list_item_1, arrDanToc);
        autotxtDanToc.setAdapter(adapterDanToc);


        spGioiTinh = (Spinner) findViewById(R.id.spGioiTinh);
        spNgaySinh = (Spinner) findViewById(R.id.spNgaySinh);
        spThangSinh = (Spinner) findViewById(R.id.spThangSinh);
        spNamSinh = (Spinner) findViewById(R.id.spNamSinh);
        spPhongBan = (Spinner) findViewById(R.id.spPhongBan);
        spHonNhan = (Spinner) findViewById(R.id.spHonNhan);

        addDSGioiTinh();
        adapterGioiTinh = new ArrayAdapter<String>(SuaChuaThongTinActivity.this, android.R.layout.simple_list_item_checked,
                dsGioiTinh);
        adapterGioiTinh.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spGioiTinh.setAdapter(adapterGioiTinh);

        addDSNgaySinh();
        adapterNgaySinh = new ArrayAdapter<String>(SuaChuaThongTinActivity.this, android.R.layout.simple_spinner_item
                , dsNgaySinh);
        adapterNgaySinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNgaySinh.setAdapter(adapterNgaySinh);

        addDSTThangSinh();
        adapterThangSinh = new ArrayAdapter<String>(SuaChuaThongTinActivity.this, android.R.layout.simple_spinner_item
                , dsThangSinh);
        adapterThangSinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spThangSinh.setAdapter(adapterThangSinh);

        addDSNamSinh();
        adapterNamSinh = new ArrayAdapter<String>(SuaChuaThongTinActivity.this, android.R.layout.simple_spinner_item
                , dsNamSinh);
        adapterNamSinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNamSinh.setAdapter(adapterNamSinh);

        addDSPhongBan();
        adapterPhongBan = new ArrayAdapter<String>(SuaChuaThongTinActivity.this, android.R.layout.simple_list_item_checked
                , dsPhongBan);
        adapterPhongBan.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spPhongBan.setAdapter(adapterPhongBan);

        addDSHonNhan();
        adapterHonNhan = new ArrayAdapter<String>(SuaChuaThongTinActivity.this, android.R.layout.simple_list_item_checked
                , dsHonNhan);
        adapterHonNhan.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spHonNhan.setAdapter(adapterHonNhan);


        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);


        Intent i = getIntent();

        NhanVien nhanVien = (NhanVien) i.getSerializableExtra("KIEU_NHAN_VIEN");


        if (nhanVien.getHinhAnh() == null) {
            imgAvatar.setImageResource(R.drawable.imgavatar);
        } else {
            Bitmap bmImage = BitmapFactory.decodeByteArray(nhanVien.getHinhAnh(), 0, nhanVien.getHinhAnh().length);
            imgAvatar.setImageBitmap(bmImage);
        }
        txtHoTen.setText(nhanVien.getHoTen());
        txtCMND.setText(String.valueOf(nhanVien.getSoCMND()));
        txtMaNhanVien.setText(nhanVien.getMaNhanVien());
        String email = nhanVien.getEmail();
        txtEmail.setText(email);
        for (int viTri = 0; viTri < dsGioiTinh.size(); viTri++) {
            if (dsGioiTinh.get(viTri).equals(nhanVien.getGioiTinh())) {
                viTriGioiTinh = viTri;
                break;
            }
        }
        spGioiTinh.setSelection(viTriGioiTinh);

        for (int viTri = 0; viTri < dsPhongBan.size(); viTri++)
            if (dsPhongBan.get(viTri).equals(nhanVien.getPhongBanTrucThuoc())) {
                viTriPhongBan = viTri;
                break;
            }
        spPhongBan.setSelection(viTriPhongBan);
        txtPhone.setText(nhanVien.getSoPhone());

        for (int viTri = 0; viTri < dsNgaySinh.size(); viTri++)
            if (dsNgaySinh.get(viTri).equals(String.valueOf(nhanVien.getNgaySinh()))) {
                viTriNgaySinh = viTri;
                break;
            }
        spNgaySinh.setSelection(viTriNgaySinh);

        for (int viTri = 0; viTri < dsThangSinh.size(); viTri++)
            if (dsThangSinh.get(viTri).equals(String.valueOf(nhanVien.getThangSinh()))) {
                viTriThangSinh = viTri;
                break;
            }
        spThangSinh.setSelection(viTriThangSinh);

        for (int viTri = 0; viTri < dsNamSinh.size(); viTri++)
            if (dsNamSinh.get(viTri).equals(String.valueOf(nhanVien.getNamSinh()))) {
                viTriNamSinh = viTri;
                break;
            }
        spNamSinh.setSelection(viTriNamSinh);


        autotxtDanToc.setText(nhanVien.getDanToc());
        txtHoKhau.setText(nhanVien.getHoKhau());
        txtNguyenQuan.setText(nhanVien.getNguyenQuan());
        txtTonGiao.setText(nhanVien.getTonGiao());

        for (int viTri = 0; viTri < dsHonNhan.size(); viTri++)
            if (dsHonNhan.get(viTri).equals(String.valueOf(nhanVien.getTinhTrangHonNhan()))) {
                viTriHonNhan = viTri;
                break;
            }
        spHonNhan.setSelection(viTriHonNhan);


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
