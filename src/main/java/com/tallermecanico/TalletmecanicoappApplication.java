package com.tallermecanico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing // 👈 Agrega esto
@EnableScheduling // <-- Esto activa el reloj interno
public class TalletmecanicoappApplication {

    public static void main(String[] args) {
        SpringApplication.run(TalletmecanicoappApplication.class, args);
    }
}