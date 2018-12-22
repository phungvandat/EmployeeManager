package com.example.phungvandat.employeemanager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phungvandat.model.NhanVien;

public class HienThiThongTinActivity extends Activity {

    ImageView imgAvatar;
    Button btnDong;
    TextView txtMaNhanVien, txtHoTen, txtCMND, txtHoKhau, txtNguyenQuan, txtTonGiao, txtPhone, txtEmail;
    TextView txtDanToc, txtPhongBan, txtNgaySinh, txtHonNhan, txtGioiTinh;
    ImageButton btnCall, btnSMS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hien_thi_thong_tin);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyCall();
            }
        });
        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLySMS();
            }
        });
    }

    private void xuLySMS() {
        Intent i = new Intent(this, SMSActivity.class);
        i.putExtra("SO_PHONE", txtPhone.getText().toString());
        startActivity(i);
    }

    private void xuLyCall() {
        Intent i = new Intent(Intent.ACTION_CALL);
        Uri uri = Uri.parse("tel:" + txtPhone.getText().toString());
        i.setData(uri);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(i);
    }

    private void addControls() {


        txtDanToc = (TextView) findViewById(R.id.txtDanToc);
        txtPhongBan = (TextView) findViewById(R.id.txtPhongBan);
        txtNgaySinh = (TextView) findViewById(R.id.txtNgaySinh);
        txtHonNhan = (TextView) findViewById(R.id.txtHonNhan);
        txtGioiTinh = (TextView) findViewById(R.id.txtGioiTinh);
        txtPhone = (TextView) findViewById(R.id.txtPhone);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtTonGiao = (TextView) findViewById(R.id.txtTonGiao);
        txtNguyenQuan = (TextView) findViewById(R.id.txtNguyenQuan);
        txtHoKhau = (TextView) findViewById(R.id.txtHoKhau);
        txtCMND = (TextView) findViewById(R.id.txtCMND);
        txtHoTen = (TextView) findViewById(R.id.txtHoTen);
        txtMaNhanVien = (TextView) findViewById(R.id.txtMaNhanVien);
        btnDong = (Button) findViewById(R.id.btnDong);
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);


        Intent i = getIntent();
        NhanVien nhanVien = (NhanVien) i.getSerializableExtra("KIEU_NHAN_VIEN");

        txtMaNhanVien.setText(nhanVien.getMaNhanVien());
        txtHoTen.setText(nhanVien.getHoTen());
        txtCMND.setText(String.valueOf(nhanVien.getSoCMND()));
        txtHoKhau.setText(nhanVien.getHoKhau());
        txtNguyenQuan.setText(nhanVien.getNguyenQuan());
        txtTonGiao.setText(nhanVien.getTonGiao());
        txtPhone.setText(nhanVien.getSoPhone());
        txtEmail.setText(nhanVien.getEmail());
        txtDanToc.setText(nhanVien.getDanToc());
        txtPhongBan.setText(nhanVien.getPhongBanTrucThuoc());
        txtNgaySinh.setText(String.valueOf(nhanVien.getNgaySinh()) + "/" + String.valueOf(nhanVien.getThangSinh())
                + "/" + String.valueOf(nhanVien.getNamSinh()));
        txtHonNhan.setText(nhanVien.getTinhTrangHonNhan());
        txtGioiTinh.setText(nhanVien.getGioiTinh());

        if (nhanVien.getHinhAnh() == null)
            imgAvatar.setImageResource(R.drawable.imgavatar);
        else {
            Bitmap bmImage = BitmapFactory.decodeByteArray(nhanVien.getHinhAnh(), 0, nhanVien.getHinhAnh().length);
            imgAvatar.setImageBitmap(bmImage);
        }

        btnCall = (ImageButton) findViewById(R.id.btnCall);
        btnSMS = (ImageButton) findViewById(R.id.btnSMS);


    }

}
