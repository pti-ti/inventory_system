package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.domain.model.Maintenance;
import com.pti_sa.inventory_system.domain.port.IMaintenanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaintenanceService {
    private final IMaintenanceRepository iMaintenanceRepository;

    public MaintenanceService(IMaintenanceRepository iMaintenanceRepository) {
        this.iMaintenanceRepository = iMaintenanceRepository;
    }

    // Guardar un mantenimiento
    public Maintenance saveMaintenance(Maintenance maintenance){
        maintenance.createAudit(maintenance.getCreatedBy()); // Auditoria
        return iMaintenanceRepository.save(maintenance);
    }

    // Actualizar un mantenimiento
    public Maintenance updateMaintenance(Maintenance maintenance){
        maintenance.createAudit(maintenance.getUpdatedBy()); // Auditoría
        return iMaintenanceRepository.update(maintenance);
    }

    // Buscar un mantenimiento por su ID
    public Optional<Maintenance> findMaintenanceById(Integer id){
        return iMaintenanceRepository.findById(id);
    }

    // Obtener todos los mantenimientos
    public List<Maintenance> findAllMaintenances(){
        return iMaintenanceRepository.findAll();
    }

    // Buscar mantenimientos por el ID del dispositivo
    public List<Maintenance> findMaintenancesByDeviceId(Integer deviceId){
        return iMaintenanceRepository.findByDeviceId(deviceId);
    }

    // Eliminar un mantenimiento por su ID
    public void deleteMaintenanceById(Integer id){
        iMaintenanceRepository.deleteById(id);
    }
}
