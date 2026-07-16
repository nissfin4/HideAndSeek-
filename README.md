# 🎮 HideAndSeek

HideAndSeek adalah aplikasi/game berbasis Java GUI yang dikembangkan untuk mata kuliah **Desain dan Pemrograman Berorientasi Objek (DPBO)**.


## 🏗️ Arsitektur Proyek
Proyek ini mengimplementasikan pola desain **MVP (Model-View-Presenter)** untuk memisahkan antara logika bisnis, tampilan antarmuka (GUI), dan penghubung di antara keduanya. Ini memastikan kode yang lebih rapi, terstruktur, dan mudah dikelola (maintainable).

## 📁 Struktur Direktori
Berikut adalah penjelasan singkat mengenai struktur folder dalam repository ini:
- `assets/` - Berisi aset visual seperti gambar dan ikon yang digunakan dalam game.
- `audio/` - Berisi file suara/musik (*sound effect* & *backgound music*).
- `models/` - Mengelola logika bisnis, data, dan koneksi ke database.
- `views/` - Mengelola segala sesuatu yang berhubungan dengan *User Interface* (UI) menggunakan Java GUI.
- `presenters/` - Bertindak sebagai perantara antara `models` dan `views`.
- `lib/` - Berisi file *library* tambahan (seperti *driver* database MySQL JDBC).
- `Main.java` - Kelas utama (*entry point*) untuk menjalankan aplikasi dan menampilkan menu utama.

---

## 🚀 Cara Menjalankan Proyek

1. **Clone Repository**
   ```bash
   git clone https://github.com/nissfin4/HideAndSeek-.git
   cd HideAndSeek-
   ```

2. **Persyaratan Sistem**
   - **Java Development Kit (JDK)** versi 8 atau lebih baru.
   - Database MySQL / XAMPP (jika ada koneksi ke database lokal).
   - IDE disarankan: IntelliJ IDEA, Eclipse, atau NetBeans.

3. **Buka di IDE (Contoh: IntelliJ IDEA)**
   - Buka IntelliJ IDEA, klik **Open** dan pilih folder proyek `HideAndSeek`.
   - Pastikan Anda mengatur *Project SDK* dengan versi Java yang benar.
   - Jika ada *library* external di folder `lib/`, jangan lupa tambahkan ke *Project Structure > Libraries*.
   - Jalankan (*Run*) file `Main.java` untuk memulai aplikasi.

