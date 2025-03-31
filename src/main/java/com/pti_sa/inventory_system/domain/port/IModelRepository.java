package com.pti_sa.inventory_system.domain.port;

import com.pti_sa.inventory_system.domain.model.Model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IModelRepository {

    Model save (Model model);
    Model update(Model model);
    Optional<Model>findById(Integer id);
    void deleteById(Integer id);
    List<Model> findAll();
    Map<String, Long> countDevicesByModel();
}
