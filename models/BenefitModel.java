package models; // Package tempat class ini berada

import java.sql.*;
import java.util.ArrayList; // Untuk membuat list dinamis
import java.util.List; // Interface List

public class BenefitModel implements BenefitModelContract { // Model untuk akses data benefit

    @Override
    public List<Benefit> getAllBenefits() { // Ambil semua data benefit
        List<Benefit> list = new ArrayList<>(); // List untuk menampung hasil query
        String sql = "SELECT * FROM tbenefit"; // Query mengambil semua data benefit

        try (Connection conn = Database.getConnection()) { // Membuka koneksi database
            if (conn != null) {
                try (Statement stmt = conn.createStatement(); // Membuat statement SQL
                     ResultSet rs = stmt.executeQuery(sql)) { // Menjalankan query dan menyimpan hasilnya

                    while (rs.next()) { // Loop selama masih ada data
                        Benefit b = new Benefit(
                                rs.getString("username"), // Ambil username
                                rs.getInt("skor"), // Ambil skor
                                rs.getInt("peluru_meleset"), // Ambil jumlah peluru meleset
                                rs.getInt("sisa_peluru") // Ambil sisa peluru
                        );
                        list.add(b); // Masukkan data ke list
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Menampilkan error jika query gagal
        }

        return list; // Mengembalikan semua data benefit
    }

    @Override
    public Benefit getBenefitByUsername(String username) { // Ambil benefit berdasarkan username
        String sql = "SELECT * FROM tbenefit WHERE username = ?"; // Query dengan parameter

        try (Connection conn = Database.getConnection()) { // Koneksi database
            if (conn != null) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) { // PreparedStatement agar aman
                    stmt.setString(1, username); // Isi parameter username
                    ResultSet rs = stmt.executeQuery(); // Jalankan query

                    if (rs.next()) { // Jika data ditemukan
                        return new Benefit(
                                rs.getString("username"), // Ambil username
                                rs.getInt("skor"), // Ambil skor
                                rs.getInt("peluru_meleset"), // Ambil peluru meleset
                                rs.getInt("sisa_peluru") // Ambil sisa peluru
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Menampilkan error database
        }

        return null; // Return null jika data tidak ada
    }

    @Override
    public void saveOrUpdate(String username, int skor, int peluruMeleset, int sisaPeluru) { // Simpan atau update benefit
        String checkSql = "SELECT username FROM tbenefit WHERE username = ?"; // Cek apakah username sudah ada
        String insertSql = "INSERT INTO tbenefit VALUES (?, ?, ?, ?)"; // Query insert data baru
        String updateSql = "UPDATE tbenefit SET skor = skor + ?, peluru_meleset = peluru_meleset + ?, sisa_peluru = ? WHERE username = ?"; // Query update data lama

        try (Connection conn = Database.getConnection()) { // Membuka koneksi database
            if (conn != null) {
                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setString(1, username); // Isi parameter username
                    ResultSet rs = checkStmt.executeQuery(); // Jalankan query cek

                    if (rs.next()) { // Jika data sudah ada
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) { // Statement update
                            updateStmt.setInt(1, skor); // Tambahkan skor
                            updateStmt.setInt(2, peluruMeleset); // Tambahkan peluru meleset
                            updateStmt.setInt(3, sisaPeluru); // Update sisa peluru
                            updateStmt.setString(4, username); // Tentukan username
                            updateStmt.executeUpdate(); // Jalankan update
                        }
                    } else { // Jika data belum ada
                        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) { // Statement insert
                            insertStmt.setString(1, username); // Isi username
                            insertStmt.setInt(2, skor); // Isi skor
                            insertStmt.setInt(3, peluruMeleset); // Isi peluru meleset
                            insertStmt.setInt(4, sisaPeluru); // Isi sisa peluru
                            insertStmt.executeUpdate(); // Jalankan insert
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Menampilkan error saat simpan/update
        }
    }
}
