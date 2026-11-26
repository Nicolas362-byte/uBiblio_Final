package com.ubiblio.service;

import com.ubiblio.model.Buku;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.stereotype.Service;

@Service
public class LaporanService {

    public void cetakLaporanBuku(List<Buku> daftarBuku, File fileTujuan)
            throws Exception {

        try (InputStream template = getClass()
                .getResourceAsStream("/reports/laporan_buku.jrxml")) {

            if (template == null) {
                throw new IllegalStateException(
                        "Template laporan_buku.jrxml tidak ditemukan.");
            }

            JasperReport report = JasperCompileManager.compileReport(template);

            Map<String, Object> parameter = new HashMap<>();
            parameter.put("tanggalCetak", LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
            parameter.put("totalBuku", daftarBuku.size());

            JasperPrint hasil = JasperFillManager.fillReport(
                    report, parameter,
                    new JRBeanCollectionDataSource(daftarBuku));

            JasperExportManager.exportReportToPdfFile(
                    hasil, fileTujuan.getAbsolutePath());
        }
    }
}
