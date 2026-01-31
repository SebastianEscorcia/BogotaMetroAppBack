package com.sena.BogotaMetroApp.mapper.auth;

import com.sena.BogotaMetroApp.persistence.models.Usuario;
import com.sena.BogotaMetroApp.presentation.dto.auth.UserAfterAuthDTO;
import org.springframework.stereotype.Component;

@Component
public class UserAuthMapper {
    public UserAfterAuthDTO toDTO (Usuario usuario){
        UserAfterAuthDTO dto = new UserAfterAuthDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getDatosPersonales().getNombreCompleto());
        return dto;
    }
}
