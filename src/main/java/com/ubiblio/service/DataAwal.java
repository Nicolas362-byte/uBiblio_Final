package com.ubiblio.service;

import com.ubiblio.model.Buku;
import com.ubiblio.repository.BukuRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** Mengisi 3 buku contoh hanya jika tabel masih kosong. */
@Component
@Order(1)
public class DataAwal implements CommandLineRunner {

    private final BukuRepository repository;

    public DataAwal(BukuRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            repository.saveAll(List.of(
                    new Buku("978001", "Laskar Pelangi", "Andrea Hirata", "Novel"),
                    new Buku("978002", "Bumi", "Tere Liye", "Fantasi"),
                    new Buku("978003", "Pemrograman Java", "Abdul Kadir", "Teknologi")
            ));
        }
    }
}
