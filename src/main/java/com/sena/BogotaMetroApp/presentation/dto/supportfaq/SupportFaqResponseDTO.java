package com.sena.BogotaMetroApp.presentation.dto.supportfaq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupportFaqResponseDTO {

    private Long id;
    private String question;
    private String answer;
    private boolean isActive;

   private Long categoryId;
}
