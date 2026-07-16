package presenters; // Package presenters untuk class penghubung antara view dan model

import models.Benefit; // Import class Benefit untuk membawa data hasil game
import views.GameViewContract; // Import kontrak view game

public class GamePresenter implements GamePresenterContract { // Presenter untuk mengatur logika setelah game berjalan

    private GameViewContract view; // Referensi ke GameView melalui interface
    private MainMenuPresenterContract mainMenuPresenter; // Referensi ke presenter menu utama

    public GamePresenter(GameViewContract view, MainMenuPresenterContract mainMenuPresenter) {
        this.view = view; // Menyimpan view game
        this.mainMenuPresenter = mainMenuPresenter; // Menyimpan presenter menu utama
    }

    @Override
    public void onGameOver(Benefit benefit) { // Dipanggil saat game berakhir
        String message = String.format( // Membuat pesan Game Over dengan data benefit
                "GAME OVER\nSkor: %d\nSisa Peluru: %d\nPeluru Meleset: %d",
                benefit.getSkor(), // Mengambil skor akhir
                benefit.getSisaPeluru(), // Mengambil sisa peluru
                benefit.getPeluruMeleset() // Mengambil jumlah peluru meleset
        );
        view.showGameOver(message); // Menampilkan pesan Game Over di view

        if (mainMenuPresenter != null) { // Mengecek apakah presenter menu utama ada
            mainMenuPresenter.updateBenefitAfterGame(benefit); // Mengirim data benefit ke menu utama
        }
    }

    @Override
    public void onGamePaused(Benefit benefit) { // Dipanggil saat game dipause
        if (mainMenuPresenter != null) { // Mengecek apakah presenter menu utama ada
            mainMenuPresenter.updateBenefitAfterGame(benefit); // Menyimpan/update data benefit saat pause
        }
    }

    @Override
    public MainMenuPresenterContract getMainMenuPresenter() { // Method untuk mengambil presenter menu utama
        return mainMenuPresenter; // Mengembalikan presenter menu utama
    }
}
