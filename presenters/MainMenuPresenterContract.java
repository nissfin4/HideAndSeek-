package presenters; // Package presenters untuk kontrak presenter menu utama

import models.Benefit; // Import class Benefit untuk membawa data hasil permainan
import views.MainMenuView; // Import class view menu utama

public interface MainMenuPresenterContract { // Interface sebagai kontrak presenter menu utama

    void loadTableData(); // Method untuk memuat dan menampilkan data tabel benefit di menu utama
    void onPlayClicked(String username); // Method yang dipanggil saat tombol Play ditekan
    void updateBenefitAfterGame(Benefit benefit); // Method untuk memperbarui data setelah game selesai atau pause
    MainMenuView getView(); // Method untuk mengambil objek view menu utama
}
