package com.sena.BogotaMetroApp.persistence.models.supportfaq;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "support_faqs")
public class SupportFaq {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String question;
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;

    private boolean isActive ;
    @ManyToOne
    @JoinColumn(name = "category_faq_id", nullable = false)
    private CategoryFaq categoryFaq;

}
