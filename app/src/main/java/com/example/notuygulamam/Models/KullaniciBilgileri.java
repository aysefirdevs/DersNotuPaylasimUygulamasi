package com.example.notuygulamam.Models;

public class KullaniciBilgileri {
    private String kullaniciAdi,okul,bolum,sınıf,resim;

    public KullaniciBilgileri() {
    }

    public KullaniciBilgileri(String kullaniciAdi,String okul, String bolum, String sınıf, String resim) {
        this.okul = okul;
        this.bolum = bolum;
        this.sınıf = sınıf;
        this.resim = resim;
        this.kullaniciAdi=kullaniciAdi;
    }

    public String getKullaniciAdi(){
        return kullaniciAdi;
    }

    public void setKullaniciAdi(){
        this.kullaniciAdi=kullaniciAdi;
    }

    public String getOkul() {
        return okul;
    }

    public void setOkul(String okul) {
        this.okul = okul;
    }

    public String getBolum() {
        return bolum;
    }

    public void setBolum(String bolum) {
        this.bolum = bolum;
    }

    public String getSınıf() {
        return sınıf;
    }

    public void setSınıf(String sınıf) {
        this.sınıf = sınıf;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    @Override
    public String toString() {
        return "KullaniciBilgileri{" +
                "kullaniciAdi='" + kullaniciAdi + '\'' +
                ", okul='" + okul + '\'' +
                ", bolum='" + bolum + '\'' +
                ", sınıf='" + sınıf + '\'' +
                ", resim='" + resim + '\'' +
                '}';
    }
}
