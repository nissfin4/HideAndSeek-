package models; // Package models untuk class yang berhubungan dengan data/database

import java.sql.Connection; // Interface untuk koneksi ke database
import java.sql.DriverManager; // Class untuk membuat koneksi database
import java.sql.SQLException; // Class untuk menangani error SQL

public class Database { // Class Database sebagai helper koneksi database

    private static final String URL = "jdbc:mysql://localhost:3306/db_tugasbesar"; // Alamat database MySQL
    private static final String USER = "root"; // Username database
    private static final String PASSWORD = ""; // Password database (kosong untuk localhost)

    public static Connection getConnection() { // Method static untuk mengambil koneksi database
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD); // Membuat dan mengembalikan koneksi
        } catch (SQLException e) { // Jika koneksi gagal
            System.out.println("Koneksi database gagal!"); // Pesan error ke console
            e.printStackTrace(); // Menampilkan detail error
            return null; // Mengembalikan null jika koneksi tidak berhasil
        }
    }
}
