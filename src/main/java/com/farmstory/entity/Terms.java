package com.farmstory.entity;



import com.farmstory.dto.user.TermsDTO;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "terms")
public class Terms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seq;

    @Column(name = "terms", columnDefinition = "TEXT")
    private String terms;

    @Column(name = "privacy", columnDefinition = "TEXT")
    private String privacy;

    public TermsDTO toDTO(){
        return TermsDTO.builder()
                .seq(seq)
                .terms(terms)
                .privacy(privacy)
                .build();
    }
}
