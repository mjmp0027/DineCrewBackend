package com.dinecrew.dinecrewbackend.usuarios;

import com.dinecrew.dinecrewbackend.config.JwtResponse;
import com.dinecrew.dinecrewbackend.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.dinecrew.dinecrewbackend.utils.ValidUtils.isValidEmail;
import static com.dinecrew.dinecrewbackend.utils.ValidUtils.isValidUsername;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> login(
            @RequestBody UserDto loginRequest
    ) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody UserDto dto
    ) {
        String username = dto.getUsername();
        String email = dto.getEmail();
        String password = dto.getPassword();

        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(400).body("El nombre de usuario no puede estar vacío");
        }

        if (!isValidUsername(username)) {
            return ResponseEntity.status(400).body("El nombre de usuario contiene caracteres no permitidos");
        }

        User userOpt = userService.findByUsername(username);

        if (userOpt == null) {
            return ResponseEntity.status(400).body("El nombre de usuario ya está en uso");
        }

        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(400).body("El email no puede estar vacío");
        }

        if (!isValidEmail(email)) {
            return ResponseEntity.status(400).body("El email no es válido");
        }

        if (password == null || password.isEmpty()) {
            return ResponseEntity.status(400).body("La contraseña no puede estar vacía");
        }

        if (password.length() < 8) {
            return ResponseEntity.status(400).body("La contraseña debe tener al menos 8 caracteres");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        user = userService.save(user);
        return ResponseEntity.ok(UserDto.fromDocument(user));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody Map<String, String> request
            )
    {
        String username = request.get("username");
        userService.sendPasswordResetToken(username);
        return ResponseEntity.ok("Email enviado");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody Map<String, String> request
            )
    {

        Map<String, String> response = new HashMap<>();

        String token = request.get("token");
        String newPassword = request.get("newPassword");
        if (newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.status(400).body("La contraseña no puede estar vacía");
        }

        if (newPassword.length() < 8) {
            return ResponseEntity.status(400).body("La contraseña debe tener al menos 8 caracteres");
        }

        try {
            userService.resetPassword(token, newPassword);
        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }

        return ResponseEntity.ok("Contraseña restablecida");
    }
}
