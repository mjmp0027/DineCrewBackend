package com.dinecrew.dinecrewbackend.usuarios;

import com.dinecrew.dinecrewbackend.mesas.Mesa;
import com.dinecrew.dinecrewbackend.mesas.MesaService;
import com.dinecrew.dinecrewbackend.services.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private MesaService mesaService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public long countUsers() {
        return repository.count();
    }

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

        repository.save(user);

        String resetUrl = "http://192.168.0.65:3000/reset-password/" + token;
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
            throw new RuntimeException("Ya ha pasado una hora desde que se solicitó el cambio de contraseña o ya ha cambiado la contraseña, por favor solicita un nuevo enlace para restablecer tu contraseña\n(vuelva a pulsar he olvidado mi contraseña)");
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);

        repository.save(user);
    }

    public void asignarMesas(String username) {
        User user = repository.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<Mesa> mesasLibres = mesaService.findAllByUserId(null);

        if (user.getMesasAsignadas().isEmpty()) {
            if (mesasLibres.isEmpty()) {
                throw new RuntimeException("No hay mesas disponibles");
            }

            List<String> mesasAsignadasList = new ArrayList<>(); // Lista con los IDs de las mesas asignadas
            // De las mesas libres se asignarán hasta un máximo de 5 mesas por camarero
            int mesasAsignadas = 0;
            for (Mesa mesa : mesasLibres) {
                if (mesasAsignadas >= 5) {
                    break;
                }
                mesa.setUserId(user.getId());
                mesa = mesaService.save(mesa);
                mesasAsignadasList.add(mesa.getId());
                mesasAsignadas++;
            }

            user.setMesasAsignadas(mesasAsignadasList);
            repository.save(user);
        }
    }

    public void liberarMesas(String username) {
        User user = repository.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<Mesa> mesasAsignadas = mesaService.findAllByUserId(user.getId());

        mesasAsignadas.forEach(mesa -> mesa.setUserId(null));
        mesaService.saveAll(mesasAsignadas);

        user.setMesasAsignadas(new ArrayList<>());
        repository.save(user);
    }
}
