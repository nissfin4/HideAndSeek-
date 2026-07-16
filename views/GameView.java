package views; // Package views untuk menyimpan semua tampilan game

import presenters.GamePresenterContract; // Import kontrak presenter game
import models.Benefit; // Import model Benefit untuk data skor dan peluru
import audio.SoundPlayer; // Import class SoundPlayer untuk suara

import javax.swing.*; // Import Swing untuk GUI
import java.awt.*; // Import AWT untuk graphics
import java.awt.image.BufferedImage; // Import BufferedImage untuk gambar
import javax.imageio.ImageIO; // Import ImageIO untuk load image
import java.awt.event.KeyEvent; // Import KeyEvent untuk keyboard input
import java.awt.event.KeyListener; // Import KeyListener untuk menangani input keyboard
import java.util.*; // Import util untuk ArrayList dan Random

public class GameView extends JFrame implements KeyListener, Runnable, GameViewContract { // Class view utama game

    private BufferedImage background, sheriff, alien, bullet, batuImage; // Gambar background, karakter, alien, peluru, dan batu

    private int SCREEN_WIDTH = 800, SCREEN_HEIGHT = 600; // Ukuran layar
    private int SHERIFF_WIDTH = 80, SHERIFF_HEIGHT = 100; // Ukuran karakter utama
    private int ALIEN_WIDTH = 60, ALIEN_HEIGHT = 80; // Ukuran alien
    private int BULLET_SIZE = 10, SPEED = 15, ALIEN_SPEED = 3; // Ukuran peluru, kecepatan karakter, kecepatan alien
    private int BATU_WIDTH = SHERIFF_WIDTH, BATU_HEIGHT = SHERIFF_HEIGHT; // Ukuran batu (sama dengan sheriff)

    private int playerX, playerY; // Posisi karakter
    private int skor = 0, peluruMeleset = 0, sisaPeluru = 0; // Statistik game

    private ArrayList<Alien> aliens = new ArrayList<>(); // List alien
    private Random random = new Random(); // Objek Random
    private ArrayList<Bullet> alienBullets = new ArrayList<>(); // List peluru alien
    private ArrayList<Bullet> sheriffBullets = new ArrayList<>(); // List peluru pemain
    private ArrayList<Rectangle> stones = new ArrayList<>(); // List batu sebagai penghalang

    private boolean running = true; // Status game sedang berjalan
    private int lastDirectionX = 0, lastDirectionY = -1; // Arah terakhir tembakan atau gerakan pemain

    private GamePresenterContract presenter; // Presenter game
    private Benefit benefit; // Objek benefit untuk data skor/peluru

    private SoundPlayer bgm = new SoundPlayer(); // SoundPlayer untuk musik latar

    public GameView(GamePresenterContract presenter, Benefit benefit) { // Constructor view game
        this.presenter = presenter; // Set presenter
        this.benefit = benefit; // Set benefit
        this.sisaPeluru = benefit.getSisaPeluru(); // Ambil sisa peluru dari benefit

        setTitle("Sheriff Game"); // Set judul jendela
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT); // Set ukuran jendela
        setLocationRelativeTo(null); // Tampilkan di tengah layar
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Tutup program jika klik X
        setResizable(false); // Tidak bisa di-resize

        loadImages(); // Load semua gambar
        initPlayer(); // Set posisi awal pemain
        initStones(); // Set posisi batu

        add(new GamePanel()); // Tambahkan panel game
        addKeyListener(this); // Tambahkan listener keyboard
        setFocusable(true); // Fokus agar bisa menerima input

        SwingUtilities.invokeLater(this::requestFocusInWindow); // Pastikan window dapat fokus
        setVisible(true); // Tampilkan window

        bgm.playLoop("assets/sound/play.wav"); // Mainkan musik latar
        new Thread(this).start(); // Jalankan thread game loop
    }

    private void initPlayer() { // Inisialisasi posisi pemain
        playerX = SCREEN_WIDTH / 2 - SHERIFF_WIDTH / 2; // Tengah layar horizontal
        playerY = SCREEN_HEIGHT / 2 - SHERIFF_HEIGHT / 2; // Tengah layar vertikal
    }

    private void initStones() {
        stones.clear();
        int jumlahBatu = 5 + random.nextInt(4);

        for (int i = 0; i < jumlahBatu; i++) {
            int x, y;
            boolean overlap;
            int attempts = 0;

            do {
                x = random.nextInt(SCREEN_WIDTH - BATU_WIDTH);
                y = random.nextInt(SCREEN_HEIGHT - BATU_HEIGHT);
                overlap = false;

                Rectangle newStone = new Rectangle(x, y, BATU_WIDTH, BATU_HEIGHT);

                // cek overlap dengan batu lain
                for (Rectangle stone : stones) {
                    if (newStone.intersects(stone)) {
                        overlap = true;
                    }
                }

                // cek jangan terlalu tengah
                int centerX = SCREEN_WIDTH / 2;
                int centerY = SCREEN_HEIGHT / 2;
                if (Math.abs(x - centerX) < 100 && Math.abs(y - centerY) < 100) {
                    overlap = true;
                }

                attempts++;
            } while (overlap && attempts < 50);

            stones.add(new Rectangle(x, y, BATU_WIDTH, BATU_HEIGHT));
        }
    }


    public void setPresenter(GamePresenterContract presenter) { // Setter presenter
        this.presenter = presenter;
    }

    private void loadImages() { // Load semua image dari folder assets
        try {
            background = ImageIO.read(getClass().getClassLoader().getResource("assets/background.jpg"));
            sheriff = ImageIO.read(getClass().getClassLoader().getResource("assets/sheriff.png"));
            alien = ImageIO.read(getClass().getClassLoader().getResource("assets/bandit.png"));
            bullet = ImageIO.read(getClass().getClassLoader().getResource("assets/peluru.png"));
            batuImage = ImageIO.read(getClass().getClassLoader().getResource("assets/batu.png"));
        } catch (Exception e) {
            System.err.println("Error loading images: " + e.getMessage()); // Jika gagal load
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) { // Method yang dipanggil saat tombol keyboard ditekan
        int key = e.getKeyCode(); // Ambil kode tombol yang ditekan
        int newX = playerX; // Inisialisasi posisi baru X sama dengan posisi pemain saat ini
        int newY = playerY; // Inisialisasi posisi baru Y sama dengan posisi pemain saat ini

        // ===== GERAK (Arrow keys) =====
        if (key == KeyEvent.VK_LEFT) { newX -= SPEED; lastDirectionX = -1; lastDirectionY = 0; } // Geser ke kiri, update arah terakhir
        if (key == KeyEvent.VK_RIGHT) { newX += SPEED; lastDirectionX = 1; lastDirectionY = 0; } // Geser ke kanan, update arah terakhir
        if (key == KeyEvent.VK_UP) { newY -= SPEED; lastDirectionX = 0; lastDirectionY = -1; } // Geser ke atas, update arah terakhir
        if (key == KeyEvent.VK_DOWN) { newY += SPEED; lastDirectionX = 0; lastDirectionY = 1; } // Geser ke bawah, update arah terakhir

        if (newX >= 0 && newX <= SCREEN_WIDTH - SHERIFF_WIDTH) playerX = newX; // Pastikan X pemain tetap di layar
        if (newY >= 0 && newY <= SCREEN_HEIGHT - SHERIFF_HEIGHT) playerY = newY; // Pastikan Y pemain tetap di layar

        // ===== TEMBAK (W/A/S/D) =====
        int shootX = 0, shootY = 0; // Inisialisasi arah tembakan
        if (key == KeyEvent.VK_W) shootY = -1; // Tembak ke atas
        if (key == KeyEvent.VK_S) shootY = 1;  // Tembak ke bawah
        if (key == KeyEvent.VK_A) shootX = -1; // Tembak ke kiri
        if (key == KeyEvent.VK_D) shootX = 1;  // Tembak ke kanan

        if ((shootX != 0 || shootY != 0) && sisaPeluru > 0) { // Cek kalau ada arah tembakan dan masih punya peluru
            SoundPlayer.playOnce("assets/sound/shoot.wav"); // Mainkan suara tembakan
            sheriffBullets.add(new Bullet( // Tambahkan peluru baru ke list
                    playerX + SHERIFF_WIDTH / 2, // Posisi X peluru = tengah pemain
                    playerY + SHERIFF_HEIGHT / 2, // Posisi Y peluru = tengah pemain
                    BULLET_SIZE, BULLET_SIZE, // Ukuran peluru
                    shootX * 8, // Kecepatan X peluru
                    shootY * 8  // Kecepatan Y peluru
            ));
            sisaPeluru--; // Kurangi jumlah peluru
        }

        // ===== PAUSE (SPACE) =====
        if (key == KeyEvent.VK_SPACE) { // Jika tombol SPACE ditekan
            running = false; // Hentikan game loop
            bgm.stop(); // Hentikan musik latar
            updateBenefit(benefit); // Update benefit terakhir
            if (presenter != null) presenter.onGamePaused(benefit); // Panggil presenter jika ada
            dispose(); // Tutup jendela game saat ini
            new MainMenuView().setVisible(true); // Buka menu utama
        }
    }


    private void gameOver() { // Method game over
        if (!running) return; // Jika sudah berhenti, return
        running = false;
        bgm.stop(); // Stop musik
        SoundPlayer.playOnce("assets/sound/GameOver.wav"); // Suara game over
        updateBenefit(benefit); // Update benefit
        if (presenter != null) {
            presenter.onGameOver(benefit); // Panggil presenter
        } else {
            showGameOver("GAME OVER"); // Tampilkan popup game over
        }
    }

    @Override public void updateBenefit(Benefit b) { // Update benefit skor/peluru
        b.setSkor(skor);
        b.setPeluruMeleset(peluruMeleset);
        b.setSisaPeluru(sisaPeluru);
    }

    @Override public void showGameOver(String msg) { // Tampilkan pesan game over
        JOptionPane.showMessageDialog(this, msg);
        dispose(); // Tutup window
    }

    @Override
    public void run() { // Game loop dijalankan di thread
        while (running) {
            if (random.nextInt(100) < 2) { // Spawn alien baru random
                int spawnX = random.nextInt(SCREEN_WIDTH - ALIEN_WIDTH);
                int spawnY = SCREEN_HEIGHT; // Dari bawah
                int vx = random.nextInt(ALIEN_SPEED * 2 + 1) - ALIEN_SPEED; // Kecepatan X random
                int vy = -ALIEN_SPEED; // Kecepatan Y ke atas
                if (vx == 0) vx = random.nextBoolean() ? ALIEN_SPEED : -ALIEN_SPEED;
                aliens.add(new Alien(spawnX, spawnY, ALIEN_WIDTH, ALIEN_HEIGHT, vx, vy));
            }

            Iterator<Alien> alienIter = aliens.iterator(); // Loop alien
            while (alienIter.hasNext()) {
                Alien a = alienIter.next();
                a.update(); // Update posisi alien

                if (random.nextInt(100) < 1) { // Alien kadang tembak
                    int dx = (playerX + SHERIFF_WIDTH / 2) - (a.x + ALIEN_WIDTH / 2);
                    int dy = (playerY + SHERIFF_HEIGHT / 2) - (a.y + ALIEN_HEIGHT / 2);
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    if (distance > 0) {
                        int bulletSpeed = 5;
                        int vx = (int) ((dx / distance) * bulletSpeed);
                        int vy = (int) ((dy / distance) * bulletSpeed);
                        alienBullets.add(new Bullet(
                                a.x + ALIEN_WIDTH / 2,
                                a.y + ALIEN_HEIGHT / 2,
                                BULLET_SIZE, BULLET_SIZE, vx, vy
                        ));
                    }
                }
            }

            Iterator<Bullet> ab = alienBullets.iterator(); // Loop peluru alien
            while (ab.hasNext()) {
                Bullet b = ab.next();
                boolean kenaBatu = false;
                for (Rectangle batu : stones) { // Cek tabrakan dengan batu
                    if (b.getBounds().intersects(batu)) kenaBatu = true;
                }
                if (kenaBatu) { // Jika kena batu, hilangkan peluru
                    ab.remove();
                    peluruMeleset++;
                    sisaPeluru++;
                    continue;
                }
                b.update(); // Update posisi peluru
                if (b.getBounds().intersects(new Rectangle(playerX, playerY, SHERIFF_WIDTH, SHERIFF_HEIGHT))) {
                    gameOver(); // Jika kena pemain, game over
                    return;
                }
            }

            Iterator<Bullet> sb = sheriffBullets.iterator(); // Loop peluru pemain
            while (sb.hasNext()) {
                Bullet b = sb.next();
                b.update();
                if (b.x < 0 || b.x > SCREEN_WIDTH || b.y < 0 || b.y > SCREEN_HEIGHT) { // Jika keluar layar
                    sb.remove();
                    continue;
                }
                boolean alienKena = false;

                Iterator<Alien> aIter = aliens.iterator();
                while (aIter.hasNext() && !alienKena) { // Cek tabrakan peluru pemain vs alien
                    Alien alien = aIter.next();
                    if (b.getBounds().intersects(alien.getBounds())) {
                        sb.remove();
                        aIter.remove(); // Hapus alien
                        skor += 10; // Tambah skor
                        alienKena = true;
                    }
                }

            }

            Iterator<Alien> aIter2 = aliens.iterator(); // Hapus alien yang keluar layar
            while (aIter2.hasNext()) {
                Alien a = aIter2.next();
                if (a.x < -ALIEN_WIDTH || a.x > SCREEN_WIDTH ||
                        a.y < -ALIEN_HEIGHT || a.y > SCREEN_HEIGHT) {
                    aIter2.remove();
                }
            }

            Iterator<Bullet> abIter = alienBullets.iterator(); // Hapus peluru alien yang keluar layar
            while (abIter.hasNext()) {
                Bullet b = abIter.next();
                if (b.x < 0 || b.x > SCREEN_WIDTH || b.y < 0 || b.y > SCREEN_HEIGHT) {
                    peluruMeleset++;
                    sisaPeluru++;
                    abIter.remove();
                }
            }

            repaint(); // Refresh panel
            try { Thread.sleep(30); } catch (InterruptedException ignored) {} // Delay game loop
        }
    }

    class Alien { // Class alien
        int x, y, width, height, vx, vy; // Posisi dan kecepatan
        Alien(int x, int y, int width, int height, int vx, int vy) { // Constructor
            this.x = x; this.y = y; this.width = width; this.height = height;
            this.vx = vx; this.vy = vy;
        }
        void update() { // Update posisi alien
            x += vx;
            y += vy;
            if (x <= 0 || x >= SCREEN_WIDTH - width) vx = -vx; // Pantul horizontal
        }
        Rectangle getBounds() { return new Rectangle(x, y, width, height); }
    }

    class Bullet { // Class peluru
        int x, y, w, h, vx, vy; // Posisi dan kecepatan
        Bullet(int x,int y,int w,int h,int vx,int vy){ this.x=x; this.y=y; this.w=w; this.h=h; this.vx=vx; this.vy=vy; } // Constructor
        void update() { x += vx; y += vy; } // Update posisi peluru
        Rectangle getBounds() { return new Rectangle(x, y, w, h); } // Dapatkan rectangle peluru
    }

    class GamePanel extends JPanel { // Panel untuk render game
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (background != null) { g.drawImage(background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this); }
            else { g.setColor(Color.BLACK); g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT); }

            if (batuImage != null) {
                for (Rectangle stone : stones) { g.drawImage(batuImage, stone.x, stone.y, stone.width, stone.height, this); }
            } else {
                g.setColor(Color.GRAY); for (Rectangle stone : stones) { g.fillRect(stone.x, stone.y, stone.width, stone.height); }
            }

            if (alien != null) {
                for (Alien a : aliens) { g.drawImage(alien, a.x, a.y, a.width, a.height, this); }
            } else {
                g.setColor(Color.RED); for (Alien a : aliens) { g.fillRect(a.x, a.y, a.width, a.height); }
            }

            if (bullet != null) {
                for (Bullet b : sheriffBullets) { g.drawImage(bullet, b.x, b.y, b.w, b.h, this); }
                for (Bullet b : alienBullets) { g.drawImage(bullet, b.x, b.y, b.w, b.h, this); }
            } else {
                g.setColor(Color.YELLOW); for (Bullet b : sheriffBullets) { g.fillOval(b.x, b.y, b.w, b.h); }
                g.setColor(Color.ORANGE); for (Bullet b : alienBullets) { g.fillOval(b.x, b.y, b.w, b.h); }
            }

            if (sheriff != null) { g.drawImage(sheriff, playerX, playerY, SHERIFF_WIDTH, SHERIFF_HEIGHT, this); }
            else { g.setColor(Color.BLUE); g.fillRect(playerX, playerY, SHERIFF_WIDTH, SHERIFF_HEIGHT); }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Skor: " + skor, 10, 30);
            g.drawString("Peluru: " + sisaPeluru, 10, 60);
            g.drawString("Meleset: " + peluruMeleset, 10, 90);
        }
    }

    @Override public void keyReleased(KeyEvent e){}
    @Override public void keyTyped(KeyEvent e){}
}
