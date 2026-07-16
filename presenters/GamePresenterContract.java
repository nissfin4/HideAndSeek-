package presenters; // Package presenters untuk interface presenter game

import models.Benefit; // Import class Benefit untuk membawa data hasil permainan

public interface GamePresenterContract { // Kontrak (interface) untuk GamePresenter
    void onGameOver(Benefit benefit); // Method yang dipanggil saat game berakhir
    void onGamePaused(Benefit benefit); // Method yang dipanggil saat game dijeda (pause)
    MainMenuPresenterContract getMainMenuPresenter(); // Method untuk mengambil presenter menu utama
}
