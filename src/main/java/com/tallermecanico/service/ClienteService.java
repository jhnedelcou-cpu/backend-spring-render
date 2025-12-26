package com.tallermecanico.service;

import com.tallermecanico.entity.ClienteEntity;
import com.tallermecanico.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.tallermecanico.exception.CedulaDuplicadaException;

// 🟢 NUEVOS IMPORTS PARA PAGINACIÓN
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    // 🟢 NUEVO MÉTODO: Listar con paginación
    public Page<ClienteEntity> listarPaginado(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // --- TUS MÉTODOS ANTERIORES SE MANTIENEN IGUAL ---

    public ClienteEntity guardar(ClienteEntity cliente) {
        Optional<ClienteEntity> existente = repository.findByCedula(cliente.getCedula());
        if (existente.isPresent()) {
            if (cliente.getId() == null || !existente.get().getId().equals(cliente.getId())) {
                throw new CedulaDuplicadaException("Ya existe un cliente con la cédula: " + cliente.getCedula());
            }
        }
        
        // 2. Validar CUIT Único (Solo si el CUIT no es nulo o vacío)
        if (cliente.getCuit() != null && !cliente.getCuit().trim().isEmpty()) {
            Optional<ClienteEntity> existenteCuit = repository.findByCuit(cliente.getCuit());
            if (existenteCuit.isPresent()) {
                // Error aquí corregido: usamos 'existenteCuit'
                if (cliente.getId() == null || !existenteCuit.get().getId().equals(cliente.getId())) {
                    // Sugerencia: Podrías crear una CuitDuplicadoException o usar una genérica
                    throw new CedulaDuplicadaException("Ya existe un cliente con el CUIT/CUIL: " + cliente.getCuit());
                }
            }
        }
        
        
   

        if (cliente.getId() == null) {
            long contador = repository.count() + 1;
            cliente.generarAccountNumber(contador);
        }

        return repository.save(cliente);
    }

    public List<ClienteEntity> listar() {
        return repository.findAll();
    }

    public ClienteEntity buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
    
    public List<ClienteEntity> listarTodos() {
        return repository.findAll();
    }
    
    public List<ClienteEntity> listarParaNotificaciones() {
        // Usamos el método que creamos en el Repository anteriormente
        return repository.findByRemindBirthdayTrue();
    }
    
    
}

