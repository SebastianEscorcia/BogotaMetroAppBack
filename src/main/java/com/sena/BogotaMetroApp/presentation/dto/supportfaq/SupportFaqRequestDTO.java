package com.sena.BogotaMetroApp.presentation.dto.supportfaq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupportFaqRequestDTO {
    @NotBlank
    private String question;

    @NotBlank
    private String answer;

    private boolean isActive = true;

    @NotNull
    private Long categoryFaqId;
}
