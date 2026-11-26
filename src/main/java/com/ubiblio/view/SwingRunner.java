package com.ubiblio.view;

import com.ubiblio.controller.BukuController;
import com.ubiblio.service.LaporanService;
import javax.swing.SwingUtilities;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class SwingRunner implements CommandLineRunner {

    private final BukuController controller;
    private final LaporanService laporanService;
    private final ConfigurableApplicationContext context;

    public SwingRunner(BukuController controller,
                       LaporanService laporanService,
                       ConfigurableApplicationContext context) {
        this.controller = controller;
        this.laporanService = laporanService;
        this.context = context;
    }

    @Override
    public void run(String... args) {
        SwingUtilities.invokeLater(() ->
                new MainFrame(controller, laporanService, context)
                        .setVisible(true));
    }
}
