-- Opsional: aplikasi sudah membuat database & tabel otomatis saat pertama dijalankan.
-- File ini hanya untuk referensi / pembuatan manual lewat phpMyAdmin.

CREATE DATABASE IF NOT EXISTS ubiblio_db
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ubiblio_db;

CREATE TABLE IF NOT EXISTS buku (
    isbn    VARCHAR(20)  NOT NULL,
    judul   VARCHAR(150) NOT NULL,
    penulis VARCHAR(100) NOT NULL,
    genre   VARCHAR(50)  NOT NULL,
    PRIMARY KEY (isbn)
);
