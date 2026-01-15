package com.sena.BogotaMetroApp.presentation.dto.supportfaq;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryFaqResponseDTO {
    private Long id;
    private String name;
    private boolean active;
}
