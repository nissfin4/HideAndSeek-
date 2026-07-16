package models;
// Menentukan bahwa class Benefit berada di dalam package models

public class Benefit {
    // Class ini dipakai untuk menyimpan data benefit / statistik pemain

    private String username;
    // Menyimpan nama pemain (username)

    private int skor;
    // Menyimpan total skor yang didapat pemain

    private int peluruMeleset;
    // Menyimpan jumlah peluru alien yang meleset

    private int sisaPeluru;
    // Menyimpan jumlah peluru sheriff yang masih tersedia

    public Benefit(String username, int skor, int peluruMeleset, int sisaPeluru) {
        // Constructor untuk mengisi semua data benefit saat objek dibuat

        this.username = username;
        // Mengisi username pemain

        this.skor = skor;
        // Mengisi skor awal pemain

        this.peluruMeleset = peluruMeleset;
        // Mengisi jumlah peluru alien yang meleset

        this.sisaPeluru = sisaPeluru;
        // Mengisi jumlah sisa peluru sheriff
    }

    public String getUsername() {
        // Method untuk mengambil username pemain
        return username;
    }

    public int getSkor() {
        // Method untuk mengambil nilai skor pemain
        return skor;
    }

    public int getPeluruMeleset() {
        // Method untuk mengambil jumlah peluru alien yang meleset
        return peluruMeleset;
    }

    public int getSisaPeluru() {
        // Method untuk mengambil jumlah sisa peluru sheriff
        return sisaPeluru;
    }

    public void setSkor(int skor) {
        // Method untuk mengubah nilai skor pemain
        this.skor = skor;
    }

    public void setPeluruMeleset(int peluruMeleset) {
        // Method untuk mengubah jumlah peluru alien yang meleset
        this.peluruMeleset = peluruMeleset;
    }

    public void setSisaPeluru(int sisaPeluru) {
        // Method untuk mengubah jumlah sisa peluru sheriff
        this.sisaPeluru = sisaPeluru;
    }
}
