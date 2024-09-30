package com.farmstory.entity;



import com.farmstory.dto.TermsDTO;
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

    private String terms;
    private String privacy;

    public TermsDTO toDTO(){
        return TermsDTO.builder()
                .seq(seq)
                .terms(terms)
                .privacy(privacy)
                .build();
    }
}
