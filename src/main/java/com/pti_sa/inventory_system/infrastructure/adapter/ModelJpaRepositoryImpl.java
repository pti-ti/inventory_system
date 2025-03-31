package com.pti_sa.inventory_system.infrastructure.adapter;


import com.pti_sa.inventory_system.domain.model.Model;
import com.pti_sa.inventory_system.domain.port.IModelRepository;
import com.pti_sa.inventory_system.infrastructure.entity.ModelEntity;
import com.pti_sa.inventory_system.infrastructure.mapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ModelJpaRepositoryImpl implements IModelRepository {

    private final IModelJpaRepository iModelJpaRepository;
    private final ModelMapper modelMapper;

    public ModelJpaRepositoryImpl(IModelJpaRepository iModelJpaRepository, ModelMapper modelMapper) {
        this.iModelJpaRepository = iModelJpaRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public Model save(Model model) {
        ModelEntity modelEntity = modelMapper.toEntity(model);
        return modelMapper.toModel(iModelJpaRepository.save(modelEntity));
    }

    @Override
    public Model update(Model model) {
        ModelEntity modelEntity = modelMapper.toEntity(model);
        return modelMapper.toModel(iModelJpaRepository.save(modelEntity));
    }

    @Override
    public Optional<Model> findById(Integer id) {
        return iModelJpaRepository.findById(id).map(modelMapper::toModel);
    }

    @Override
    public void deleteById(Integer id) {
        iModelJpaRepository.deleteById(id);
    }

    @Override
    public List<Model> findAll() {
        return iModelJpaRepository.findAll().stream().map(modelMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> countDevicesByModel() {
        return Map.of();
    }
}