# uBiblio - Personal Library Management

Gabungan dari 4 versi proyek (JDBC, Hibernate, Spring Boot, Spring Boot + JasperReports) menjadi satu aplikasi:

- **Tampilan** Swing (sidebar, dashboard, koleksi buku) dari versi JDBC
- **Struktur MVC**: `model` / `repository` / `service` / `controller` / `view`
- **Hibernate/JPA** lewat Spring Data JPA (tabel dibuat otomatis)
- **Cetak laporan PDF** dengan JasperReports
- **MySQL** (XAMPP)

## Cara menjalankan di NetBeans

1. Install **JDK 17 atau lebih baru** dan **Apache NetBeans** (sudah termasuk Maven).
2. Buka **XAMPP Control Panel**, klik **Start** pada **MySQL**. (Apache tidak perlu.)
3. Di NetBeans: **File > Open Project...**, pilih folder `uBiblio` (ikon cangkir kopi/Maven), klik **Open Project**.
4. Tunggu NetBeans selesai mengunduh dependency (lihat progress bar kanan bawah). Butuh internet pada pertama kali.
5. Klik kanan project > **Clean and Build**. Pastikan akhirnya `BUILD SUCCESS`.
6. Klik kanan project > **Run** (atau tekan **F6**). Jendela uBiblio akan muncul.

Database `ubiblio_db` dan tabel `buku` dibuat otomatis, dan 3 buku contoh diisi jika tabel masih kosong.
Tidak perlu import SQL (file `database/schema.sql` hanya referensi).

Alternatif tanpa NetBeans: jalankan `mvn clean package`, lalu `java -jar target/uBiblio-1.0.jar` (atau klik `jalankan.bat` di Windows).

## Jika ada masalah

| Gejala | Solusi |
|---|---|
| Dialog "uBiblio gagal dijalankan" / Communications link failure | MySQL di XAMPP belum Start, atau port bukan 3306 |
| Access denied for user 'root' | Samakan `spring.datasource.password` di `src/main/resources/application.properties` |
| Port 3306 dipakai MySQL lain | Matikan MySQL lain atau ubah port di `application.properties` |
| Dependency merah / tidak terunduh | Pastikan internet aktif, klik kanan project > **Reload POM** lalu **Clean and Build** |
| `release version 17 not supported` | Pilih JDK 17+ di **Tools > Java Platforms**, lalu klik kanan project > Properties > Build > Compile |

## Cetak laporan PDF

Menu **Koleksi Buku > Cetak Laporan PDF**, pilih lokasi simpan. Template ada di `src/main/resources/reports/laporan_buku.jrxml`.

## Struktur

```
src/main/java/com/ubiblio/
  UBiblioApplication.java      titik masuk (main)
  model/Buku.java              entity JPA
  repository/BukuRepository    akses data
  service/BukuService          logika bisnis (validasi, ISBN duplikat)
  service/LaporanService       JasperReports -> PDF
  service/DataAwal             data contoh
  controller/BukuController    penghubung view dan service
  view/MainFrame, SwingRunner  tampilan Swing
```
