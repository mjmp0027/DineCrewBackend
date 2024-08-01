package com.dinecrew.dinecrewbackend.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping(value = {"/{username}"})
    public ResponseEntity<?> getUser(
            @PathVariable String username
    ) {
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body("El usuario con el username: " + username + " no se ha encontrado");
        }

        return ResponseEntity.ok(UserDto.fromDocument(userService.findByUsername(username)));
    }
}
