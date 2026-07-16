package views; // Package views untuk tampilan GUI

import java.util.List; // Import List untuk menampung data
import models.Benefit; // Import model Benefit untuk data pemain

public interface MainMenuViewContract { // Contract/interface untuk MainMenuView
    void showTableData(List<Benefit> list); // Tampilkan daftar Benefit di tabel
    void showMessage(String msg); // Tampilkan pesan popup
    void setVisible(boolean visible); // Set visibilitas JFrame
    String getUsernameInput(); // Ambil input username dari user
}
