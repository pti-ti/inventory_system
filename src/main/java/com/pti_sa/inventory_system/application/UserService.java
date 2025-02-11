package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.application.dto.response.UserResponseDTO;
import com.pti_sa.inventory_system.domain.model.Location;
import com.pti_sa.inventory_system.domain.model.User;
import com.pti_sa.inventory_system.domain.port.ILocationRepository;
import com.pti_sa.inventory_system.domain.port.IUserRepository;
import com.pti_sa.inventory_system.infrastructure.entity.LocationEntity;
import com.pti_sa.inventory_system.infrastructure.mapper.LocationMapper;
import com.pti_sa.inventory_system.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final IUserRepository iUserRepository;
    private final ILocationRepository iLocationRepository;
    private final UserMapper userMapper;
    private final LocationMapper locationMapper;

    public UserService(IUserRepository iUserRepository, ILocationRepository iLocationRepository, UserMapper userMapper, LocationMapper locationMapper) {
        this.iUserRepository = iUserRepository;
        this.iLocationRepository = iLocationRepository;
        this.userMapper = userMapper;
        this.locationMapper = locationMapper;
    }

    // Guardar usuario
    public UserResponseDTO saveUser(User user) {
        // Validar si el email ya está registrado
        if (iUserRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        // Validar si la ubicación está presente
        if (user.getLocation() == null || user.getLocation().getId() == null) {
            throw new RuntimeException("El ID de la ubicación del usuario no puede ser nulo.");
        }

        // Buscar la ubicación en la base de datos
        Location location = iLocationRepository.findById(user.getLocation().getId())
                .orElseThrow(() -> new RuntimeException("Ubicación no encontrada con ID: " + user.getLocation().getId()));

        // Asignar la ubicación validada al usuario
        user.setLocation(location);

        // Agregar datos de auditoría
        user.createAudit(user.getCreatedBy());

        // Guardar el usuario en la base de datos
        User savedUser = iUserRepository.save(user);

        // Convertir a DTO de respuesta y retornarlo
        return userMapper.toResponseDTO(savedUser);
    }

    // Actualizar usuario
    public User updateUser(User user){
        Optional<User> existingUser = iUserRepository.findById(user.getId());
        if(existingUser.isEmpty()){
            throw new RuntimeException("Usuario no encontrado");
        }

        user.updateAudit(user.getUpdatedBy()); // Auditoría al actualizar
        return iUserRepository.update(user);
    }

    // Buscar usuario por ID
    public Optional<UserResponseDTO> findUserById(Integer id){
        return iUserRepository.findById(id)
                .map(userMapper::toResponseDTO);
    }

    // Buscar usuario por email
    public Optional<UserResponseDTO>findUserByEmail(String email){
        return iUserRepository.findByEmail(email)
                .map(userMapper::toResponseDTO);
    }

    // Obtener todos los usuarios
    public List<UserResponseDTO> findAllUsers(){
        return iUserRepository.findAll()
                .stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    // Eliminar un usuario por su ID
    public void deleteUserById(Integer id){
        iUserRepository.deleteById(id);
    }

    // Verificar si el correo electrónico existe
    public boolean emailExists(String email){
        return iUserRepository.existsByEmail(email);
    }
}
