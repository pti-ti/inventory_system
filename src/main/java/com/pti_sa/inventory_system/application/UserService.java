package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.application.dto.UserResponseDTO;
import com.pti_sa.inventory_system.domain.model.User;
import com.pti_sa.inventory_system.domain.port.IUserRepository;
import com.pti_sa.inventory_system.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final IUserRepository iUserRepository;
    private final UserMapper userMapper;

    public UserService(IUserRepository iUserRepository, UserMapper userMapper) {
        this.iUserRepository = iUserRepository;
        this.userMapper = userMapper;
    }

    // Guardar usuario
    public User saveUser(User user){
        //Agregar validaciones despúes
        if(iUserRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("El correo ya está registrado.");
        }
        user.createAudit(user.getCreatedBy()); // Auditoría al crear el usuario
        return iUserRepository.save(user);
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
