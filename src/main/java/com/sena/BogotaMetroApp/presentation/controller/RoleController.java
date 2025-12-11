package com.sena.BogotaMetroApp.presentation.controller;

import com.sena.BogotaMetroApp.persistence.models.Role;
import com.sena.BogotaMetroApp.services.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<Role> crearRole(@RequestParam String nombre) {
        Role rolCreado = roleService.crearRole(nombre);
        return ResponseEntity.ok(rolCreado);
    }

    @GetMapping
    public ResponseEntity<List<Role>> listarRoles() {
        return ResponseEntity.ok(roleService.listarRoles());
    }
}
