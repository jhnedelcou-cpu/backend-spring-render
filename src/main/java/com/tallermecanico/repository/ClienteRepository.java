package com.tallermecanico.repository;

import java.util.Optional;
import java.util.List; // Importante agregar esta importación

import org.springframework.data.jpa.repository.JpaRepository;
import com.tallermecanico.entity.ClienteEntity;

public interface ClienteRepository 
        extends JpaRepository<ClienteEntity, Long> {

    Optional<ClienteEntity> findByCedula(String cedula);
    
    Optional<ClienteEntity> findByCuit(String cuit);

    // 🟢 Agregá esta línea aquí:
    List<ClienteEntity> findByRemindBirthdayTrue();
    
}