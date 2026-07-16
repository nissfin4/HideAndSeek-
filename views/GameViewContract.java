package views; // Package views untuk tampilan game

import models.Benefit; // Import model Benefit untuk data skor dan peluru

public interface GameViewContract { // Interface kontrak untuk view game
    void updateBenefit(Benefit benefit); // Update data skor, peluru, dan peluru meleset
    void showGameOver(String message); // Tampilkan pesan Game Over
}
