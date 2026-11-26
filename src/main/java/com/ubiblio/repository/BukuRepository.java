package com.ubiblio.repository;

import com.ubiblio.model.Buku;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BukuRepository extends JpaRepository<Buku, String> {

    // Pencarian di ISBN, judul, penulis, dan genre (seperti versi JDBC)
    @Query("select b from Buku b "
            + "where lower(b.isbn) like lower(concat('%', :kata, '%')) "
            + "or lower(b.judul) like lower(concat('%', :kata, '%')) "
            + "or lower(b.penulis) like lower(concat('%', :kata, '%')) "
            + "or lower(b.genre) like lower(concat('%', :kata, '%')) "
            + "order by b.judul")
    List<Buku> cari(@Param("kata") String kata);

    @Query("select count(distinct lower(b.genre)) from Buku b")
    long hitungGenre();
}
