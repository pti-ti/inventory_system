package com.pti_sa.inventory_system.application;


import com.pti_sa.inventory_system.application.dto.response.ModelResponseDTO;
import com.pti_sa.inventory_system.domain.model.Model;
import com.pti_sa.inventory_system.domain.port.IModelRepository;
import com.pti_sa.inventory_system.infrastructure.mapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModelService {
    private final IModelRepository iModelRepository;
    private final ModelMapper modelMapper;

    public ModelService(IModelRepository iModelRepository, ModelMapper modelMapper) {
        this.iModelRepository = iModelRepository;
        this.modelMapper = modelMapper;
    }

    // Guardar el modelo
    public ModelResponseDTO saveModel(Model model, Integer userId){
        if(model.getCreatedBy() == null){
            model.setCreatedBy(userId);
        }

        model.createAudit(userId);

        Model savedModel = iModelRepository.save(model);
        return modelMapper.toDTO(savedModel);
    }

    // Actualizar modelo
    public Model updateModel(Model model){
        model.updateAudit(model.getUpdatedBy());
        return iModelRepository.update(model);
    }

    // Buscar modelo por su id
    public Optional<ModelResponseDTO> findModelById(Integer id){
        return iModelRepository.findById(id)
                .map(modelMapper::toDTO);
    }

    // Contar los dispositivos por su marca
    public Map<String, Long> countDevicesByModel(){ return iModelRepository.countDevicesByModel();}

    // Obtener todas las marcas
    public List<ModelResponseDTO> findAllModels(){
        return iModelRepository.findAll()
                .stream()
                .map(modelMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Eliminar un modelo por su ID
    public void deleteModelById(Integer id){ iModelRepository.deleteById(id);}


}
