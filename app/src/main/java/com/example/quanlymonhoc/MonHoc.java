// File: MonHoc.java
package com.example.quanlymonhoc;

public class MonHoc {
    private String tenMon;
    private int soTinChi;

    public MonHoc(String tenMon, int soTinChi) {
        this.tenMon = tenMon;
        this.soTinChi = soTinChi;
    }

    // Getter cho tenMon
    public String getTenMon() {
        return tenMon;
    }

    // Setter cho tenMon
    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    // Getter cho soTinChi
    public int getSoTinChi() {
        return soTinChi;
    }

    // Setter cho soTinChi (Rất quan trọng cho chức năng cập nhật)
    public void setSoTinChi(int soTinChi) {
        this.soTinChi = soTinChi;
    }

    @Override
    public String toString() {
        return "Môn: " + tenMon + " (" + soTinChi + " tín chỉ)";
    }
}
