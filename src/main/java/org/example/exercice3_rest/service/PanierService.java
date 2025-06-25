package org.example.exercice3_rest.service;

import jakarta.servlet.http.HttpSession;
import org.example.exercice3_rest.dto.ProduitResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PanierService {
    ProduitService produitService;
    HttpSession session;

    public PanierService(ProduitService produitService, HttpSession session) {
        this.produitService = produitService;
        this.session = session;
    }

    public Map<Long, Integer> addItemToPanier(Map<Long, Integer> panier, Long id) {
        if (panier == null) {
            panier = new HashMap<>();
        }

        //Vérifier que le produit existe
        try {
            produitService.get(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Produit non trouvé dans le panier");
        }

        panier.put(id, panier.getOrDefault(id, 0) + 1);
        return panier;
    }

    public Map<Long, Integer> removeItemFromPanier(Map<Long, Integer> panier, Long id) {
        if (panier == null) {
            throw new IllegalStateException("Panier inexistant");
        }
        if (panier.get(id) == null) {
            throw new IllegalArgumentException("Produit non trouvé dans le panier");
        } else{
            panier.put(id, panier.get(id) - 1);
            //Si il n'y a plus de quantité dans le panier, alors on retire le produit
            if (panier.get(id) == 0) {
                panier.remove(id);
            }
        }
        return panier;
    }

    public String validerPanier(Map<Long, Integer> panier){
        if (panier == null || panier.isEmpty()) {
            throw new RuntimeException("Panier vide");
        }

        //Récupérer les produits et calculer le total
        String affichagePanier = "Panier :\n";
        double total = 0;
        for (Map.Entry<Long, Integer> entry : panier.entrySet()) {
            ProduitResponseDTO detail = produitService.get(entry.getKey());
            total += detail.getPrix() * entry.getValue();

            //String pour afficher le panier;
            affichagePanier += detail.getNom() + " x" + entry.getValue() + " = " + detail.getPrix() * entry.getValue() + "\n";
        }
        affichagePanier += "Total : " + total;
        return affichagePanier;
    }
}
