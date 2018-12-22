package com.example.phungvandat.model;

import java.io.Serializable;

/**
 * Created by PHUNGVANDAT on 3/19/2018.
 */
public class HopDong implements Serializable {
    private String maNhanVien;
    private String ngayVaoLam;
    private String ngayThoiViec;
    private String trangThaiHopDong;

    public HopDong(String maNhanVien, String ngayVaoLam, String ngayThoiViec, String trangThaiHopDong) {
        this.maNhanVien = maNhanVien;
        this.ngayVaoLam = ngayVaoLam;
        this.ngayThoiViec = ngayThoiViec;
        this.trangThaiHopDong = trangThaiHopDong;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(String ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public String getNgayThoiViec() {
        return ngayThoiViec;
    }

    public void setNgayThoiViec(String ngayThoiViec) {
        this.ngayThoiViec = ngayThoiViec;
    }

    public String getTrangThaiHopDong() {
        return trangThaiHopDong;
    }

    public void setTrangThaiHopDong(String trangThaiHopDong) {
        this.trangThaiHopDong = trangThaiHopDong;
    }
}
