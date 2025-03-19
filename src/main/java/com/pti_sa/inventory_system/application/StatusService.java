package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.application.dto.response.LocationResponseDTO;
import com.pti_sa.inventory_system.application.dto.response.StatusResponseDTO;
import com.pti_sa.inventory_system.domain.model.Location;
import com.pti_sa.inventory_system.domain.model.Status;
import com.pti_sa.inventory_system.domain.port.IStatusRepository;
import com.pti_sa.inventory_system.infrastructure.mapper.StatusMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StatusService {
    private final IStatusRepository iStatusRepository;
    private final StatusMapper statusMapper;

    public StatusService(IStatusRepository iStatusRepository, StatusMapper statusMapper) {
        this.iStatusRepository = iStatusRepository;
        this.statusMapper = statusMapper;
    }

    //Guardar estado
    public StatusResponseDTO saveStatus(Status status, Integer userId) {
        if (status.getCreatedBy() == null) {
            status.setCreatedBy(userId);
        }

        status.createAudit(userId);  // 🔹 Ahora `createdBy` tiene un valor seguro

        Status savedStatus = iStatusRepository.save(status);
        return statusMapper.toDTO(savedStatus);
    }

    // Actualizar estado
    public Status updateStatus(Status status){
        status.updateAudit(status.getUpdatedBy());
        return iStatusRepository.update(status);
    }

    // Buscar estado por su ID
    public Optional<StatusResponseDTO> findStatusById(Integer id){
        return iStatusRepository.findById(id)
                .map(statusMapper::toDTO);
    }

    // Obtener todos los estados
    public List<StatusResponseDTO> findAllStatuses(){
        return iStatusRepository.findAll()
                .stream()
                .map(statusMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Eliminar un estado por su ID
    public void deleteStatusById(Integer id){
        iStatusRepository.deleteById(id);
    }
}
