package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.domain.model.Logbook;
import com.pti_sa.inventory_system.domain.port.ILogbookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogbookService {
    private final ILogbookRepository iLogbookRepository;

    public LogbookService(ILogbookRepository iLogbookRepository) {
        this.iLogbookRepository = iLogbookRepository;
    }

    // Guardar un registro de bitácora
    public Logbook saveLogbook(Logbook logbook){
        logbook.createAudit(logbook.getCreatedBy()); // Auditoría
        return iLogbookRepository.save(logbook);
    }

    // Actualizar un registro de bitácora
    public Logbook updatedLogbook(Logbook logbook){
        logbook.createAudit(logbook.getUpdatedBy()); // Auditoría
        return iLogbookRepository.update(logbook);
    }

    // Buscar un registro de bitácora por su id
    public Optional<Logbook> findLogbookById(Integer id){
        return iLogbookRepository.findById(id);
    }

    // Obtener todos los registros de bitácora
    public List<Logbook> findAllLogbooks(){
        return iLogbookRepository.findAll();
    }

    // Buscar registros de bitácora por DeviceId
    public List<Logbook> findLogbookByDeviceId(Integer deviceId){
        return iLogbookRepository.findByDeviceId(deviceId);
    }

    // Buscar registros de bitácora por UserId
    public List<Logbook> findLogbooksByUserId(Integer userId){
        return iLogbookRepository.findByUserId(userId);
    }

    // Buscar registros de bitácora por LocationId
    public List<Logbook> findLogbooksByLocationId(Integer locationId){
        return iLogbookRepository.findByLocationId(locationId);
    }

    // Eliminar un registro de bitácora por su ID
    public void deleteLogbookById(Integer id){
        iLogbookRepository.deleteById(id);
    }

}
