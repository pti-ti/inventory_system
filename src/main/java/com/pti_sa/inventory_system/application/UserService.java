package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.application.dto.response.UserResponseDTO;
import com.pti_sa.inventory_system.domain.model.Device;
import com.pti_sa.inventory_system.domain.model.Location;
import com.pti_sa.inventory_system.domain.model.User;
import com.pti_sa.inventory_system.domain.port.IDeviceRepository;
import com.pti_sa.inventory_system.domain.port.ILocationRepository;
import com.pti_sa.inventory_system.domain.port.IUserRepository;

import com.pti_sa.inventory_system.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final IUserRepository iUserRepository;
    private final ILocationRepository iLocationRepository;
    private final IDeviceRepository iDeviceRepository;
    private final UserMapper userMapper;

    public UserService(IUserRepository iUserRepository, ILocationRepository iLocationRepository, IDeviceRepository iDeviceRepository, UserMapper userMapper) {
        this.iUserRepository = iUserRepository;
        this.iLocationRepository = iLocationRepository;
        this.iDeviceRepository = iDeviceRepository;
        this.userMapper = userMapper;
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
        user.setLocation(location);
        user.createAudit(user.getCreatedBy());
        User savedUser = iUserRepository.save(user);
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
    public List<UserResponseDTO>findUserByEmail(String email){
        return iUserRepository.findByEmail(email)
                .stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener todos los usuarios
    public List<UserResponseDTO> findAllUsers(){
        return iUserRepository.findAll()
                .stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Obtener los usuarios por localidad
    public List<User> getUsersByLocation(Integer locationId){
        return iUserRepository.findByLocationId(locationId);
    }

    // Obtener los usuarios por id
    public Optional<User> getUserById(Integer id){
        return iUserRepository.findById(id);
    }


    // Eliminar un usuario por su ID
    public void deleteUserById(Integer id){
        User user = iUserRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setDeleted(true);
        iUserRepository.save(user);
    }

    // Verificar si el correo electrónico existe
    public boolean emailExists(String email){
        return iUserRepository.existsByEmail(email);
    }
}
