package com.ubiblio;

import javax.swing.JOptionPane;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class UBiblioApplication {

    public static void main(String[] args) {
        try {
            // headless(false) wajib: tanpa ini Spring Boot melarang jendela Swing
            // dan muncul HeadlessException.
            new SpringApplicationBuilder(UBiblioApplication.class)
                    .headless(false)
                    .run(args);

        } catch (Throwable e) {
            e.printStackTrace();

            Throwable akar = e;
            while (akar.getCause() != null) {
                akar = akar.getCause();
            }

            JOptionPane.showMessageDialog(null,
                    "uBiblio gagal dijalankan.\n\n"
                    + "Pastikan MySQL di XAMPP sudah berstatus Start,\n"
                    + "lalu periksa username/password di application.properties.\n\n"
                    + "Detail: " + akar.getMessage(),
                    "uBiblio - Kesalahan",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}
