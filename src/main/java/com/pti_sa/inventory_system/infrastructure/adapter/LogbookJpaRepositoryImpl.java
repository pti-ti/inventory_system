package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.domain.model.Logbook;
import com.pti_sa.inventory_system.domain.port.ILogbookRepository;
import com.pti_sa.inventory_system.infrastructure.entity.LogbookEntity;
import com.pti_sa.inventory_system.infrastructure.mapper.LogbookMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LogbookJpaRepositoryImpl implements ILogbookRepository {

    private final ILogbookJpaRepository iLogbookJpaRepository;
    private final LogbookMapper logbookMapper;

    public LogbookJpaRepositoryImpl(ILogbookJpaRepository iLogbookJpaRepository, LogbookMapper logbookMapper) {
        this.iLogbookJpaRepository = iLogbookJpaRepository;
        this.logbookMapper = logbookMapper;
    }

    @Override
    public Logbook save(Logbook logbook) {
        LogbookEntity entity = logbookMapper.toEntity(logbook);
        return logbookMapper.toModel(iLogbookJpaRepository.save(entity));
    }

    @Override
    public Logbook update(Logbook logbook) {
        LogbookEntity entity = logbookMapper.toEntity(logbook);
        return logbookMapper.toModel(iLogbookJpaRepository.save(entity));
    }

    @Override
    public Optional<Logbook> findById(Integer id) {
        return iLogbookJpaRepository.findById(id).map(logbookMapper::toModel);
    }

    @Override
    public void deleteById(Integer id) {
        iLogbookJpaRepository.deleteById(id);
    }

    @Override
    public List<Logbook> findAll() {
        return iLogbookJpaRepository.findAll()
                .stream()
                .map(logbookMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Logbook> findAllByDeletedFalse() {
        return iLogbookJpaRepository.findAllByDeletedFalse()
                .stream()
                .map(logbookMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Logbook> findByDeviceId(Integer deviceId) {
        return iLogbookJpaRepository.findByDeviceId(deviceId)
                .stream()
                .map(logbookMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Logbook> findByUserId(Integer userId) {
        return iLogbookJpaRepository.findByUserId(userId)
                .stream()
                .map(logbookMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Logbook> findByLocationId(Integer locationId) {
        return List.of();
    }

    @Override
    public List<Logbook> findByStatusName(String statusName) {
        return iLogbookJpaRepository.findByStatusName(statusName).stream()
                .map(logbookMapper::toModel)
                .toList();
    }

    @Override
    public List<Logbook> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return iLogbookJpaRepository.findByCreatedAtBetween(startDate, endDate)
                .stream()
                .map(logbookMapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Logbook> findLatestLogbook() {
        return iLogbookJpaRepository.findFirstByOrderByIdDesc()
                .map(logbookMapper::toModel);

    }

}
