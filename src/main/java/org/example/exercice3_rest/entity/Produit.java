package org.example.exercice3_rest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.exercice3_rest.dto.ProduitResponseDTO;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private double prix;

    public ProduitResponseDTO entityToDTO() {
        return ProduitResponseDTO.builder()
                .id(this.getId())
                .nom(this.getNom())
                .prix(this.getPrix())
                .build();
    }
}
