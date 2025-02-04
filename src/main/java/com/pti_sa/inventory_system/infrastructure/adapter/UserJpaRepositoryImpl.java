package com.pti_sa.inventory_system.infrastructure.adapter;

import com.pti_sa.inventory_system.domain.model.User;
import com.pti_sa.inventory_system.domain.port.IUserRepository;
import com.pti_sa.inventory_system.infrastructure.entity.UserEntity;
import com.pti_sa.inventory_system.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserJpaRepositoryImpl implements IUserRepository {
    private final IUserJpaRepository iUserJpaRepository;
    private final UserMapper userMapper;

    public UserJpaRepositoryImpl(IUserJpaRepository iUserJpaRepository, UserMapper userMapper) {
        this.iUserJpaRepository = iUserJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userMapper.toEntity(user);
        return userMapper.toModel(iUserJpaRepository.save(userEntity));
    }

    @Override
    public User update(User user) {
        UserEntity userEntity = userMapper.toEntity(user);
        return userMapper.toModel(iUserJpaRepository.save(userEntity));
    }

    @Override
    public Optional<User> findById(Integer id) {
        return iUserJpaRepository.findById(id).map(userMapper::toModel);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return iUserJpaRepository.findByEmail(email).map(userMapper::toModel);
    }

    @Override
    public List<User> findAll() {
        return iUserJpaRepository.findAll().stream().map(userMapper::toModel).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer id) {
        iUserJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return iUserJpaRepository.existsByEmail(email);
    }
}
