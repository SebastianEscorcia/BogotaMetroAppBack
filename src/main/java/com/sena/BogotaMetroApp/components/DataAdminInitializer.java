package com.sena.BogotaMetroApp.components;

import com.sena.BogotaMetroApp.persistence.models.Role;
import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.persistence.repository.RoleRepository;
import com.sena.BogotaMetroApp.persistence.repository.UsuarioRepository;
import com.sena.BogotaMetroApp.utils.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(2)
@RequiredArgsConstructor
public class DataAdminInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.correo:admin@metro.com}")
    private String adminEmail;

    @Value("${app.admin.clave:admin123}")
    private String adminPass;


    @Override
    public void run(String... args) {

        if (usuarioRepository.findByCorreo(adminEmail).isEmpty()) {
            Role adminRole = roleRepository.findByNombre(RoleEnum.ADMIN.toString()).orElseThrow(() -> new RuntimeException("ERROR CRÍTICO: El Rol ADMIN no fue creado por el RoleInitializer"));

            Usuario admin = new Usuario();
            admin.setCorreo(adminEmail);
            admin.setClave(passwordEncoder.encode(adminPass));
            admin.setRol(adminRole);

            usuarioRepository.save(admin);

            if(adminEmail.isEmpty() && adminPass.isEmpty()){
                System.out.println("Admin creado con valores por defecto. Por favor cambie las credenciales inmediatamente.");
            } else {
                System.out.println("Admin creado con correo: " + adminEmail);
            }

        }
    }
}
