package com.tallermecanico.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tallermecanico.entity.ClienteEntity;
import com.tallermecanico.repository.ClienteRepository;

@Service
public class BirthdayAlertService {

    @Autowired
    private ClienteRepository clienteRepository;

    // Se ejecuta todos los días a las 09:00 AM
 //   @Scheduled(cron = "0 0 9 * * *")
    @Scheduled(fixedDelay = 10000) // Se ejecuta cada 10 segundos para la prueba
    public void checkUpcomingBirthdays() {
       // LocalDate hoy = LocalDate.now();
      //  LocalDate fechaObjetivo = hoy.plusDays(7); // Buscamos a los que cumplen en exactamente 7 días
    	LocalDate hoy = LocalDate.now();
        
        // CAMBIO PARA PRUEBA: En lugar de plusDays(7), usamos hoy directamente
        LocalDate fechaObjetivo = hoy;
        int mesBuscado = fechaObjetivo.getMonthValue();
        int diaBuscado = fechaObjetivo.getDayOfMonth();

        // Traemos solo los clientes que tienen el tilde de alerta activado
        List<ClienteEntity> clientesConAlerta = clienteRepository.findByRemindBirthdayTrue();

        for (ClienteEntity cliente : clientesConAlerta) {
            // INTEGRACIÓN: Verificamos que tenga fecha cargada para evitar NullPointerException
            if (cliente.getBirthDate() != null) {
                
                int mesCumple = cliente.getBirthDate().getMonthValue();
                int diaCumple = cliente.getBirthDate().getDayOfMonth();

                // Comparamos si el cumple cae justo en la fecha objetivo (una semana antes)
                if (mesCumple == mesBuscado && diaCumple == diaBuscado) {
                    enviarSmsAlDuenio(cliente);
                }
            }
        }
    }

    private void enviarSmsAlDuenio(ClienteEntity c) {
        // Este es el mensaje que te llegará avisándote a vos
        String mensaje = "ALERTA TALLER: En 7 días cumple años " + c.getFirstName() + " " + c.getLastName() + 
                         ". ¡Prepará su promoción!";
        
        // Por ahora lo vemos en la consola de Spring
        System.out.println(">>> NOTIFICACIÓN SISTEMA: " + mensaje);
        
        // TODO: Aquí irá la llamada al Bot de Telegram o Twilio
    }
}