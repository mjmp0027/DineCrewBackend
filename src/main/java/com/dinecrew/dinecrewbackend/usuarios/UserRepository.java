package com.dinecrew.dinecrewbackend.usuarios;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByResetPasswordToken(String resetPasswordToken);
}
