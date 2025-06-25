package org.example.exercice3_rest.repository;

import org.example.exercice3_rest.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, Long> {

}
