package audio; // Paket tempat kelas SoundPlayer berada

import javax.sound.sampled.*; // Import kelas audio dari Java
import java.io.InputStream; // Import InputStream untuk membaca file suara

public class SoundPlayer { // Kelas untuk memutar suara

    private Clip clip; // Menyimpan clip audio yang sedang dimainkan (untuk loop)

    public static void playOnce(String path) { // Metode statis untuk memutar suara sekali
        try {
            InputStream audioSrc =
                    SoundPlayer.class.getClassLoader().getResourceAsStream(path); // Ambil file suara dari resources

            if (audioSrc == null) { // Jika file tidak ditemukan
                System.err.println("Sound file not found: " + path); // Tampilkan error
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(audioSrc); // Buat stream audio
            Clip clip = AudioSystem.getClip(); // Siapkan clip untuk memutar audio
            clip.open(ais); // Buka clip dengan audio stream

            // Tambahkan listener untuk menutup clip saat selesai diputar
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close(); // Tutup clip setelah selesai
                }
            });

            clip.start(); // Mulai memutar suara

        } catch (Exception e) { // Tangani error jika gagal memutar
            System.err.println("Error playing sound: " + path);
            e.printStackTrace();
        }
    }

    public void playLoop(String path) { // Metode untuk memutar suara berulang (loop)
        try {
            stop(); // Hentikan clip sebelumnya jika ada

            InputStream audioSrc =
                    getClass().getClassLoader().getResourceAsStream(path); // Ambil file suara

            if (audioSrc == null) { // Jika file tidak ditemukan
                System.err.println("Sound file not found: " + path);
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(audioSrc); // Buat stream audio
            clip = AudioSystem.getClip(); // Siapkan clip baru
            clip.open(ais); // Buka clip
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Atur loop terus menerus
            clip.start(); // Mulai memutar

        } catch (Exception e) { // Tangani error jika gagal memutar
            System.err.println("Error playing loop sound: " + path);
            e.printStackTrace();
        }
    }

    public void stop() { // Metode untuk menghentikan dan menutup clip
        if (clip != null && clip.isRunning()) { // Jika ada clip yang sedang berjalan
            clip.stop(); // Hentikan clip
            clip.close(); // Tutup clip
            clip = null; // Reset variabel
        }
    }
}
