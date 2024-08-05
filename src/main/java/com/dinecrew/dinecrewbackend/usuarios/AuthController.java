package com.dinecrew.dinecrewbackend.usuarios;

import com.dinecrew.dinecrewbackend.config.JwtResponse;
import com.dinecrew.dinecrewbackend.config.JwtUtil;
import com.dinecrew.dinecrewbackend.config.Response;
import com.dinecrew.dinecrewbackend.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @Autowired
    private UserDetailsService userDetailsService;

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

        //Si el usuario tiene el rol cocinero no se le asignan mesas
        User user = userService.findByUsername(userDetails.getUsername());
        if (user.getRole() == Role.COCINERO) {
            return ResponseEntity.ok(new JwtResponse(jwt));
        }

        userService.asignarMesas(userDetails.getUsername());

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/logout/{username}")
    public ResponseEntity<?> logout(
            @PathVariable String username
    ) {
        Map<String, String> response = new HashMap<>();
        System.out.println("Logging out: " + username);
        userService.liberarMesas(username);
        response.put("message", "Sesión cerrada y mesas liberadas");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody UserDto dto
    ) {
        Map<String, String> response = new HashMap<>();
        String username = dto.getUsername();
        String email = dto.getEmail();
        String password = dto.getPassword();
        Role role = dto.getRole();

        if (username == null || username.isEmpty()) {
            response.put("message", "El nombre de usuario no puede estar vacío");
            return ResponseEntity.status(400).body(response);
        }

        if (!isValidUsername(username)) {
            response.put("message", "El nombre de usuario contiene caracteres no permitidos");
            return ResponseEntity.status(400).body(response);
        }

        User userOpt = userService.findByUsername(username);

        if (userOpt != null) {
            response.put("message", "El nombre de usuario ya está en uso");
            return ResponseEntity.status(400).body(response);
        }

        if (email == null || email.isEmpty()) {
            response.put("message", "El email no puede estar vacío");
            return ResponseEntity.status(400).body(response);
        }

        if (!isValidEmail(email)) {
            response.put("message", "El email no es válido");
            return ResponseEntity.status(400).body(response);
        }

        if (password == null || password.isEmpty()) {
            response.put("message", "La contraseña no puede estar vacía");
            return ResponseEntity.status(400).body(response);
        }

        if (password.length() < 8) {
            response.put("message", "La contraseña debe tener al menos 8 caracteres");
            return ResponseEntity.status(400).body(response);
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .mesasAsignadas(new ArrayList<>())
                .build();

        if (userService.countUsers() == 0) {
            userService.save(user);
            response.put("message", "Primer usuario registrado");
            return ResponseEntity.ok(response);
        } else {
            user = userService.save(user);
            return ResponseEntity.ok(UserDto.fromDocument(user));
        }

    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody Map<String, String> request
            )
    {
        String username = request.get("username");
        userService.sendPasswordResetToken(username);
        return ResponseEntity.ok(new Response("Email enviado"));
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
            return ResponseEntity.status(400).body(new Response("La contraseña no puede estar vacía"));
        }

        if (newPassword.length() < 8) {
            return ResponseEntity.status(400).body(new Response("La contraseña debe tener al menos 8 caracteres"));
        }

        try {
            userService.resetPassword(token, newPassword);
        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
        response.put("message", "Contraseña restablecida correctamente, ya puede cerrar esta ventana");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody String token) {
        String username = jwtUtil.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        boolean isValid = jwtUtil.validateToken(token, userDetails);
        return ResponseEntity.ok(isValid);
    }
}
