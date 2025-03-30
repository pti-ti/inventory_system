package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.application.dto.response.BrandResponseDTO;
import com.pti_sa.inventory_system.domain.model.Brand;
import com.pti_sa.inventory_system.domain.port.IBrandRepository;
import com.pti_sa.inventory_system.infrastructure.mapper.BrandMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandService {
    private final IBrandRepository iBrandRepository;
    private final BrandMapper brandMapper;

    public BrandService(IBrandRepository iBrandRepository, BrandMapper brandMapper) {
        this.iBrandRepository = iBrandRepository;
        this.brandMapper = brandMapper;
    }

    // Guardar Marca
    public BrandResponseDTO saveBrand(Brand brand, Integer userId){
        if(brand.getCreatedBy() == null){
            brand.setCreatedBy(userId);
        }

        brand.createAudit(userId);

        Brand savedBrand = iBrandRepository.save(brand);
        return brandMapper.toDTO(savedBrand);
    }

    // Actualizar marca
    public Brand updateBrand(Brand brand){
        brand.updateAudit(brand.getUpdatedBy());
        return iBrandRepository.update(brand);
    }

    // Buscar marca por su id
    public Optional<BrandResponseDTO>findBrandById(Integer id){
        return iBrandRepository.findById(id)
                .map(brandMapper::toDTO);
    }

    // Contar los dispositivos por su marca
    public Map<String, Long>countDevicesByBrand(){
        return iBrandRepository.countDevicesByBrand();
    }

    // Obtener todas las marcas
    public List<BrandResponseDTO> findAllBrands(){
        return iBrandRepository.findAll()
                .stream()
                .map(brandMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Eliminar una marca por su ID
    public void deleteBrandById(Integer id){
        iBrandRepository.deleteById(id);
    }

}