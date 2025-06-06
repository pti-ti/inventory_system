package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.domain.model.Location;
import com.pti_sa.inventory_system.domain.port.ILocationRepository;
import com.pti_sa.inventory_system.infrastructure.entity.LocationEntity;
import com.pti_sa.inventory_system.infrastructure.mapper.LocationMapper;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    @Override
    public Map<String, Long> countDevicesByLocation() {
        return iLocationJpaRepository.countDevicesByLocation()
                .stream()
                .collect(Collectors.toMap(
                        record -> (String) record[0],
                        record -> (Long) record[1]
                ));
    }

    @Override
    public Map<String, Map<String, Long>> countDevicesByLocationAndType() {
        List<Object[]> results = iLocationJpaRepository.countDevicesByLocationAndType();

        return results.stream()
                .collect(Collectors.groupingBy(
                        record -> (String) record[0], // Ubicación (l.name)
                        Collectors.toMap(
                                record -> (String) record[1], // Tipo de dispositivo (lb.device.type)
                                record -> {
                                    if (record[2] instanceof Number) {
                                        return ((Number) record[2]).longValue();
                                    } else {
                                        throw new IllegalArgumentException("Error: record[2] no es un número -> " + record[2]);
                                    }
                                }
                        )
                ));
    }
}