package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.domain.model.Location;
import com.pti_sa.inventory_system.domain.port.ILocationRepository;
import com.pti_sa.inventory_system.infrastructure.entity.LocationEntity;
import com.pti_sa.inventory_system.infrastructure.mapper.LocationMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LocationJpaRepositoryImpl implements ILocationRepository {

    private final ILocationJpaRepository iLocationJpaRepository;
    private final LocationMapper locationMapper;

    public LocationJpaRepositoryImpl(ILocationJpaRepository iLocationJpaRepository, LocationMapper locationMapper) {
        this.iLocationJpaRepository = iLocationJpaRepository;
        this.locationMapper = locationMapper;
    }

    @Override
    public Location save(Location location) {
        LocationEntity entity = locationMapper.toEntity(location);
        return locationMapper.toModel(iLocationJpaRepository.save(entity));
    }

    @Override
    public Location update(Location location) {
        LocationEntity entity = locationMapper.toEntity(location);
        return locationMapper.toModel(iLocationJpaRepository.save(entity));
    }

    @Override
    public Optional<Location> findById(Integer id) {
        return iLocationJpaRepository.findById(id).map(locationMapper::toModel);
    }

    @Override
    public Optional<Location> findByName(String name) {
        return iLocationJpaRepository.findByName(name)
                .map(locationMapper::toModel);
    }

    @Override
    public void deleteById(Integer id) {
        iLocationJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return iLocationJpaRepository.existsByName(name);
    }

    @Override
    public List<Location> findAll() {
        return iLocationJpaRepository.findAll()
                .stream()
                .map(locationMapper::toModel)
                .collect(Collectors.toList());
    }
}