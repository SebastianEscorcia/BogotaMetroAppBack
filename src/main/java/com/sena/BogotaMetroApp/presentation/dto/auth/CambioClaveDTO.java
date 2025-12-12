package com.sena.BogotaMetroApp.presentation.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CambioClaveDTO {
    private String token;
    private String nuevaClave;
}
