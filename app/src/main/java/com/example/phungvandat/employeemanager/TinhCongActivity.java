package com.example.phungvandat.employeemanager;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.phungvandat.model.CongLamViec;

public class TinhCongActivity extends Activity {

    TextView txtGuiThongTinLuong;
    ImageButton btnBack;

    AutoCompleteTextView txtMaNhanVien;
    String[] dsMaNhanVien;
    ArrayAdapter<String> adapterMaNhanVien;

    Spinner spThang;
    ArrayList<String> dsThang;
    ArrayAdapter<String> adapterThang;
    int viTrispThang = -1;

    Spinner spNam;
    ArrayList<String> dsNam;
    ArrayAdapter<String> adapterNam;
    int viTrispNam = -1;

    Button btnHienThi;

    ArrayList<CongLamViec> dsCongLamViec;

    TextView txtTongCong, txtHeSoLuong, txtTongLuong, txtThang;

    TableLayout tbTinhCong;

    String DATABASE_NAME = "dbNhanVien.sqlite";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;

    int quyenTruyCap = 1;
    String MSNV = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinh_cong);
        xuLySaoChepCSDLTuAssetsVaoHeThongMobile();
        addControls();
        addEvents();

    }

    private void xuLySaoChepCSDLTuAssetsVaoHeThongMobile() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAcsset();
                Toast.makeText(TinhCongActivity.this, "Sao chep database thanh cong", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(TinhCongActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

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
        txtGuiThongTinLuong= (TextView) findViewById(R.id.txtGuiThongTinLuong);
        btnBack= (ImageButton) findViewById(R.id.btnBack);
        quyenTruyCap = getIntent().getIntExtra("QUYEN_TRUY_CAP", quyenTruyCap);
        MSNV = getIntent().getStringExtra("MSNV");

        txtMaNhanVien = (AutoCompleteTextView) findViewById(R.id.txtMaNhanVien);
        addDanhSachMaNhanVien();
        adapterMaNhanVien = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dsMaNhanVien);
        txtMaNhanVien.setAdapter(adapterMaNhanVien);

        spThang = (Spinner) findViewById(R.id.spThang);
        addDanhSachThang();
        adapterThang = new ArrayAdapter<String>(TinhCongActivity.this, android.R.layout.simple_spinner_item, dsThang);
        adapterThang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spThang.setAdapter(adapterThang);

        spNam = (Spinner) findViewById(R.id.spNam);
        addDanhSachNam();
        adapterNam = new ArrayAdapter<String>(TinhCongActivity.this, android.R.layout.simple_spinner_item, dsNam);
        adapterNam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNam.setAdapter(adapterNam);

        btnHienThi = (Button) findViewById(R.id.btnHienThi);

        txtTongCong = (TextView) findViewById(R.id.txtTongCong);
        txtHeSoLuong = (TextView) findViewById(R.id.txtHeSoLuong);
        txtTongLuong = (TextView) findViewById(R.id.txtTongLuong);
        txtThang = (TextView) findViewById(R.id.txtThang);

        tbTinhCong = (TableLayout) findViewById(R.id.tbTinhCong);
    }

    private void addEvents() {
        txtGuiThongTinLuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyGuiThongTinLuong();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnHienThi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyHienThi();
            }
        });
        spNam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTrispNam = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spThang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viTrispThang = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void xuLyGuiThongTinLuong() {
        try {
            Cursor cursor= database.rawQuery("select Phone from NhanVien where MSNV="+txtMaNhanVien.getText().toString(),null);
            String soDienThoaiNhanVien = null;
            while (cursor.moveToNext())
                soDienThoaiNhanVien = cursor.getString(0);
            //lấy mặc định SmsManager
            final SmsManager sms = SmsManager.getDefault();
            Intent msgSent = new Intent("ACTION_MSG_SENT");
            //Khai báo pendingintent để kiểm tra kết quả
            final PendingIntent pendingMsgSent =
                    PendingIntent.getBroadcast(this, 0, msgSent, 0);
            registerReceiver(new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    int result = getResultCode();
                    String s = "Send OK";
                    if (result != Activity.RESULT_OK) {
                        s = "Send failed";
                    }
                    Toast.makeText(TinhCongActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }, new IntentFilter("ACTION_MSG_SENT"));
            //Gọi hàm gửi tin nhắn đi
            sms.sendTextMessage(soDienThoaiNhanVien, null, ("Lương " +dsThang.get(viTrispThang)
                    +"/"+dsNam.get(viTrispNam)+" của nhân viên " + txtMaNhanVien.getText().toString()+
                    " là: ").toString() + (txtTongLuong.getText().toString()).toString() + "USD".toString(),
                    pendingMsgSent, null);
        }
        catch (Exception ex){

        }
    }

    private void xuLyHienThi() {
        addDanhSachTinhCong();
        txtThang.setText(dsThang.get(viTrispThang));
        try {
            for (int i = 1; i <= dsCongLamViec.size(); i++) {
                TableRow tbrTinhCong;
                tbrTinhCong = new TableRow(this);
                TextView textTBNgay;
                TextView textTBGioVao;
                TextView textTBGioRa;
                TextView textTBTrangThai;
                textTBNgay = new TextView(this);
                textTBNgay.setBackgroundResource(R.drawable.item_table);
                textTBNgay.setGravity(Gravity.CENTER);
                textTBGioVao = new TextView(this);
                textTBGioVao.setBackgroundResource(R.drawable.item_table);
                textTBGioVao.setGravity(Gravity.CENTER);
                textTBGioRa = new TextView(this);
                textTBGioRa.setBackgroundResource(R.drawable.item_table);
                textTBGioRa.setGravity(Gravity.CENTER);
                textTBTrangThai = new TextView(this);
                textTBTrangThai.setBackgroundResource(R.drawable.item_table);
                textTBTrangThai.setGravity(Gravity.CENTER);
                textTBNgay.setText(String.valueOf(dsCongLamViec.get(i - 1).getNgay()));
                textTBGioVao.setText(dsCongLamViec.get(i - 1).getGioVao());
                textTBGioRa.setText(dsCongLamViec.get(i - 1).getGioRa());
                textTBTrangThai.setText(dsCongLamViec.get(i - 1).getTrangThai());
                tbrTinhCong.addView(textTBNgay);
                tbrTinhCong.addView(textTBGioVao);
                tbrTinhCong.addView(textTBGioRa);
                tbrTinhCong.addView(textTBTrangThai);
                tbTinhCong.addView(tbrTinhCong, i);
            }
        } catch (Exception ex) {

        }

    }

    private void addDanhSachMaNhanVien() {
        if (quyenTruyCap == 1) {
            database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            Cursor cursor;
            try {
                cursor = database.rawQuery("select MSNV,HoTen from NhanVien", null);
                dsMaNhanVien = new String[cursor.getCount()];
                int i = 0;
                while (cursor.moveToNext()) {
                    dsMaNhanVien[i] = String.valueOf(cursor.getInt(0));
                    i++;
                }
                cursor.close();
            } catch (Exception ex) {
            }
        } else {
            dsMaNhanVien = new String[1];
            dsMaNhanVien[0] = MSNV;
        }
    }

    private void addDanhSachThang() {
        dsThang = new ArrayList<>();
        for (int i = 0; i < 12; i++)
            dsThang.add(String.valueOf((i + 1)));


    }

    private void addDanhSachNam() {
        dsNam = new ArrayList<>();
        dsNam.add("2017");
        dsNam.add("2018");
    }

    private void addDanhSachTinhCong() {
        dsCongLamViec = new ArrayList<>();

        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = null;

        int tongCong = 0;
        float heSoLuong = 0;
        float TongLuong = 0;

        try {

            Cursor ckt = database.rawQuery("select MSNV from NhanVien", null);
            int check = 0;
            while (ckt.moveToNext()) {
                int msnv_DB = ckt.getInt(0);
                if (Integer.parseInt(txtMaNhanVien.getText().toString()) == msnv_DB) {
                    check = 1;
                    break;
                }
            }
            if (check == 0) {
                Toast.makeText(this, "Không tồn tại nhân viên có mã số " + txtMaNhanVien.getText().toString(), Toast.LENGTH_SHORT).show();
                return;
            }
            if (!txtMaNhanVien.getText().toString().equals(MSNV) && quyenTruyCap==0) {
                Toast.makeText(this, "Bạn không có quyền xem thông tin tính công của nhân viên " + txtMaNhanVien.getText().toString(),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            cursor = database.rawQuery("select MSNV_TC,Ngay,TrangThai,GioVao,GioRa,NgayVaoLam,NgayThoiViec,HeSoLuong " +
                            "from TinhCong,NhanVien,HopDongViecLam " +
                            "where MSNV_TC=MSNV and MSNV=MSNV_HD and MSNV_TC=" + txtMaNhanVien.getText().toString() +
                            " and Nam=" + dsNam.get(viTrispNam) + " and Thang=" + dsThang.get(viTrispThang)
                    , null);
            if (cursor.getCount() == 0) {
                Cursor cursor1 = database.rawQuery("select MSNV_HD,NgayVaoLam,NgayThoiViec" +
                        " from HopDongViecLam " +
                        "where  MSNV_HD=" + txtMaNhanVien.getText().toString(), null);
                int thang = Integer.parseInt(dsThang.get(viTrispThang));
                int nam = Integer.parseInt(dsNam.get(viTrispNam));
                String ngayVaoLam = "";
                String thangVao, namVao;
                thangVao = "";
                namVao = "";
                int numthangVao, numnamVao;
                numnamVao = numthangVao = 0;

                String ngayThoiViec = "";
                String thangNghi, namNghi;
                thangNghi = "";
                namNghi = "";
                int numthangNghi, numnamNghi;
                numthangNghi = numnamNghi = 0;

                if (cursor1.moveToFirst()) {
                    ngayVaoLam = cursor1.getString(1);
                    Pattern pattern = Pattern.compile("\\d*");
                    Matcher matcher = pattern.matcher(ngayVaoLam.charAt(1) + "");

                    if (matcher.matches()) {
                        thangVao += ngayVaoLam.charAt(3);
                        matcher = pattern.matcher(ngayVaoLam.charAt(4) + "");
                        if (matcher.matches())
                            thangVao += ngayVaoLam.charAt(4);
                    } else {
                        thangVao += ngayVaoLam.charAt(2);
                        matcher = pattern.matcher(ngayVaoLam.charAt(3) + "");
                        if (matcher.matches())
                            thangVao += ngayVaoLam.charAt(3);

                    }
                    int lengthVao = ngayVaoLam.length();
                    namVao += ngayVaoLam.charAt(lengthVao - 4);
                    namVao += ngayVaoLam.charAt(lengthVao - 3);
                    namVao += ngayVaoLam.charAt(lengthVao - 2);
                    namVao += ngayVaoLam.charAt(lengthVao - 1);
                    try {
                        numthangVao = Integer.parseInt(thangVao);
                        numnamVao = Integer.parseInt(namVao);
                    } catch (Exception e) {
                    }
                    if (cursor1.getString(2) != null) {
                        ngayThoiViec = cursor1.getString(2);
                        Pattern patternNghi = Pattern.compile("\\d*");
                        Matcher matcherNghi = patternNghi.matcher(ngayThoiViec.charAt(1) + "");
                        if (matcherNghi.matches()) {
                            thangNghi += ngayThoiViec.charAt(3);
                            matcherNghi = patternNghi.matcher(ngayThoiViec.charAt(4) + "");
                            if (matcherNghi.matches())
                                thangNghi += ngayThoiViec.charAt(4);

                        } else {
                            thangNghi += ngayThoiViec.charAt(2);
                            matcherNghi = patternNghi.matcher(ngayThoiViec.charAt(3) + "");
                            if (matcherNghi.matches())
                                thangNghi += ngayThoiViec.charAt(3);
                        }

                        int lengthNghi = ngayThoiViec.length();
                        namNghi += ngayThoiViec.charAt(lengthNghi - 4);
                        namNghi += ngayThoiViec.charAt(lengthNghi - 3);
                        namNghi += ngayThoiViec.charAt(lengthNghi - 2);
                        namNghi += ngayThoiViec.charAt(lengthNghi - 1);

                        try {
                            numthangNghi = Integer.parseInt(thangNghi);
                            numnamNghi = Integer.parseInt(namNghi);
                        } catch (Exception e) {
                        }

                    }
                    if (numnamNghi == 0) {
                        numthangNghi = numthangVao + 1;
                        numnamNghi = numnamVao + 1;
                    }
                    if (nam < numnamVao || nam > numnamNghi) {
                        if (nam < numnamVao) {
                            Toast.makeText(this, "Nhân viên bắt đầu làm " + cursor1.getString(1), Toast.LENGTH_SHORT).show();
                        } else if (nam > numnamNghi) {
                            Toast.makeText(this, "Nhân viên đã nghỉ việc " + cursor1.getString(2), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        if (thang < numthangVao || thang > numthangNghi) {
                            if (thang < numthangVao) {
                                Toast.makeText(this, "Nhân viên bắt đầu làm " + cursor1.getString(1), Toast.LENGTH_SHORT).show();
                            } else if (thang > numthangNghi) {
                                Toast.makeText(this, "Nhân viên đã nghỉ việc " + cursor1.getString(2), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Chưa có dữ liệu", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {
                while (cursor.moveToNext()) {
                    if (cursor.getString(3) == null) {
                        break;
                    }
                    String maNhanVien = String.valueOf(cursor.getInt(0));
                    int ngayLV = cursor.getInt(1);
                    int numtrangThai = cursor.getInt(2);
                    String trangThai = "";
                    if (numtrangThai == 1) {
                        trangThai += "Làm việc";
                    } else {
                        trangThai = "Nghỉ việc";
                    }
                    String gioVao = cursor.getString(3);
                    String gioRa = cursor.getString(4);

                    dsCongLamViec.add(new CongLamViec(maNhanVien, ngayLV, trangThai, gioVao, gioRa));
                    if (cursor.getInt(2) == 1) {
                        tongCong++;
                    }
                    heSoLuong = cursor.getFloat(7);
                }
            }
            ckt.close();
            cursor.close();
        } catch (Exception ex) {

        }
        txtTongCong.setText(String.valueOf(tongCong));
        txtHeSoLuong.setText(String.valueOf(heSoLuong));
        TongLuong = (float) tongCong * heSoLuong * 5;
        txtTongLuong.setText(String.valueOf(TongLuong) + " USD");


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
