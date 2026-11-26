package com.ubiblio.view;

import com.ubiblio.controller.BukuController;
import com.ubiblio.model.Buku;
import com.ubiblio.service.LaporanService;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.springframework.context.ConfigurableApplicationContext;

public class MainFrame extends JFrame {

    private static final Color NAVY = new Color(23, 54, 93);
    private static final Color BLUE = new Color(39, 100, 165);
    private static final Color BG = new Color(245, 248, 252);
    private static final Color WHITE = Color.WHITE;
    private static final Color ABU = new Color(100, 116, 139);

    private final BukuController controller;
    private final LaporanService laporanService;
    private final ConfigurableApplicationContext context;

    private DefaultTableModel model;
    private JTable tabel;
    private JTextField txtCari;
    private JLabel lblHalaman;
    private JLabel lblTotal;
    private JLabel lblGenre;
    private JPanel konten;
    private JButton btnDashboard;
    private JButton btnKoleksi;

    public MainFrame(BukuController controller,
                     LaporanService laporanService,
                     ConfigurableApplicationContext context) {
        this.controller = controller;
        this.laporanService = laporanService;
        this.context = context;

        setTitle("uBiblio - Personal Library Management");
        setSize(1100, 700);
        setMinimumSize(new Dimension(850, 550));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                keluar();
            }
        });

        buatSidebar();
        buatAreaUtama();
        tampilkanDashboard();
    }

    private void keluar() {
        dispose();
        context.close();
        System.exit(0);
    }

    // ---------------------------------------------------------------
    // Kerangka tampilan
    // ---------------------------------------------------------------

    private void buatSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(NAVY);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(25, 15, 20, 15));

        JLabel logo = new JLabel("  uBiblio");
        logo.setForeground(WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 27));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("  PERSONAL LIBRARY");
        sub.setForeground(new Color(190, 210, 235));
        sub.setFont(new Font("SansSerif", Font.PLAIN, 10));
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnDashboard = menu("Dashboard");
        btnKoleksi = menu("Koleksi Buku");
        btnDashboard.setBackground(BLUE);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(sub);
        sidebar.add(Box.createVerticalStrut(45));
        sidebar.add(btnDashboard);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnKoleksi);
        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel(
                "<html><center>uBiblio Desktop<br>"
                + "Swing + Spring Boot + MySQL</center></html>");
        footer.setForeground(new Color(190, 210, 235));
        footer.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(footer);

        add(sidebar, BorderLayout.WEST);
    }

    private JButton menu(String nama) {
        JButton b = tombol("  " + nama, NAVY);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        b.setPreferredSize(new Dimension(190, 45));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));

        b.addActionListener(e -> {
            warnaMenu(nama);
            lblHalaman.setText(nama);

            if (nama.equals("Dashboard")) {
                tampilkanDashboard();
            } else {
                tampilkanKoleksi();
            }
        });

        return b;
    }

    private void warnaMenu(String nama) {
        btnDashboard.setBackground(nama.equals("Dashboard") ? BLUE : NAVY);
        btnKoleksi.setBackground(nama.equals("Koleksi Buku") ? BLUE : NAVY);
    }

    private void buatAreaUtama() {
        JPanel kanan = new JPanel(new BorderLayout());
        kanan.setBackground(BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        header.setBorder(new EmptyBorder(20, 28, 20, 28));

        lblHalaman = new JLabel("Dashboard");
        lblHalaman.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblHalaman.setForeground(NAVY);

        JLabel profil = new JLabel("Perpustakaan Pribadi");
        profil.setFont(new Font("SansSerif", Font.PLAIN, 13));
        profil.setForeground(ABU);

        header.add(lblHalaman, BorderLayout.WEST);
        header.add(profil, BorderLayout.EAST);

        konten = new JPanel(new BorderLayout());
        konten.setBackground(BG);
        konten.setBorder(new EmptyBorder(24, 28, 24, 28));

        kanan.add(header, BorderLayout.NORTH);
        kanan.add(konten, BorderLayout.CENTER);
        add(kanan, BorderLayout.CENTER);
    }

    // ---------------------------------------------------------------
    // Komponen kecil
    // ---------------------------------------------------------------

    private JPanel kartu(String judul, JLabel nilai) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 233, 242)),
                new EmptyBorder(15, 16, 15, 16)));

        JLabel label = new JLabel(judul);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(ABU);

        nilai.setFont(new Font("SansSerif", Font.BOLD, 24));
        nilai.setForeground(NAVY);

        p.add(label, BorderLayout.NORTH);
        p.add(nilai, BorderLayout.CENTER);
        return p;
    }

    // Tombol berwarna yang tampil benar di semua sistem operasi
    private JButton tombol(String teks, Color warna) {
        JButton b = new JButton(teks);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setForeground(WHITE);
        b.setBackground(warna);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(10, 15, 10, 15));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // ---------------------------------------------------------------
    // Halaman Dashboard
    // ---------------------------------------------------------------

    private void tampilkanDashboard() {
        konten.removeAll();
        konten.setLayout(new BorderLayout());

        JLabel sambutan = new JLabel(
                "<html><h1>Selamat Datang di uBiblio!</h1>"
                + "<p style='font-size:12px;color:#64748b'>"
                + "Kelola koleksi buku pribadi dengan mudah.</p></html>");

        lblTotal = new JLabel("0");
        lblGenre = new JLabel("0");

        JPanel statistik = new JPanel(new GridLayout(1, 2, 15, 15));
        statistik.setBackground(BG);
        statistik.add(kartu("Total Buku", lblTotal));
        statistik.add(kartu("Total Genre", lblGenre));

        JButton buka = tombol("Buka Koleksi Buku", BLUE);
        buka.addActionListener(e -> {
            warnaMenu("Koleksi Buku");
            lblHalaman.setText("Koleksi Buku");
            tampilkanKoleksi();
        });

        JPanel isi = new JPanel(new BorderLayout(0, 20));
        isi.setBackground(BG);
        isi.add(sambutan, BorderLayout.NORTH);
        isi.add(statistik, BorderLayout.CENTER);

        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bawah.setBackground(BG);
        bawah.add(buka);

        konten.add(isi, BorderLayout.NORTH);
        konten.add(bawah, BorderLayout.CENTER);

        // Tabel tidak dipakai di dashboard
        model = null;
        txtCari = null;

        perbaruiStatistik();
        konten.revalidate();
        konten.repaint();
    }

    // ---------------------------------------------------------------
    // Halaman Koleksi Buku
    // ---------------------------------------------------------------

    private void tampilkanKoleksi() {
        konten.removeAll();
        konten.setLayout(new BorderLayout(0, 15));

        JLabel judul = new JLabel("Kelola Koleksi Buku");
        judul.setFont(new Font("SansSerif", Font.BOLD, 19));
        judul.setForeground(new Color(45, 55, 72));

        JLabel deskripsi = new JLabel(
                "Tambah, cari, ubah, hapus, dan cetak laporan koleksi buku.");
        deskripsi.setFont(new Font("SansSerif", Font.PLAIN, 13));
        deskripsi.setForeground(new Color(110, 125, 145));

        JPanel teks = new JPanel();
        teks.setBackground(BG);
        teks.setLayout(new BoxLayout(teks, BoxLayout.Y_AXIS));
        teks.add(judul);
        teks.add(Box.createVerticalStrut(5));
        teks.add(deskripsi);

        lblTotal = new JLabel("0");
        lblGenre = new JLabel("0");

        JPanel ringkasan = new JPanel(new GridLayout(1, 2, 15, 0));
        ringkasan.setBackground(BG);
        ringkasan.add(kartu("Total Koleksi", lblTotal));
        ringkasan.add(kartu("Jumlah Genre", lblGenre));

        JPanel isiAtas = new JPanel(new BorderLayout(0, 15));
        isiAtas.setBackground(BG);
        isiAtas.add(teks, BorderLayout.NORTH);
        isiAtas.add(ringkasan, BorderLayout.CENTER);

        JPanel area = new JPanel(new BorderLayout(0, 12));
        area.setBackground(WHITE);
        area.setBorder(new EmptyBorder(15, 15, 15, 15));

        txtCari = new JTextField();
        txtCari.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtCari.setToolTipText("Cari ISBN, judul, penulis, atau genre");
        txtCari.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 228, 238)),
                new EmptyBorder(10, 12, 10, 12)));

        JButton tambah = tombol("+ Tambah Buku", BLUE);

        JPanel pencarian = new JPanel(new BorderLayout(10, 0));
        pencarian.setBackground(WHITE);
        pencarian.add(txtCari, BorderLayout.CENTER);
        pencarian.add(tambah, BorderLayout.EAST);

        model = new DefaultTableModel(
                new String[]{"ISBN", "Judul Buku", "Penulis", "Genre"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tabel = new JTable(model);
        tabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabel.setRowHeight(35);
        tabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabel.setShowVerticalLines(false);
        tabel.setGridColor(new Color(235, 240, 246));
        tabel.setSelectionBackground(new Color(220, 235, 250));
        tabel.setSelectionForeground(NAVY);
        tabel.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabel.getTableHeader().setBackground(new Color(235, 242, 250));
        tabel.getTableHeader().setForeground(NAVY);

        JScrollPane scroll = new JScrollPane(tabel);
        scroll.setBorder(BorderFactory.createLineBorder(
                new Color(230, 235, 242)));

        JButton laporan = tombol("Cetak Laporan PDF", new Color(30, 140, 110));
        JButton edit = tombol("Edit Buku", new Color(225, 155, 35));
        JButton hapus = tombol("Hapus Buku", new Color(190, 55, 65));

        JPanel bawah = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bawah.setBackground(WHITE);
        bawah.add(laporan);
        bawah.add(edit);
        bawah.add(hapus);

        area.add(pencarian, BorderLayout.NORTH);
        area.add(scroll, BorderLayout.CENTER);
        area.add(bawah, BorderLayout.SOUTH);

        konten.add(isiAtas, BorderLayout.NORTH);
        konten.add(area, BorderLayout.CENTER);

        tambah.addActionListener(e -> dialogBuku(null));
        edit.addActionListener(e -> editBuku());
        hapus.addActionListener(e -> hapusBuku());
        laporan.addActionListener(e -> cetakLaporan());

        // Pencarian langsung saat mengetik
        txtCari.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                muatTabel();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                muatTabel();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                muatTabel();
            }
        });

        muatTabel();
        konten.revalidate();
        konten.repaint();
    }

    // ---------------------------------------------------------------
    // Data
    // ---------------------------------------------------------------

    private void muatTabel() {
        if (model == null || txtCari == null) {
            return;
        }

        try {
            List<Buku> daftar = controller.cari(txtCari.getText().trim());

            model.setRowCount(0);
            for (Buku b : daftar) {
                model.addRow(new Object[]{
                    b.getIsbn(), b.getJudul(), b.getPenulis(), b.getGenre()
                });
            }

            perbaruiStatistik();

        } catch (Exception e) {
            tampilkanError("Gagal mengambil data", e);
        }
    }

    private void perbaruiStatistik() {
        try {
            if (lblTotal != null) {
                lblTotal.setText(String.valueOf(controller.total()));
            }
            if (lblGenre != null) {
                lblGenre.setText(String.valueOf(controller.jumlahGenre()));
            }
        } catch (Exception e) {
            tampilkanError("Gagal mengambil statistik", e);
        }
    }

    // ---------------------------------------------------------------
    // Tambah / Edit / Hapus
    // ---------------------------------------------------------------

    // lama == null  -> tambah buku baru; selain itu -> edit buku tersebut
    private void dialogBuku(Buku lama) {
        boolean edit = lama != null;

        JTextField isbn = new JTextField(edit ? lama.getIsbn() : "");
        JTextField judul = new JTextField(edit ? lama.getJudul() : "");
        JTextField penulis = new JTextField(edit ? lama.getPenulis() : "");

        JComboBox<String> genre = new JComboBox<>(new String[]{
            "Novel", "Pendidikan", "Teknologi",
            "Fantasi", "Sejarah", "Komik", "Lainnya"});

        if (edit) {
            genre.setSelectedItem(lama.getGenre());
            if (!lama.getGenre().equals(genre.getSelectedItem())) {
                genre.addItem(lama.getGenre());
                genre.setSelectedItem(lama.getGenre());
            }
        }

        JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
        form.setPreferredSize(new Dimension(350, 260));
        form.add(new JLabel("ISBN:"));
        form.add(isbn);
        form.add(new JLabel("Judul buku:"));
        form.add(judul);
        form.add(new JLabel("Penulis:"));
        form.add(penulis);
        form.add(new JLabel("Genre:"));
        form.add(genre);

        int hasil = JOptionPane.showConfirmDialog(
                this, form,
                edit ? "Edit Data Buku" : "Tambah Buku Baru",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (hasil != JOptionPane.OK_OPTION) {
            return;
        }

        String kategori = (String) genre.getSelectedItem();
        Buku buku = new Buku(
                isbn.getText().trim(),
                judul.getText().trim(),
                penulis.getText().trim(),
                kategori == null ? "" : kategori);

        try {
            if (edit) {
                controller.edit(lama.getIsbn(), buku);
                JOptionPane.showMessageDialog(this, "Data buku diperbarui!");
            } else {
                controller.tambah(buku);
                JOptionPane.showMessageDialog(this,
                        "Buku berhasil ditambahkan!");
            }
            muatTabel();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Periksa kembali", JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            tampilkanError("Gagal menyimpan data", e);
        }
    }

    private void editBuku() {
        int baris = tabel.getSelectedRow();

        if (baris == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih buku yang ingin diedit.");
            return;
        }

        dialogBuku(new Buku(
                model.getValueAt(baris, 0).toString(),
                model.getValueAt(baris, 1).toString(),
                model.getValueAt(baris, 2).toString(),
                model.getValueAt(baris, 3).toString()));
    }

    private void hapusBuku() {
        int baris = tabel.getSelectedRow();

        if (baris == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih buku yang ingin dihapus.");
            return;
        }

        String isbn = model.getValueAt(baris, 0).toString();
        String judul = model.getValueAt(baris, 1).toString();

        int konfirmasi = JOptionPane.showConfirmDialog(
                this, "Hapus buku \"" + judul + "\"?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (konfirmasi != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            controller.hapus(isbn);
            JOptionPane.showMessageDialog(this, "Buku berhasil dihapus.");
            muatTabel();

        } catch (Exception e) {
            tampilkanError("Gagal menghapus buku", e);
        }
    }

    // ---------------------------------------------------------------
    // Laporan PDF (JasperReports)
    // ---------------------------------------------------------------

    private void cetakLaporan() {
        JFileChooser pemilih = new JFileChooser();
        pemilih.setDialogTitle("Simpan Laporan Data Buku");
        pemilih.setSelectedFile(new File("laporan_buku.pdf"));
        pemilih.setFileFilter(new FileNameExtensionFilter("PDF (*.pdf)", "pdf"));

        if (pemilih.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File pilihan = pemilih.getSelectedFile();
        final File tujuan = pilihan.getName().toLowerCase().endsWith(".pdf")
                ? pilihan
                : new File(pilihan.getAbsolutePath() + ".pdf");

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                laporanService.cetakLaporanBuku(controller.cari(""), tujuan);
                return null;
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());

                try {
                    get();
                } catch (ExecutionException e) {
                    tampilkanError("Gagal membuat laporan",
                            e.getCause() instanceof Exception
                                    ? (Exception) e.getCause() : e);
                    return;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                int buka = JOptionPane.showConfirmDialog(
                        MainFrame.this,
                        "Laporan berhasil dibuat di:\n"
                        + tujuan.getAbsolutePath()
                        + "\n\nBuka file sekarang?",
                        "Berhasil",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE);

                if (buka == JOptionPane.YES_OPTION
                        && Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().open(tujuan);
                    } catch (Exception e) {
                        tampilkanError("File tidak bisa dibuka otomatis", e);
                    }
                }
            }
        }.execute();
    }

    private void tampilkanError(String pesan, Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
                pesan + ":\n" + e.getMessage(),
                "Kesalahan",
                JOptionPane.ERROR_MESSAGE);
    }
}
