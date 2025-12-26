package com.tallermecanico.controller;

import com.tallermecanico.dto.ClienteDTO;
import com.tallermecanico.dto.ClienteResponseDTO;
import com.tallermecanico.dto.PhoneDTO;
import com.tallermecanico.dto.VehiculoDTO;
import com.tallermecanico.entity.ClienteEntity;
import com.tallermecanico.entity.PhoneEntity;
import com.tallermecanico.entity.VehiculoEntity;
import com.tallermecanico.service.ClienteService;

import io.swagger.v3.oas.annotations.tags.Tag;

import com.tallermecanico.api.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Gestión de clientes del taller mecánico")
public class ClienteController {

    @Autowired
    private ClienteService service;

    // ---------------------------
    // Crear cliente
    // ---------------------------
    @PostMapping
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> crearCliente(@Valid @RequestBody ClienteDTO dto) {
        ClienteEntity cliente = mapearDtoAEntity(dto);
        ClienteEntity clienteGuardado = service.guardar(cliente);

        ClienteResponseDTO responseDto = mapearEntityAResponse(clienteGuardado);

        return ResponseEntity.ok(ApiResponse.success(responseDto, "Cliente creado con éxito"));
    }

    // ---------------------------
    // Listar todos los clientes
    // ---------------------------
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ClienteResponseDTO>>> listarClientes(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "15") int size
    ) {
        // 1. Definimos la paginación (Ordenado por ID descendente para ver los nuevos arriba)
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        // 2. El service debe devolver un Page de la Entidad
        Page<ClienteEntity> paginaEntities = service.listarPaginado(pageable);

        // 3. Mapeamos la página de Entidades a una página de DTOs
        Page<ClienteResponseDTO> paginaDtos = paginaEntities.map(this::mapearEntityAResponse);

        // 4. Devolvemos el objeto Page completo
        return ResponseEntity.ok(ApiResponse.success(paginaDtos, "Lista de clientes paginada"));
    }
    // ---------------------------
    // Obtener cliente por ID
    // ---------------------------
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> obtenerCliente(@PathVariable Long id) {
        ClienteEntity cliente = service.buscarPorId(id);
        if (cliente == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Cliente no encontrado"));
        }

        ClienteResponseDTO dto = mapearEntityAResponse(cliente);
        return ResponseEntity.ok(ApiResponse.success(dto, "Cliente encontrado"));
    }

    // ---------------------------
    // Actualizar cliente
    // ---------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> actualizarCliente(@PathVariable Long id,
                                                                             @Valid @RequestBody ClienteDTO dto) {
        ClienteEntity clienteExistente = service.buscarPorId(id);
        if (clienteExistente == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Cliente no encontrado"));
        }

        ClienteEntity clienteActualizado = mapearDtoAEntity(dto);
        clienteActualizado.setId(id);

        ClienteEntity guardado = service.guardar(clienteActualizado);
        ClienteResponseDTO responseDto = mapearEntityAResponse(guardado);

        return ResponseEntity.ok(ApiResponse.success(responseDto, "Cliente actualizado correctamente"));
    }

    // ---------------------------
    // Eliminar cliente
    // ---------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminarCliente(@PathVariable Long id) {
        ClienteEntity cliente = service.buscarPorId(id);
        if (cliente == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Cliente no encontrado"));
        }

        service.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Cliente eliminado correctamente"));
    }
    
    @GetMapping("/notificaciones/cumpleanios")
    public ResponseEntity<ApiResponse<List<ClienteResponseDTO>>> obtenerProximosCumpleanios() {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(7);

        // 1. Pedimos al service los clientes que tienen el tilde de alerta
        List<ClienteEntity> clientesConAlerta = service.listarParaNotificaciones();

        // 2. Filtramos por fecha y mapeamos a DTO
        List<ClienteResponseDTO> proximos = clientesConAlerta.stream()
                .filter(c -> {
                    if (c.getBirthDate() == null) return false;
                    
                    // Ajustamos el cumple al año actual para comparar
                    LocalDate cumpleEsteAnio = c.getBirthDate().withYear(hoy.getYear());
                    
                    // Si el cumple ya pasó este año, calculamos para el próximo
                    if (cumpleEsteAnio.isBefore(hoy)) {
                        cumpleEsteAnio = cumpleEsteAnio.plusYears(1);
                    }

                    // Verificamos si está dentro de los próximos 7 días
                    return (cumpleEsteAnio.isEqual(hoy) || cumpleEsteAnio.isAfter(hoy)) 
                            && (cumpleEsteAnio.isBefore(limite) || cumpleEsteAnio.isEqual(limite));
                })
                .map(this::mapearEntityAResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(proximos, "Lista de alertas cargada"));
    }
    // ---------------------------
    // Mapper: DTO → Entity
    // ---------------------------
    private ClienteEntity mapearDtoAEntity(ClienteDTO dto) {
        ClienteEntity cliente = new ClienteEntity();

        cliente.setAccountNumber(dto.getAccountNumber());
        cliente.setClientType(dto.getClientType());
        cliente.setFirstName(dto.getFirstName());
        cliente.setLastName(dto.getLastName());
        cliente.setDocType(dto.getDocType());
        cliente.setDocumentNumber(dto.getDocumentNumber());
        cliente.setCedula(dto.getCedula());

        if (dto.getBirthDate() != null && !dto.getBirthDate().isEmpty()) {
            cliente.setBirthDate(LocalDate.parse(dto.getBirthDate(), DateTimeFormatter.ISO_DATE));
        }

        cliente.setClientGroup(dto.getClientGroup());
        cliente.setCurrency(dto.getCurrency());
        cliente.setConditions(dto.getConditions());
        cliente.setPais(dto.getPais());
        cliente.setProvincia(dto.getProvincia());
        cliente.setCiudad(dto.getCiudad());
        cliente.setStreet(dto.getStreet());
        cliente.setZipCode(dto.getZipCode());
        cliente.setLicenseId(dto.getLicenseId());
        cliente.setEmail(dto.getEmail());
        cliente.setCuit(dto.getCuit());
        cliente.setCondicionIva(dto.getCondicionIva());
        cliente.setRemindBirthday(dto.isRemindBirthday());
        cliente.setObservacion(dto.getObservacion());

        // Phones
        if (dto.getPhones() != null) {
            List<PhoneEntity> phones = dto.getPhones().stream().map(pdto -> {
                PhoneEntity phone = new PhoneEntity();
                phone.setType(pdto.getType());
                phone.setAreaCode(pdto.getAreaCode());
                phone.setNumber(pdto.getNumber());
                return phone;
            }).collect(Collectors.toList());
            cliente.setPhones(phones);
        }

        // Vehiculos
        if (dto.getVehiculosAsociados() != null) {
            List<VehiculoEntity> vehiculos = dto.getVehiculosAsociados().stream().map(vdto -> {
                VehiculoEntity vehiculo = new VehiculoEntity();
                vehiculo.setVehicleId(vdto.getVehicleId());
                return vehiculo;
            }).collect(Collectors.toList());
            cliente.setVehiculosAsociados(vehiculos);
        }

        return cliente;
    }

    // ---------------------------
    // Mapper: Entity → ResponseDTO
    // ---------------------------
    private ClienteResponseDTO mapearEntityAResponse(ClienteEntity entity) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(entity.getId());
        dto.setAccountNumber(entity.getAccountNumber());
        dto.setClientType(entity.getClientType());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setDocType(entity.getDocType());
        dto.setDocumentNumber(entity.getDocumentNumber());
        dto.setBirthDate(entity.getBirthDate() != null ? entity.getBirthDate().toString() : null);
        dto.setClientGroup(entity.getClientGroup());
        dto.setCurrency(entity.getCurrency());
        dto.setConditions(entity.getConditions());
        dto.setPais(entity.getPais());
        dto.setProvincia(entity.getProvincia());
        dto.setCiudad(entity.getCiudad());
        dto.setStreet(entity.getStreet());
        dto.setZipCode(entity.getZipCode());
        dto.setLicenseId(entity.getLicenseId());
        dto.setEmail(entity.getEmail());
        dto.setCuit(entity.getCuit());
        dto.setCondicionIva(entity.getCondicionIva());
        dto.setRemindBirthday(entity.isRemindBirthday());
        dto.setObservacion(entity.getObservacion());
        dto.setCedula(entity.getCedula());
        
     // --- NUEVO: Pasar fechas de auditoría al DTO ---
        dto.setFechaAlta(entity.getFechaAlta());
        dto.setFechaModificacion(entity.getFechaModificacion());
        // ----------------------------------------------

        // Phones mapping... (tu código existente)
        // Vehiculos mapping... (tu código existente)
        if(entity.getPhones() != null) {
            dto.setPhones(entity.getPhones().stream().map(phone -> {
                PhoneDTO p = new PhoneDTO();
                p.setType(phone.getType());
                p.setAreaCode(phone.getAreaCode());
                p.setNumber(phone.getNumber());
                return p;
            }).collect(Collectors.toList()));
        }

        if(entity.getVehiculosAsociados() != null) {
            dto.setVehiculosAsociados(entity.getVehiculosAsociados().stream().map(v -> {
                VehiculoDTO vdto = new VehiculoDTO();
                vdto.setVehicleId(v.getVehicleId());
                return vdto;
            }).collect(Collectors.toList()));
        }

        return dto;
    }
    
    

}


