package org.example.exercice3_rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.exercice3_rest.entity.Produit;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProduitReceiveDTO {
    private String nom;
    private double prix;

    public Produit dtoToEntity() {
        return Produit.builder()
                .nom(nom)
                .prix(prix)
                .build();
    }
}
