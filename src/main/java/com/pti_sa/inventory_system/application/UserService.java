package com.pti_sa.inventory_system.application;

import com.pti_sa.inventory_system.domain.model.User;
import com.pti_sa.inventory_system.domain.port.IUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final IUserRepository iUserRepository;

    public UserService(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    // Guardar usuario
    public User saveUser(User user){
        //Agregar validaciones despúes
        if(iUserRepository.existsByEmail(user.getEmail())){
            throw new IllegalArgumentException("El correo ya está registrado.");
        }
        user.createAudit(user.getCreatedBy()); // Auditoría al crear el usuario
        return iUserRepository.save(user);
    }

    // Buscar usuario por ID
    public Optional<User> findUserById(Integer id){
        return iUserRepository.findById(id);
    }

    // Buscar usuario por email
    public Optional<User>findUserByEmail(String email){
        return iUserRepository.findByEmail(email);
    }

    // Obtener todos los usuarios
    public List<User> findAllUsers(){
        return iUserRepository.findAll();
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
