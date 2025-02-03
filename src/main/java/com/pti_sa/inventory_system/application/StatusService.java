package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.domain.model.Status;
import com.pti_sa.inventory_system.domain.port.IStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusService {
    private final IStatusRepository iStatusRepository;

    public StatusService(IStatusRepository iStatusRepository) {
        this.iStatusRepository = iStatusRepository;
    }

    //Guardar estado
    public Status saveStatus(Status status){
        status.createAudit(status.getCreatedBy()); // Auditoría
        return iStatusRepository.save(status);
    }

    // Actualizar estado
    public Status updateStatus(Status status){
        status.updateAudit(status.getUpdatedBy());
        return iStatusRepository.update(status);
    }

    // Buscar estado por su ID
    public Optional<Status> findStatusById(Integer id){
        return iStatusRepository.findById(id);
    }

    // Obtener todos los estados
    public List<Status> findAllStatuses(){
        return iStatusRepository.findAll();
    }

    // Eliminar un estado por su ID
    public void deleteStatusById(Integer id){
        iStatusRepository.deleteById(id);
    }
}
