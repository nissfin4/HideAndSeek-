package models; // Package untuk semua class model

import java.util.List; // Digunakan untuk menyimpan kumpulan data Benefit

public interface BenefitModelContract { // Kontrak (interface) untuk model Benefit
    List<Benefit> getAllBenefits(); // Method untuk mengambil semua data benefit dari database
    Benefit getBenefitByUsername(String username); // Method untuk mengambil data benefit berdasarkan username
    void saveOrUpdate(String username, int skor, int peluruMeleset, int sisaPeluru); // Method untuk menyimpan atau memperbarui data benefit
}
