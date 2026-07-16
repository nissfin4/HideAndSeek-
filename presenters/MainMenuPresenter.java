package presenters; // Package presenters untuk logika penghubung View dan Model

import models.*; // Import semua class pada package models (Benefit, BenefitModel, dll)
import views.GameView; // Import tampilan game
import views.MainMenuView; // Import tampilan menu utama
import views.MainMenuViewContract; // Import kontrak view menu utama

public class MainMenuPresenter implements MainMenuPresenterContract { // Presenter untuk menu utama

    private MainMenuViewContract view; // Referensi ke view menu utama
    private BenefitModelContract model; // Referensi ke model (database / data)

    public MainMenuPresenter(MainMenuViewContract view) { // Constructor presenter
        this.view = view; // Menyimpan view yang dikirim
        this.model = new BenefitModel(); // Membuat instance model untuk akses database
    }

    @Override
    public void loadTableData() { // Method untuk memuat data tabel di menu utama
        view.showTableData(model.getAllBenefits()); // Ambil data dari model lalu tampilkan ke view
    }

    @Override
    public void onPlayClicked(String username) { // Dipanggil saat tombol Play ditekan
        if (username == null || username.isEmpty()) { // Cek apakah username kosong
            view.showMessage("Username tidak boleh kosong!"); // Tampilkan pesan ke user
            return; // Hentikan proses jika username tidak valid
        }

        Benefit benefit = model.getBenefitByUsername(username); // Ambil data benefit berdasarkan username
        if (benefit == null) { // Jika username belum ada di database
            model.saveOrUpdate(username, 0, 0, 0); // Simpan data awal ke database
            benefit = new Benefit(username, 0, 0, 0); // Buat objek benefit baru
        }

        GameView gameView = new GameView(null, benefit); // Buat tampilan game dan kirim data benefit
        GamePresenter presenter = new GamePresenter(gameView, this); // Buat presenter game
        gameView.setPresenter(presenter); // Hubungkan view game dengan presenternya

        view.setVisible(false); // Sembunyikan menu utama saat game dimulai
    }

    @Override
    public void updateBenefitAfterGame(Benefit benefit) { // Dipanggil setelah game selesai atau pause
        model.saveOrUpdate( // Simpan atau update data hasil permainan ke database
                benefit.getUsername(), // Username pemain
                benefit.getSkor(), // Skor yang didapat
                benefit.getPeluruMeleset(), // Jumlah peluru alien yang meleset
                benefit.getSisaPeluru() // Sisa peluru di akhir permainan
        );
        loadTableData(); // Refresh data tabel di menu utama
        view.setVisible(true); // Tampilkan kembali menu utama
    }

    @Override
    public MainMenuView getView() { // Method untuk mengambil objek view menu utama
        return (MainMenuView) view; // Casting dari contract ke implementasi view
    }
}
