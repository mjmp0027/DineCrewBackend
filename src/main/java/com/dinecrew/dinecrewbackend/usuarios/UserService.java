package com.dinecrew.dinecrewbackend.usuarios;

import com.dinecrew.dinecrewbackend.services.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Get user by username
    public User findByUsername(String username) {
        return repository.findByUsername(username).orElse(null);
    }

    // Get all users
    public List<User> findAll() {
        return repository.findAll();
    }

    // Save user
    public User save(User user) {
        return repository.save(user);
    }

    // Delete user
    public void delete(User user) {
        repository.delete(user);
    }

    public void sendPasswordResetToken(String username) {
        Optional<User> userOpt = repository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        User user = userOpt.get();
        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetPasswordExpires(new Date(System.currentTimeMillis() + 3600000));

        repository.save(user);

        String resetUrl = "http://192.168.1.182:3000/reset-password/" + token;
        String emailContent = "Para restablecer tu contraseña, haz clic en el siguiente enlace:\n" + resetUrl;

        try {
            emailService.sendEmail(user.getEmail(), "Restablecer contraseña", emailContent);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al enviar el correo electrónico");
        }
    }

    public void resetPassword(String token, String newPassword) {
        Optional<User> userOpt = repository.findByResetPasswordToken(token);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Token no válido");
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordExpires(null);

        repository.save(user);
    }
}
