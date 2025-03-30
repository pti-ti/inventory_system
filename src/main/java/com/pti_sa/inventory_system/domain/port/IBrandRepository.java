package com.pti_sa.inventory_system.domain.port;

import com.pti_sa.inventory_system.domain.model.Brand;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IBrandRepository {

    Brand save (Brand brand);
    Brand update(Brand brand);
    Optional<Brand>findById(Integer id);
    void deleteById(Integer id);
    List<Brand> findAll();
    Map<String, Long> countDevicesByBrand();
}