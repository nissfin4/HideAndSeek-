package views; // Package views untuk tampilan menu utama

import presenters.MainMenuPresenter; // Import presenter untuk logika menu
import models.Benefit; // Import model Benefit untuk data pemain

import javax.swing.*; // Import Swing GUI
import javax.swing.border.EmptyBorder; // Border kosong
import javax.swing.table.DefaultTableModel; // Model tabel
import javax.swing.table.DefaultTableCellRenderer; // Renderer tabel
import java.awt.*; // Layout dan warna
import java.util.List; // List untuk data

public class MainMenuView extends JFrame implements MainMenuViewContract { // JFrame menu utama, implement contract

    private JTable table; // Tabel data pemain
    private DefaultTableModel tableModel; // Model tabel
    private JTextField usernameField; // Input username
    private JButton playButton; // Tombol main
    private JButton quitButton; // Tombol keluar

    private MainMenuPresenter presenter; // Presenter menu utama
    private Image backgroundImage; // Background menu

    public MainMenuView() { // Konstruktor menu utama
        setTitle("Sheriff Edition"); // Judul jendela
        setSize(750, 520); // Ukuran jendela
        setLocationRelativeTo(null); // Tengah layar
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Tutup aplikasi saat exit

        presenter = new MainMenuPresenter(this); // Inisialisasi presenter

        loadBackgroundImage(); // Load gambar background
        initComponents(); // Inisialisasi komponen GUI
        presenter.loadTableData(); // Load data tabel dari model
    }

    private void loadBackgroundImage() { // Load background
        try {
            backgroundImage = new ImageIcon(
                    getClass().getClassLoader().getResource("assets/background.jpg") // Ambil gambar
            ).getImage();
        } catch (Exception e) {
            backgroundImage = null; // Jika gagal, null
        }
    }

    private void initComponents() { // Inisialisasi komponen GUI
        JPanel mainPanel = new JPanel() { // Panel utama
            @Override
            protected void paintComponent(Graphics g) { // Custom paint background
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // Draw background
                }
                g.setColor(new Color(0, 0, 0, 150)); // Warna overlay hitam transparan
                g.fillRect(0, 0, getWidth(), getHeight()); // Fill overlay
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 10)); // Layout border
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Padding
        setContentPane(mainPanel); // Set panel utama

        // TITLE
        JLabel title = new JLabel("Sheriff Game", SwingConstants.CENTER); // Judul
        title.setFont(new Font("Segoe UI Black", Font.BOLD, 30)); // Font besar
        title.setForeground(new Color(255, 215, 0)); // Warna emas
        mainPanel.add(title, BorderLayout.NORTH); // Letak di atas

        // CENTER PANEL
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10)); // Panel tengah
        centerPanel.setOpaque(false); // Transparan
        mainPanel.add(centerPanel, BorderLayout.CENTER); // Tambah ke main panel

        // INPUT CARD (USERNAME + PLAY)
        JPanel inputCard = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); // Panel input
        inputCard.setBackground(new Color(255, 255, 255, 220)); // Warna putih transparan

        JLabel userLabel = new JLabel("Username:"); // Label username
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Font label

        usernameField = new JTextField(15); // Input username
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        playButton = new JButton("PLAY"); // Tombol main
        playButton.setFont(new Font("Segoe UI", Font.BOLD, 16));

        inputCard.add(userLabel); // Tambah label
        inputCard.add(usernameField); // Tambah input
        inputCard.add(playButton); // Tambah tombol play

        centerPanel.add(inputCard, BorderLayout.NORTH); // Letak di atas center panel

        // TABLE (READ ONLY)
        tableModel = new DefaultTableModel(
                new String[]{"Username", "Skor", "Peluru Meleset", "Sisa Peluru"}, 0 // Kolom tabel
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false; // Tabel tidak bisa diedit
            }
        };

        table = new JTable(tableModel); // Buat tabel
        table.setRowHeight(25); // Tinggi baris

        DefaultTableCellRenderer center = new DefaultTableCellRenderer(); // Renderer center
        center.setHorizontalAlignment(JLabel.CENTER); // Tengah
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center); // Terapkan ke semua kolom
        }

        JScrollPane scrollPane = new JScrollPane(table); // Scrollable
        centerPanel.add(scrollPane, BorderLayout.CENTER); // Tambah ke center

        // QUIT PANEL
        JPanel quitPanel = new JPanel(); // Panel quit
        quitPanel.setOpaque(false); // Transparan

        quitButton = new JButton("QUIT"); // Tombol quit
        quitButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        quitButton.setForeground(Color.WHITE); // Warna tulisan
        quitButton.setBackground(Color.RED); // Warna tombol

        quitPanel.add(quitButton); // Tambah tombol quit
        mainPanel.add(quitPanel, BorderLayout.SOUTH); // Letak bawah

        // ACTIONS
        playButton.addActionListener(e ->
                presenter.onPlayClicked(usernameField.getText()) // Jalankan ketika play diklik
        );

        quitButton.addActionListener(e -> {
            dispose(); // Tutup jendela
            System.exit(0); // Keluar aplikasi
        });
    }

    // CONTRACT
    @Override
    public void showTableData(List<Benefit> list) { // Tampilkan data tabel
        tableModel.setRowCount(0); // Hapus tabel lama
        for (Benefit b : list) {
            tableModel.addRow(new Object[]{ // Tambah baris
                    b.getUsername(),
                    b.getSkor(),
                    b.getPeluruMeleset(),
                    b.getSisaPeluru()
            });
        }
    }

    @Override
    public void showMessage(String msg) { // Tampilkan popup
        JOptionPane.showMessageDialog(this, msg);
    }

    @Override
    public String getUsernameInput() { // Ambil input username
        return usernameField.getText();
    }
}
