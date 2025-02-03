package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.domain.model.Status;
import com.pti_sa.inventory_system.domain.port.IStatusRepository;
import com.pti_sa.inventory_system.infrastructure.entity.StatusEntity;
import com.pti_sa.inventory_system.infrastructure.mapper.StatusMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class StatusJpaRepositoryImpl implements IStatusRepository {

    private final IStatusJpaRepository iStatusJpaRepository;
    private final StatusMapper statusMapper;

    public StatusJpaRepositoryImpl(IStatusJpaRepository iStatusJpaRepository, StatusMapper statusMapper) {
        this.iStatusJpaRepository = iStatusJpaRepository;
        this.statusMapper = statusMapper;
    }

    @Override
    public Status save(Status status) {
        StatusEntity statusEntity = statusMapper.toEntity(status);
        return statusMapper.toModel(iStatusJpaRepository.save(statusEntity));
    }

    @Override
    public Status update(Status status) {
        StatusEntity statusEntity = statusMapper.toEntity(status);
        return statusMapper.toModel(iStatusJpaRepository.save(statusEntity));
    }

    @Override
    public Optional<Status> findById(Integer id) {
        return iStatusJpaRepository.findById(id).map(statusMapper::toModel);
    }

    @Override
    public void deleteById(Integer id) {
        iStatusJpaRepository.deleteById(id);
    }

    @Override
    public List<Status> findAll() {
        return iStatusJpaRepository.findAll().stream().map(statusMapper::toModel).collect(Collectors.toList());
    }
}
