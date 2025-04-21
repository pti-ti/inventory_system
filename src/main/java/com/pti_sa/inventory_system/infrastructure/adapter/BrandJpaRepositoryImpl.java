package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.domain.model.Brand;
import com.pti_sa.inventory_system.domain.port.IBrandRepository;
import com.pti_sa.inventory_system.infrastructure.entity.BrandEntity;
import com.pti_sa.inventory_system.infrastructure.mapper.BrandMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class BrandJpaRepositoryImpl implements IBrandRepository {

    private final IBrandJpaRepository iBrandJpaRepository;
    private final BrandMapper brandMapper;

    public BrandJpaRepositoryImpl(IBrandJpaRepository iBrandJpaRepository, BrandMapper brandMapper) {
        this.iBrandJpaRepository = iBrandJpaRepository;
        this.brandMapper = brandMapper;
    }

    @Override
    public Brand save(Brand brand) {
        BrandEntity brandEntity = brandMapper.toEntity(brand);
        return brandMapper.toModel(iBrandJpaRepository.save(brandEntity));
    }

    @Override
    public Brand update(Brand brand) {
        BrandEntity brandEntity = brandMapper.toEntity(brand);
        return brandMapper.toModel(iBrandJpaRepository.save(brandEntity));
    }

    @Override
    public Optional<Brand> findById(Integer id) {
        return iBrandJpaRepository.findById(id).map(brandMapper::toModel);
    }

    @Override
    public void deleteById(Integer id) {
        iBrandJpaRepository.deleteById(id);
    }

    @Override
    public List<Brand> findAll() {
        return iBrandJpaRepository.findAll().stream().map(brandMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> countDevicesByBrand() {
        return Map.of();
    }



    /*@Override
    public Map<String, Long> countDevicesByBrand() {
        return iBrandJpaRepository.countDevicesByBrand()
                .stream()
                .collect(Collectors.toMap(
                        record -> (String) record[0],   // Nombre del estado
                        record -> (Long) record[1]
                ));
    }*/
}