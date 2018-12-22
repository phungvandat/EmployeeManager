package com.example.phungvandat.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by PHUNGVANDAT on 3/10/2018.
 */
public class NhanVien implements Serializable {
    protected String maNhanVien;
    protected String hoTen;
    protected int ngaySinh;
    protected int thangSinh;
    protected int namSinh;
    protected String gioiTinh;
    protected int soCMND;
    protected String soPhone;
    protected String email;
    protected String danToc;
    protected String nguyenQuan;
    protected String hoKhau;
    protected String tonGiao;
    protected String tinhTrangHonNhan;
    protected String phongBanTrucThuoc;
    protected float heSoLuong;
    protected byte[] hinhAnh;
    protected String lichSuPhongBan;

    public NhanVien(String maNhanVien, String hoTen, int ngaySinh, int thangSinh, int namSinh,
                    String gioiTinh, int soCMND, String soPhone, String email, String danToc,
                    String nguyenQuan, String hoKhau, String tonGiao, String tinhTrangHonNhan,
                    String phongBanTrucThuoc, float heSoLuong, byte[] hinhAnh,String lichSuPhongBan) {
        this.maNhanVien = maNhanVien;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.thangSinh = thangSinh;
        this.namSinh = namSinh;
        this.gioiTinh = gioiTinh;
        this.soCMND = soCMND;
        this.soPhone = soPhone;
        this.email = email;
        this.danToc = danToc;
        this.nguyenQuan = nguyenQuan;
        this.hoKhau = hoKhau;
        this.tonGiao = tonGiao;
        this.tinhTrangHonNhan = tinhTrangHonNhan;
        this.phongBanTrucThuoc = phongBanTrucThuoc;
        this.heSoLuong = heSoLuong;
        this.hinhAnh= hinhAnh;
        this.lichSuPhongBan=lichSuPhongBan;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public int getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(int ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public int getThangSinh() {
        return thangSinh;
    }

    public void setThangSinh(int thangSinh) {
        this.thangSinh = thangSinh;
    }

    public int getNamSinh() {
        return namSinh;
    }

    public void setNamSinh(int namSinh) {
        this.namSinh = namSinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public int getSoCMND() {
        return soCMND;
    }

    public void setSoCMND(int soCMND) {
        this.soCMND = soCMND;
    }

    public String getSoPhone() {
        return soPhone;
    }

    public void setSoPhone(String soPhone) {
        this.soPhone = soPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDanToc() {
        return danToc;
    }

    public void setDanToc(String danToc) {
        this.danToc = danToc;
    }

    public String getNguyenQuan() {
        return nguyenQuan;
    }

    public void setNguyenQuan(String nguyenQuan) {
        this.nguyenQuan = nguyenQuan;
    }

    public String getHoKhau() {
        return hoKhau;
    }

    public void setHoKhau(String hoKhau) {
        this.hoKhau = hoKhau;
    }

    public String getTonGiao() {
        return tonGiao;
    }

    public void setTonGiao(String tonGiao) {
        this.tonGiao = tonGiao;
    }

    public String getTinhTrangHonNhan() {
        return tinhTrangHonNhan;
    }

    public void setTinhTrangHonNhan(String tinhTrangHonNhan) {
        this.tinhTrangHonNhan = tinhTrangHonNhan;
    }

    public String getPhongBanTrucThuoc() {
        return phongBanTrucThuoc;
    }

    public void setPhongBanTrucThuoc(String phongBanTrucThuoc) {
        this.phongBanTrucThuoc = phongBanTrucThuoc;
    }

    public float getHeSoLuong() {
        return heSoLuong;
    }

    public void setHeSoLuong(float heSoLuong) {
        this.heSoLuong = heSoLuong;
    }

    public byte[] getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getLichSuNhanVien() {
        return lichSuPhongBan;
    }

    public void setLichSuNhanVien(String lichSuPhongBan) {
        this.lichSuPhongBan = lichSuPhongBan;
    }

}
