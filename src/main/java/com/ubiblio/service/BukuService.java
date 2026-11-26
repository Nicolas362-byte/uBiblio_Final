package com.ubiblio.service;

import com.ubiblio.model.Buku;
import com.ubiblio.repository.BukuRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BukuService {

    private final BukuRepository repository;

    public BukuService(BukuRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Buku> cari(String kata) {
        String keyword = kata == null ? "" : kata.trim();
        return repository.cari(keyword);
    }

    @Transactional(readOnly = true)
    public long total() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public long jumlahGenre() {
        return repository.hitungGenre();
    }

    public void tambah(Buku buku) {
        validasi(buku);

        if (repository.existsById(buku.getIsbn())) {
            throw new IllegalArgumentException("ISBN sudah digunakan.");
        }

        repository.save(buku);
    }

    // ISBN boleh diubah saat edit (seperti versi JDBC).
    public void edit(String isbnLama, Buku bukuBaru) {
        validasi(bukuBaru);

        if (!repository.existsById(isbnLama)) {
            throw new IllegalArgumentException("Data buku tidak ditemukan.");
        }

        if (isbnLama.equals(bukuBaru.getIsbn())) {
            repository.save(bukuBaru);
            return;
        }

        if (repository.existsById(bukuBaru.getIsbn())) {
            throw new IllegalArgumentException("ISBN sudah digunakan.");
        }

        repository.deleteById(isbnLama);
        repository.flush();
        repository.save(bukuBaru);
    }

    public void hapus(String isbn) {
        if (!repository.existsById(isbn)) {
            throw new IllegalArgumentException("Data buku tidak ditemukan.");
        }

        repository.deleteById(isbn);
    }

    private void validasi(Buku b) {
        if (isKosong(b.getIsbn()) || isKosong(b.getJudul())
                || isKosong(b.getPenulis()) || isKosong(b.getGenre())) {
            throw new IllegalArgumentException("Semua data wajib diisi.");
        }
    }

    private boolean isKosong(String s) {
        return s == null || s.trim().isEmpty();
    }
}
