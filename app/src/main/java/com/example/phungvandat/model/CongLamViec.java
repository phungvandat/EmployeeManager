package com.example.phungvandat.model;

import java.io.Serializable;
import java.io.StringReader;

/**
 * Created by PHUNGVANDAT on 3/17/2018.
 */
public class CongLamViec implements Serializable {
    private String maNhanVien;
    private int ngay;
    private String trangThai;
    private String gioRa;
    private String gioVao;

    public CongLamViec(String maNhanVien, int ngay, String trangThai, String gioVao, String gioRa) {
        this.maNhanVien = maNhanVien;
        this.ngay = ngay;
        this.trangThai = trangThai;
        this.gioRa = gioRa;
        this.gioVao = gioVao;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public int getNgay() {
        return ngay;
    }

    public void setNgay(int ngay) {
        this.ngay = ngay;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getGioRa() {
        return gioRa;
    }

    public void setGioRa(String gioRa) {
        this.gioRa = gioRa;
    }

    public String getGioVao() {
        return gioVao;
    }

    public void setGioVao(String gioVao) {
        this.gioVao = gioVao;
    }
}
