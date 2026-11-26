package com.ubiblio.controller;

import com.ubiblio.model.Buku;
import com.ubiblio.service.BukuService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class BukuController {

    private final BukuService service;

    public BukuController(BukuService service) {
        this.service = service;
    }

    public List<Buku> cari(String kata) {
        return service.cari(kata);
    }

    public long total() {
        return service.total();
    }

    public long jumlahGenre() {
        return service.jumlahGenre();
    }

    public void tambah(Buku buku) {
        service.tambah(buku);
    }

    public void edit(String isbnLama, Buku buku) {
        service.edit(isbnLama, buku);
    }

    public void hapus(String isbn) {
        service.hapus(isbn);
    }
}
